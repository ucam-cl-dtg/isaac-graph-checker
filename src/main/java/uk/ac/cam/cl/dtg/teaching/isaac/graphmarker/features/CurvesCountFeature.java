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

import uk.ac.cam.cl.dtg.teaching.isaac.graphmarker.data.Input;

import java.util.Collections;
import java.util.List;

/**
 * An input feature which requires a specific number of lines to be drawn.
 */
public class CurvesCountFeature implements InputFeature<CurvesCountFeature.Instance> {

    public static final CurvesCountFeature manager = new CurvesCountFeature();

    /**
     * Private constructor; use the manager singleton.
     */
    private CurvesCountFeature() {
    }

    @Override
    public String tag() {
        return "curves";
    }

    /**
     * An instance of the CurvesCount feature.
     */
    public class Instance implements InputFeature.Instance {

        private final int count;

        /**
         * Create a curve count feature.
         * @param count Required number of curves.
         */
        private Instance(int count) {
            this.count = count;
        }

        @Override
        public boolean match(Input input) {
            return input.getLines().size() == count;
        }
    }

    @Override
    public Instance deserialize(String featureData) {
        try {
            return new Instance(Integer.valueOf(featureData.trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not a number: " + featureData, e);
        }
    }

    @Override
    public List<String> generate(Input expectedInput) {
        if (expectedInput.getLines().size() < 2) {
            return Collections.emptyList();
        }
        return Collections.singletonList(Integer.toString(expectedInput.getLines().size()));
    }
}
