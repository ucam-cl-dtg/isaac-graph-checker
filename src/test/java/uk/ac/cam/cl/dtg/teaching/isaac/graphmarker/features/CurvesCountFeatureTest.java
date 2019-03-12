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
package uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.features;

import org.junit.Test;
import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.data.Input;

import java.util.List;

import static org.junit.Assert.*;
import static uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.TestHelpers.inputOf;
import static uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.TestHelpers.lineOf;

public class CurvesCountFeatureTest {

    @Test
    public void simpleCurveCountWorks() {
        List<String> data = CurvesCountFeature.manager.generate(inputOf(
            lineOf(x -> x, -10, 0),
            lineOf(x -> x, 0, 10)
        ));

        Input input = inputOf(lineOf(x -> 1.0, -10, 10), lineOf(x -> 0.0, -10, 10));

        assertTrue(CurvesCountFeature.manager.deserialize(data.get(0)).match(input));
    }

    @Test
    public void oneCurveDoesntGenerateAFeature() {
        Input input = inputOf(lineOf(x -> 1.0, -10, 10));
        assertEquals(0, CurvesCountFeature.manager.generate(input).size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void notAnumberThrows() {
        CurvesCountFeature.manager.deserialize("foo");
    }
}