package uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.features;

import org.junit.Test;
import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.data.Line;
import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.data.Point;
import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.geometry.Sector;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.geometry.TestHelpers.lineOf;

public class ExpectedSectorsFeatureTest {

    private static ExpectedSectorsFeature feature = new ExpectedSectorsFeature();
    private static ExpectedSectorsFeature.Data featureData = feature.deserialize("through:");

    @Test
    public void basicLineHasCorrectSectorList() {
        List<Sector> sectorList =  featureData.convertLineToSectorList(lineOf(
                new Point(-1, 1),
                new Point(2, -1)
        ));

        assertEquals("[topLeft, onAxisWithPositiveY, topRight, onAxisWithPositiveX, bottomRight]", sectorList.toString());
    }

    @Test
    public void xSquaredMinusTwoHasCorrectSectorList() {
        Predicate<Line> testFeature = feature.matcher(feature.deserialize(
                "through: topLeft, onAxisWithNegativeX, bottomLeft, onAxisWithNegativeY, bottomRight, onAxisWithPositiveX, topRight"));

        assertTrue(testFeature.test(lineOf(x -> x*x - 2, -5, 5)));
        assertFalse(testFeature.test(lineOf(x -> x*x + 2, -5, 5)));
    }

    @Test
    public void xCubedPlusSquaredMinusTwoHasCorrectSectorList() {
        Predicate<Line> testFeature = feature.matcher(feature.deserialize(
                "through: bottomLeft, onAxisWithNegativeX, topLeft, onAxisWithPositiveY, topRight, onAxisWithPositiveX, bottomRight, onAxisWithPositiveX, topRight"));

        assertTrue(testFeature.test(lineOf(x -> x*x*x - 3*x*x + 2, -10, 10)));
        assertFalse(testFeature.test(lineOf(x -> x*x - 3*x*x + 2, -10, 10)));
    }

    @Test
    public void cosXFromMinus2PiTo2Pi() {
        Predicate<Line> testFeature = feature.matcher(feature.deserialize(
                "through:topLeft, onAxisWithNegativeX, bottomLeft, onAxisWithNegativeX, topLeft, onAxisWithPositiveY, topRight, onAxisWithPositiveX, bottomRight, onAxisWithPositiveX, topRight"));

        assertTrue(testFeature.test(lineOf(x -> Math.cos(x), -2 * Math.PI, 2 * Math.PI)));
        assertFalse(testFeature.test(lineOf(x -> Math.cos(x + Math.PI / 2), -2 * Math.PI, 2 * Math.PI)));
    }

    @Test
    public void sinXFromMinus2PiTo2Pi() {
        Predicate<Line> testFeature = feature.matcher(feature.deserialize(
                "through:  onAxisWithNegativeX, topLeft, onAxisWithNegativeX, bottomLeft, origin, topRight, onAxisWithPositiveX, bottomRight, onAxisWithPositiveX"));

        assertTrue(testFeature.test(lineOf(x -> Math.sin(x), -2 * Math.PI, 2 * Math.PI)));
        assertFalse(testFeature.test(lineOf(x -> Math.cos(x), -2 * Math.PI, 2 * Math.PI)));
    }

    @Test
    public void matchCorrectlyWhenStartingAtOrigin() {
        Predicate<Line> testFeature = feature.matcher(feature.deserialize("through:origin, topRight"));

        assertTrue(testFeature.test(lineOf(x -> x, 0, 10)));
    }

    @Test
    public void customSectionListMatches() {
        Sector[] sectors = {Sector.topLeft, Sector.bottomRight};
        ExpectedSectorsFeature feature = new ExpectedSectorsFeature(Arrays.asList(sectors));

        Predicate<Line> testFeature = feature.matcher(feature.deserialize("through:topLeft, bottomRight"));

        assertTrue(testFeature.test(lineOf(x -> 2 - x, -5, 5)));
    }

    @Test
    public void generateMatchesItself() {
        ExpectedSectorsFeature.Data data = feature.generate((lineOf(x -> Math.cos(x), -2 * Math.PI, 2 * Math.PI)));

        Predicate<Line> testFeature = feature.matcher(data);

        assertTrue(testFeature.test(wobbly(lineOf(x -> Math.cos(x), -2 * Math.PI, 2 * Math.PI))));
    }

    private Line wobbly(Line points) {
        return new Line(points.stream()
                .map(p -> new Point(
                        p.getX() + Math.random() * 0.02 - 0.01,
                        p.getY() + Math.random() * 0.02 - 0.01))
                .collect(Collectors.toList()));
    }

}