/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.optaplanner.core.impl.score.director.drools.testgen;

import java.io.File;
import java.util.List;

import org.kie.api.KieBase;
import org.optaplanner.core.impl.score.director.drools.DroolsScoreDirector;
import org.optaplanner.core.impl.score.director.drools.LegacyDroolsScoreDirectorFactory;

public class TestGenLegacyDroolsScoreDirectorFactory<Solution_> extends LegacyDroolsScoreDirectorFactory<Solution_> {

    private final List<String> scoreDrlList;
    private final List<File> scoreDrlFileList;

    public TestGenLegacyDroolsScoreDirectorFactory(KieBase kieBase, List<String> scoreDrlList, List<File> scoreDrlFileList) {
        super(kieBase);
        this.scoreDrlList = scoreDrlList;
        this.scoreDrlFileList = scoreDrlFileList;
    }

    @Override
    public DroolsScoreDirector<Solution_> buildScoreDirector(
            boolean lookUpEnabled, boolean constraintMatchEnabledPreference) {
        return new TestGenDroolsScoreDirector<>(
                this, lookUpEnabled, constraintMatchEnabledPreference, scoreDrlList, scoreDrlFileList);
    }

}
