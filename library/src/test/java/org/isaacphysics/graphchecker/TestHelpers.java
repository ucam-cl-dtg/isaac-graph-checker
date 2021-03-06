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
package org.isaacphysics.graphchecker;

import org.isaacphysics.graphchecker.data.Input;
import org.isaacphysics.graphchecker.data.Point;
import org.isaacphysics.graphchecker.data.Line;
import org.isaacphysics.graphchecker.data.PointOfInterest;
import org.isaacphysics.graphchecker.data.PointType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestHelpers {

    public static Line lineOf(List<Point> points) {
        List<PointOfInterest> pointsOfInterest = new ArrayList<>();

        Supplier<PointType> lastPointType = () -> pointsOfInterest.isEmpty() ? null
            : pointsOfInterest.get(pointsOfInterest.size() - 1).getPointType();

        if (points.size() > 2) {
            for (int i = 1; i < points.size() - 1; i++) {
                Point p1 = points.get(i - 1);
                Point p2 = points.get(i);
                Point p3 = points.get(i + 1);
                Point p4 = null;
                if (i + 2 < points.size()) {
                    p4 = points.get(i + 2);
                }
                if (p1.getY() > p2.getY() && PointType.MINIMA != lastPointType.get()) {
                    if (p3.getY() > p2.getY()) {
                        pointsOfInterest.add(new PointOfInterest(p2, PointType.MINIMA));
                    } else if (p2.getY() == p3.getY() && p4 != null && p4.getY() > p2.getY()) {
                        pointsOfInterest.add(new PointOfInterest(p2.add(p3).times(0.5), PointType.MINIMA));
                    }
                }
                if (p1.getY() < p2.getY() && PointType.MAXIMA != lastPointType.get()) {
                    if (p3.getY() < p2.getY()) {
                        pointsOfInterest.add(new PointOfInterest(p2, PointType.MAXIMA));
                    } else if (p2.getY() == p3.getY() && p4 != null && p4.getY() < p2.getY()) {
                        pointsOfInterest.add(new PointOfInterest(p2.add(p3).times(0.5), PointType.MAXIMA));
                    }
                }
            }
        }
        return new Line(points, pointsOfInterest);
    }

    public static Line lineOf(Point... points) {
        return lineOf(Arrays.asList(points));
    }

    public static Line lineOf(double... coords) {
        if (coords.length % 2 != 0) {
            throw new IllegalArgumentException("Odd number of values for coordinates");
        }
        Point[] points = new Point[coords.length / 2];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(coords[i * 2], coords[i * 2 + 1]);
        }
        return lineOf(points);
    }

    public static Line lineOf(Function<Double, Double> f, double minX, double maxX) {
        int points = 101; // Make it easier to hit symmetric points
        double diff = (maxX - minX) / (points - 1);
        return lineOf(IntStream.range(0, points)
            .mapToDouble(i -> minX + diff * i)
            .mapToObj(x -> new Point(x, f.apply(x)))
            .collect(Collectors.toList()));
    }

    public static Input inputOf(Function<Double, Double> f, double minX, double maxX) {
        return new Input(Collections.singletonList(lineOf(f, minX, maxX)));
    }

    public static Input inputOf(Line... lines) {
        return new Input(Arrays.asList(lines));
    }
}