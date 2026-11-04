/****************************************************************************
 *
 * TimeWeighted.java -- time-weighted average.
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

/**
 * This class computes the time-weighted average of a sequence of
 * observations (0, 0), (v_1, t_1), (v_2, t_2), ... (v_{n-1},
 * t_{n-1}).  The initial observation is assumed to be the value 0 at
 * time 0.
 */
public class TimeWeighted implements Variable {
    final String name;
    double lastVal;
    double lastTime;
    double sum; // running integral of the observations
    int count; // number of observations

    public TimeWeighted(String name) {
        this.name = name;
        lastVal = 0;
        lastTime = 0;
        sum = 0;
        count = 0;
    }

    public void update(double val, double t) {
        assert(t >= lastTime);

        count++;
        sum += lastVal * (t - lastTime);
        lastVal = val;
        lastTime = t;
    }

    /**
     * Returns the time-weighted average of the observations.
     * Requires a non-zero observation period.
     */
    public double get( ) {
        assert(lastTime > 0);

        return sum / lastTime;
    }

    public void report( ) {
        System.out.println();
        System.out.println();
        System.out.println("******************************************************************************");
        System.out.println("**");
        System.out.println("** " + name);
        System.out.println("**");
        System.out.println();

        System.out.printf("Number of observations... %d\n", count);
        System.out.printf("Observation period....... %.3f\n", lastTime);
        System.out.printf("Time-weighted average.... %.3f\n", get());

        System.out.println();
        System.out.println("** end " + name);
    }
}
