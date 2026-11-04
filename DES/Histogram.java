/****************************************************************************
 *
 * Histogram.java - compute histogram of observations.
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
 *
 */
public class Histogram implements Variable {
    final String name;
    double left, right, width; // left and right bounds of the histogram
    double min, max; // min and max value observed
    double sum; // sum of observations (for computing the mean)
    int nbins, count;
    int counts[];

    public Histogram( String name, double left, double right, int nbins ) {
        assert(left < right);
        assert(nbins > 0);

        this.name = name;
        this.left = left;
        this.right = right;
        this.min = Double.POSITIVE_INFINITY;
        this.max = Double.NEGATIVE_INFINITY;
        this.sum = 0;
        this.nbins = nbins;
        this.width = (right - left) / nbins;
        this.count = 0;
        this.counts = new int[nbins+2];
    }

    public void update( double val ) {
        int bin;

        if (val < left)
            bin = 0;
        else if (val > right)
            bin = nbins + 1;
        else
            bin = 1 + (int)Math.floor((val - left) / width);
        counts[bin]++;
        sum += val;
        count++;
        if (val < min)
            min = val;
        if (val > max)
            max = val;
    }

    /**
     * Return the count.
     */
    public double get( ) {
        return count;
    }

    public void report( ) {
        final int LEN = 50;

        assert(count > 0);

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
        System.out.printf("Mean..................... %.3f\n", sum / count);
        System.out.println();
        System.out.println("cell/lower lim/   n/ freq/ cum%");
        for (int i=0; i<nbins+2; i++) {
            System.out.printf("%4d ", i);
            if (i == 0)
                System.out.printf("-infinity ");
            else
                System.out.printf("%9.2f ", min + width * (i-1));
            System.out.printf("%4d ", counts[i]);
            System.out.printf("%5.2f ", counts[i] / (double)count);
            System.out.printf("%6.2f ", 100 * counts[i] / (double)count);
            System.out.println("*".repeat(LEN * counts[i] / count));
        }
        System.out.println();
        System.out.println("** end " + name);
    }
}
