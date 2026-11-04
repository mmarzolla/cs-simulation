/****************************************************************************
 *
 * Simulation.java - A discrete-event simulation engine
 *
 * Copyright (C) 2026 Moreno Marzolla
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
import java.util.PriorityQueue;

public abstract class Simulation {
    PriorityQueue<Event> FEL; // Future Event List
    double time = 0;          // Current simulation time

    public abstract class Event implements Comparable<Event> {
        double evtime;

        public Event( double evtime ) { this.evtime = evtime; }

        /**
         * Return the time at which this event occurs.
         */
        public double time( ) { return evtime; }

        /**
         * An Event must implement the Comparable interface, since it
         * will be inserted into a priority queue. Comparison of
         * events happen by comparing the timestamps.
         */
        public int compareTo( Event other ) {
            if (time() < other.time())
                return -1;
            else if (time() > other.time())
                return 1;
            else
                return 0;
        }

        /**
         * User-defined actions executed by this event.
         */
        public abstract void execute( );
    }

    public Simulation( ) {
        this.FEL = new PriorityQueue<Event>();
    }

    /**
     * Returns the current simulation time.
     */
    public double now( ) { return time; }

    /**
     * Return true iff the simulation should terminate. To be
     * redefined by subclasses.
     */
    public boolean finished( ) { return false; }

    /**
     * Run the simulation until the event list becomes empty, or we
     * hit some user-defined stopping criteria.
     */
    public void run( ) {
        while (!FEL.isEmpty() && !finished()) {
            Event ev = FEL.poll();
            time = ev.time();
            ev.execute();
        }
    }

    /**
     * Insert event ev in the FEL. If there already is another event
     * that is scheduled at the exact same time, the relative order of
     * execution is unspecified.
     */
    public final void schedule( Event ev ) { FEL.offer(ev); }

    /**
     * Remove the first occurrence of event ev (if any) from the
     * future event list.
     */
    public final void cancel( Event ev ) { FEL.remove(ev); }
}
