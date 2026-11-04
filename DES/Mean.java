/****************************************************************************
 *
 * Mean.java -- mean computation.
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
 * This class computes the minimum, maximum, and average of a
 * sequence of observations v_0, v_1, ... v_{n-1}.
 */
public class Mean implements Variable {
    final String name;
    double min, max, sum, sumsq;
    int count;

    public Mean( String name ) {
        this.name = name;
        this.sum = 0;
        this.sumsq = 0;
        this.count = 0;
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
    }

    public void update( double val ) {
        count++;
        sum += val;
        sumsq += val*val;
        if (val > max)
            max = val;
        if (val < min)
            min = val;
    }

    /**
     * Return the average; requires nonempty set of observations.
     */
    public double get( ) {
        assert(count > 0);

        return sum / count;
    }

    public void report( ) {
        assert( count > 1 );

        final double mean = get( );
        final double variance = 1/(double)(count -1) * (sumsq - 2*mean*sum + count * mean*mean);
        final double stddev = Math.sqrt(1 / (double)count * (sumsq - 2*mean*sum + count * mean*mean) );

        System.out.println();
        System.out.println();
        System.out.println("******************************************************************************");
        System.out.println("**");
        System.out.println("** " + name);
        System.out.println("**");
        System.out.println();
        System.out.printf("Number of observations... %d\n", count);
        System.out.printf("Minimum.................. %.3f\n", min);
        System.out.printf("Maximum.................. %.3f\n", max);
        System.out.printf("Mean..................... %.3f\n", get());
        System.out.printf("Variance................. %.3f\n", variance);
        System.out.printf("Standard deviation....... %.3f\n", stddev);
        System.out.println();
        System.out.println("** end " + name);

    }
}
