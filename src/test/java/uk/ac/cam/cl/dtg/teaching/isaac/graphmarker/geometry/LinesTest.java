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
package uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.geometry;

import org.junit.Test;
import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.data.Line;

import java.util.List;

import static org.junit.Assert.*;
import static uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.TestHelpers.lineOf;

public class LinesTest {

    @Test
    public void splitOnPointsOfInterest() {
        Line line = lineOf(-10,0, -5,5, 0,0, 5,-5, 10,0);

        List<Line> lines = Lines.splitOnPointsOfInterest(line);

        assertEquals(3, lines.size());
        assertEquals(lineOf(-10,0, -5,5), lines.get(0));
        assertEquals(lineOf(-5,5, 0,0, 5,-5), lines.get(1));
        assertEquals(lineOf(5,-5, 10, 0), lines.get(2));
    }
}