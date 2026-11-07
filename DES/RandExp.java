/****************************************************************************
 *
 * RandExp.java - Exponential RNG
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

import java.util.Random;

/**
 * Utility class to generate pseudo-random numbers with exponential
 * distribution with rate lambda.
 */
public class RandExp {
    Random rng;
    double lambda;

    /**
     * This RNG will generate a stream of pseudo-random numbers
     * that exponentially distributed with mean (1/lambda).
     *
     * This class is for illustrative purposes only and should not
     * be used in production:
     *
     * - Each instance of this class relies on an instance of a
     *   Java Random object that is initialized
     *   non-deterministically. A mechanism should be provided to
     *   initialize the RNG deterministically, to guarantee
     *   repeatability of simulation runs.
     *
     * - In this implementation, different instances of the
     *   RandExp class are not guaranteed to produce
     *   non-overlapping sequences of pseudo-random numbers.
     */
    public RandExp(double lambda) {
        this.rng = new Random();
        this.lambda = lambda;
    }

    public double nextDouble( ) {
        return Math.log(1-rng.nextDouble())/(-lambda);
    }
}
