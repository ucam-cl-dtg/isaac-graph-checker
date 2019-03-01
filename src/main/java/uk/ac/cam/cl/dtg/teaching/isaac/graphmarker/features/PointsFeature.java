package uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.features;

import com.google.common.collect.Streams;
import org.apache.commons.lang3.tuple.ImmutablePair;
import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.data.Line;
import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.data.PointType;
import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.geometry.Sector;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PointsFeature implements LineFeature<PointsFeature.Instance> {

    public static final PointsFeature manager = new PointsFeature();

    @Override
    public String TAG() { return "points"; }

    protected class Instance implements LineFeature.Instance {

        private final List<ImmutablePair<PointType, Sector>> expectedPoints;

        Instance(List<ImmutablePair<PointType, Sector>> expectedPoints) {
            this.expectedPoints = expectedPoints;
        }

        @Override
        public String serialize() {
            return expectedPoints.stream()
                .map(entry -> entry.getLeft() + " in " + entry.getRight())
                .collect(Collectors.joining(", "));
        }

        @Override
        public boolean match(Line line) {
            if (expectedPoints.size() != line.getPointsOfInterest().size()) return false;

            return Streams.zip(expectedPoints.stream(), line.getPointsOfInterest().stream(),
                (expected, actual) -> expected.getLeft() == actual.getPointType()
                    && Sector.classify(actual, Sector.defaultOrderedSectors).contains(expected.getRight()))
                .allMatch(Boolean::booleanValue);
        }
    }

    @Override
    public Instance deserialize(String featureData) {
        String[] items = featureData.split("\\s*,\\s*");
        return new Instance(Arrays.stream(items)
            .map(item -> {
                String[] parts = item.split(" in ");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Incorrect number of point parts in: " + item);
                }
                PointType expectedType = PointType.valueOf(parts[0].trim().toUpperCase());
                Sector expectedSector = Sector.byName(parts[1].trim());
                return ImmutablePair.of(expectedType, expectedSector);
            })
            .collect(Collectors.toList()));
    }

    @Override
    public String generate(Line expectedLine) {
        return new Instance(expectedLine.getPointsOfInterest().stream()
            .map(point -> ImmutablePair.of(point.getPointType(), Sector.classify(point)))
            .collect(Collectors.toList())
        ).serialize();
    }

    private PointsFeature() {
    }
}
