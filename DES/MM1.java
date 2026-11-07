/****************************************************************************
 *
 * MM1.java - Event-based simulation of a M/M/1 queue.
 *
 * Copyright (C) 2026 Moreno Marzolla.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 ****************************************************************************/

import java.util.LinkedList;
import java.io.*;

/**
 * This class simulates a M/M/1 queueing center. Inter-arrival times
 * of requests are exponentially distributed, as are service times.
 *
 * Let \lambda denote the arrival rate, and \mu the service rate.
 * According to queueing theory, the utilization of the server is U =
 * \rho = \lambda / \mu, the average number Q of requests in the system is:
 *
 * Q = \rho / (1 - \rho)
 *
 * and the average response time is
 *
 * R = \rho / (\mu - \lambda)
 */
public class MM1 extends Simulation {
    Server srv;
    RandExp arrivals;   // inter-arrival times generator
    int nCompleted = 0; // number of completed jobs
    int maxJobs = 1000; // how many jobs to generate
    PrintStream outStream;

    /**
     * A job is a container of some state variables. We store various
     * times related to the job lifecycle that are useful for
     * computing statistics.
     */
    protected class Job {
        static int jobIdCounter = 0;
        final int jobId;
        double arrivalTime;
        double serviceStartTime;
        double serviceCompleteTime;

        public Job( ) {
            this.jobId = jobIdCounter++;
        }
    }

    /**
     * A server consists of FIFO queue of jobs that are waiting for
     * service, and a counter (inService) that indicates how many jobs
     * are currently being served (0 or 1).
     */
    protected class Server {
        LinkedList<Job> queue; // FIFO queue of jobs waiting for service
        RandExp srvTime;       // service time distribution
        int inService;         // number of jobs being served (0 or 1)
        TimeWeighted nreq;     // average number of requests in the system
        TimeWeighted util;     // utilization
        Mean respTime;         // average response time

        public Server( double serviceRate ) {
            queue = new LinkedList<Job>();
            srvTime = new RandExp(serviceRate);
            inService = 0;
            nreq = new TimeWeighted("Mean number of requests");
            util = new TimeWeighted("Utilization");
            respTime = new Mean("Mean response time");
        }

        /**
         * Arrival of a new job j.
         */
        public void onArrival(Job j) {
            j.arrivalTime = now();
            queue.add(j);
            if (inService == 0) {
                schedule(new ServiceStartEvent(now()));
            }
            System.out.printf("%8.3f %4d Arrival\n", j.arrivalTime, j.jobId);
        }

        /**
         * Start service of the first job in the queue.
         */
        public void onServiceStart( ) {
            assert(queue.size() > 0);
            assert(inService == 0);

            final Job j = queue.poll();

            j.serviceStartTime = now();
            inService = 1;
            util.update(inService, now());
            schedule(new ServiceCompleteEvent(now() + srvTime.nextDouble(), j));
            System.out.printf("%8.3f %4d ServiceStart\n", j.serviceStartTime, j.jobId);
        }

        /**
         * Completion of job j.
         */
        public void onServiceComplete( Job j ) {
            assert(inService == 1);

            j.serviceCompleteTime = now();
            nCompleted++;
            inService = 0;
            util.update(inService, now());
            if (queue.size() > 0) {
                schedule(new ServiceStartEvent(now()));
            }
            System.out.printf("%8.3f %4d ServiceComplete\n", j.serviceCompleteTime, j.jobId);
            nreq.update(njobs(), now());
            respTime.update(j.serviceCompleteTime - j.arrivalTime);
        }

        /**
         * Total number of jobs in the system, which includes those in
         * the queue and the job being served (if any).
         */
        public int njobs( ) {
            return queue.size() + inService;
        }

        /**
         * Report statistics
         */
        public void report( ) {
            nreq.report();
            util.report();
            respTime.report();
        }
    }

    /**********************************************************************
     *
     * Arrival of a new job.
     *
     **********************************************************************/
    public class ArrivalEvent extends Event {

        public ArrivalEvent( double time ) {
            super(time);
        }

        public void execute( ) {
            srv.onArrival( new Job() );
            // Generate next arrival
            double nextArrival = time() + arrivals.nextDouble();
            schedule(new ArrivalEvent(nextArrival));
        }
    }

    /**********************************************************************
     *
     * Start service of the first job in the queue.
     *
     **********************************************************************/
    public class ServiceStartEvent extends Event {

        public ServiceStartEvent( double time ) {
            super(time);
        }

        public void execute( ) {
            srv.onServiceStart( );
        }
    }

    /**********************************************************************
     *
     * Service completion of the first job in the queue.
     *
     **********************************************************************/
    public class ServiceCompleteEvent extends Event {
        protected Job j;

        public ServiceCompleteEvent( double time, Job j ) {
            super(time);
            this.j = j;
        }

        public void execute( ) {
            srv.onServiceComplete( j );
            outStream.printf("%d,%.3f,%.3f,%.3f\n",
                             j.jobId, j.arrivalTime, j.serviceStartTime, j.serviceCompleteTime);
        }
    }

    /**
     * The simulation finishes when 1000 jobs are processed.
     */
    public boolean finished() { return nCompleted >= maxJobs; }

    /**
     * Instantiate the simulation.
     */
    public MM1( double arrivalRate, double serviceRate, String fileName, int maxJobs ) {
        this.maxJobs = maxJobs;
        arrivals = new RandExp(arrivalRate);
        srv = new Server(serviceRate);
        try {
            outStream = new PrintStream(new File(fileName) );
        } catch(FileNotFoundException ex) {
            System.err.printf("Can not create output file " + fileName);
        }
        outStream.printf("jobId,arrivalTime,startTime,endTime\n");
        // Start the simulation by creating a single arrival event;
        // each arrival generates the next arrival.
        System.out.println("    Time  Job Event");
        System.out.println("-------- ---- -----------------------");
        schedule(new ArrivalEvent(now()));
    }

    /**
     * To execute this program:
     *
     * javac *.java
     * java MM1 [lambda [mu [outfile [maxjobs]]]]
     *
     * Example:
     * java MM1 0.8 1.0 out.csv 500
     */
    public static void main(String[] args) {
        double lambda = 0.9;
        double mu = 1.0;
        String fileName = "mm1.csv";
        int maxJobs = 5000;

        if (args.length > 0)
            lambda = Double.valueOf(args[0]);
        if (args.length > 1)
            mu = Double.valueOf(args[1]);
        if (args.length > 2)
            fileName = args[2];
        if (args.length > 3)
            maxJobs = Integer.valueOf(args[3]);
        assert(lambda < mu);
        MM1 sim = new MM1(lambda, mu, fileName, maxJobs);
        sim.run();
        sim.srv.report();
    }
}
