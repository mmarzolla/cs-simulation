/****************************************************************************
 *
 * SimTest.java - Demo simulation
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

public class SimTest extends Simulation {
    public class MyEvent extends Event {
        final String name;
        public MyEvent( double time, String name ) {
            super(time);
            this.name = name;
        }
        public void execute( ) {
            System.out.println("Event " + name + " at time " + time());
        }
    }

    public SimTest( ) {
        Event e1 = new MyEvent(5, "e1");
        Event e2 = new MyEvent(10, "e2");
        Event e3 = new MyEvent(10, "e3");
        Event e4 = new MyEvent(21, "e4");
        schedule( e1 );
        schedule( e2 );
        scheduleBefore( e3, e2 );
        schedule( e4 );
    }

    public static void main( String[] args ) {
        Simulation sim = new SimTest();
        sim.run();
    }
}
