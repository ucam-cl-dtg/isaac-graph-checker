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

/**
 * Represents a rectangular region.
 */
public class Rect {
    private final double left;
    private final double right;
    private final double top;
    private final double bottom;

    /**
     * Create a rectangle.
     * @param left The low x co-ordinate.
     * @param right The high x co-ordinate.
     * @param top The high y co-ordinate.
     * @param bottom The low y co-ordinate.
     */
    public Rect(double left, double right, double top, double bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    @SuppressWarnings("javadocMethod")
    public double getLeft() {
        return left;
    }

    @SuppressWarnings("javadocMethod")
    public double getRight() {
        return right;
    }

    @SuppressWarnings("javadocMethod")
    public double getTop() {
        return top;
    }

    @SuppressWarnings("javadocMethod")
    public double getBottom() {
        return bottom;
    }
}
