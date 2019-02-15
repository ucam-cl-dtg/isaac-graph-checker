package uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.geometry;

import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.data.Intersection;
import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.data.IntersectionParams;
import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.data.Line;
import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.data.Point;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a region of the graph, e.g. above the x axis, or on the y axis with y < 0
 */
public class Sector {

    private static final double AXIS_SLOP = 0.01;
    private static final double ORIGIN_SLOP = AXIS_SLOP * 2;
    private static final Point UP = new Point(0, 1);
    private static final Point DOWN = new Point(0, -1);
    private static final Point RIGHT = new Point(1, 0);
    private static final Point LEFT = new Point(-1, 0);

    private final String name;
    private final List<Segment> segments;

    private Sector(String name, List<Segment> segments) {
        this.name = name;
        this.segments = segments;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean contains(Point p) {
        return this.segments.stream().allMatch(segment -> segment.inside(p));
    }

    private boolean intersects(Segment s) {
        return this.segments.stream().anyMatch(segment -> segment.intersects(s));
    }

    public Intersection intersects(Line line) {
        boolean allInside = true;
        boolean someInside = false;
        boolean anyIntersections = false;
        Point lastPoint = null;
        for (Point point : line) {
            if (contains(point)) {
                someInside = true;
            } else {
                allInside = false;
            }
            if (lastPoint != null) {
                anyIntersections |= intersects(Segment.closed(lastPoint, point));
            }
            lastPoint = point;
        }
        return  allInside && !anyIntersections ? Intersection.INSIDE
                : someInside || anyIntersections ? Intersection.INTERSECTS
                : Intersection.OUTSIDE;
    }

    public IntersectionParams intersectionParams(Segment lineSegment) {
        return new IntersectionParams(this.segments.stream()
                .map(segment -> segment.intersectionParam(lineSegment))
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList()));
    }

    /*public boolean whollyContains(Line line) {
        return false;
    }*/

    private static Sector quadrant(String name, Point origin, Point axis1, Point axis2) {
        return new Sector(name, Arrays.asList(
            Segment.openOneEnd(origin, axis1, axis2),
            Segment.openOneEnd(origin, axis2, axis1)
        ));
    }

    private static Sector centeredQuadrant(String name, Point axis1, Point axis2) {
        Point origin = new Point(0, 0);
        return quadrant(name, origin, axis1, axis2);
    }

    public static Sector topRight() {
        return centeredQuadrant("topRight", RIGHT, UP);
    }

    public static Sector topLeft() {
        return centeredQuadrant("topLeft", LEFT, UP);
    }

    public static Sector bottomLeft() {
        return centeredQuadrant("bottomLeft", LEFT, DOWN);
    }

    public static Sector bottomRight() {
        return centeredQuadrant("bottomRight", RIGHT, DOWN);
    }

    private static Sector sloppyAxis(String name, Point left, Point right, Point axis) {
        left = left.times(AXIS_SLOP);
        right = right.times(AXIS_SLOP);
        return new Sector(name, Arrays.asList(
                Segment.closed(left, right),
                Segment.openOneEnd(left, axis, Side.RIGHT),
                Segment.openOneEnd(right, axis, Side.LEFT)
        ));
    }

    public static Sector onAxisWithPositiveY() {
        return sloppyAxis("onAxisWithPositiveY", LEFT, RIGHT, UP);
    }

    public static Sector onAxisWithNegativeY() {
        return sloppyAxis("onAxisWithNegativeY", RIGHT, LEFT, DOWN);
    }

    public static Sector onAxisWithPositiveX() {
        return sloppyAxis("onAxisWithPositiveX", UP, DOWN, RIGHT);
    }

    public static Sector onAxisWithNegativeX() {
        return sloppyAxis("onAxisWithNegativeX", DOWN, UP, LEFT);
    }

    public static Sector origin() {
        Point[] p = new Point[]{new Point(ORIGIN_SLOP, ORIGIN_SLOP), new Point(-ORIGIN_SLOP, ORIGIN_SLOP), new Point(-ORIGIN_SLOP, -ORIGIN_SLOP), new Point(ORIGIN_SLOP, -ORIGIN_SLOP)};
        return new Sector("origin", Arrays.asList(
            Segment.closed(p[0], p[1]),
            Segment.closed(p[1], p[2]),
            Segment.closed(p[2], p[3]),
            Segment.closed(p[3], p[0])
        ));
    }
}