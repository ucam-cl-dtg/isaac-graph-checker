/**
 * Copyright 2019 University of Cambridge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.isaacphysics.graphchecker.data;

import java.util.Objects;

/**
 * Represents a single point.
 */
public class Point {
    private final double x;
    private final double y;

    /**
     * Create a point.
     * @param x The x co-ordinate.
     * @param y The y co-ordinate.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @SuppressWarnings("javadocMethod")
    public double getX() {
        return x;
    }

    @SuppressWarnings("javadocMethod")
    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Add another point to this one and return as a new point.
     * @param p Point to add.
     * @return The new point.
     */
    public Point add(Point p) {
        return new Point(x + p.x, y + p.y);
    }

    /**
     * Subtract another point from this one and return as a new point.
     * @param p Point to subtract.
     * @return The new point.
     */
    public Point minus(Point p) {
        return new Point(x - p.x, y - p.y);
    }

    /**
     * Multiple the co-ordinates by a constant factor and return as a new point.
     * @param m The factor to multiply by.
     * @return The new point.
     */
    public Point times(double m) {
        return new Point(x * m, y * m);
    }

}
