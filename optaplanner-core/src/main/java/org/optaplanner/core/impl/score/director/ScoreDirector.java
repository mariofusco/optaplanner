/*
 * Copyright 2011 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.impl.score.director;

import java.util.Collection;

import org.optaplanner.core.api.domain.lookup.LookUpStrategyType;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.constraint.ConstraintMatchTotal;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.impl.domain.variable.descriptor.VariableDescriptor;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.solver.ProblemFactChange;

/**
 * The ScoreDirector holds the {@link PlanningSolution working solution}
 * and calculates the {@link Score} for it.
 * @param <Solution_> the solution type, the class with the {@link PlanningSolution} annotation
 */
public interface ScoreDirector<Solution_> {

    /**
     * The {@link PlanningSolution} that is used to calculate the {@link Score}.
     * <p>
     * Because a {@link Score} is best calculated incrementally (by deltas),
     * the {@link ScoreDirector} needs to be notified when its {@link PlanningSolution working solution} changes.
     * <p>
     * If the {@link PlanningSolution working solution} has been changed since {@link #calculateScore} was called,
     * its {@link Score} won't be correct.
     * @return never null
     */
    Solution_ getWorkingSolution();

    /**
     * The {@link PlanningSolution working solution} must never be the same instance as the
     * {@link PlanningSolution best solution}, it should be a (un)changed clone.
     * <p>
     * Only call this method on a separate {@link ScoreDirector} instance,
     * built by {@link Solver#getScoreDirectorFactory()},
     * not on the one used inside the {@link Solver} itself.
     * @param workingSolution never null
     */
    void setWorkingSolution(Solution_ workingSolution);

    /**
     * Calculates the {@link Score} and updates the {@link PlanningSolution working solution} accordingly.
     * @return never null, the {@link Score} of the {@link PlanningSolution working solution}
     */
    Score calculateScore();

    /**
     * @return true if {@link #getConstraintMatchTotals()} can be called
     */
    boolean isConstraintMatchEnabled();

    /**
     * Explains the {@link Score} of {@link #calculateScore()}.
     * @return never null
     * @throws IllegalStateException if {@link #isConstraintMatchEnabled()} returns false
     */
    Collection<ConstraintMatchTotal> getConstraintMatchTotals();

//    /**
//     * Returns an aggregation of all the inverses of {@link ConstraintMatch#getJustificationList()}.
//     * <p>
//     * TODO Experimental method because it's calculated from scratch, without delta's. It will probably be renamed at some point.
//     * @return never null, each key is an element from {@link ConstraintMatch#getJustificationList()} and the value is its {@link ConstraintMatch}
//     * @throws IllegalStateException if {@link #isConstraintMatchEnabled()} returns false
//     */
//    Map<Object, List<ConstraintMatch>> extractIndictmentMap();

    void beforeEntityAdded(Object entity);

    void afterEntityAdded(Object entity);

    void beforeVariableChanged(Object entity, String variableName);

    void afterVariableChanged(Object entity, String variableName);

    // TODO VariableDescriptor is not likely to go to public API

    void beforeVariableChanged(VariableDescriptor variableDescriptor, Object entity);

    void afterVariableChanged(VariableDescriptor variableDescriptor, Object entity);

    void changeVariableFacade(VariableDescriptor variableDescriptor, Object entity, Object newValue);

    void triggerVariableListeners();

    void beforeEntityRemoved(Object entity);

    void afterEntityRemoved(Object entity);

    // TODO extract this set of methods into a separate interface, only used by ProblemFactChange

    void beforeProblemFactAdded(Object problemFact);

    void afterProblemFactAdded(Object problemFact);

    void beforeProblemPropertyChanged(Object problemFactOrEntity);

    void afterProblemPropertyChanged(Object problemFactOrEntity);

    void beforeProblemFactRemoved(Object problemFact);

    void afterProblemFactRemoved(Object problemFact);

    /**
     * Translates an entity or fact instance (often from another {@link Thread} or JVM)
     * to this {@link ScoreDirector}'s internal working instance.
     * Useful during {@link Move} rebasing and in a {@link ProblemFactChange}.
     * <p>
     * Matching is determined by the {@link LookUpStrategyType} on {@link PlanningSolution}.
     * Matching uses a {@link PlanningId} by default.
     * @param externalObject sometimes null
     * @return null if externalObject is null or if there is no workingObject for externalObject
     * @throws IllegalArgumentException if it cannot be located or if the externalObject's class is not supported
     * @throws IllegalStateException if it cannot be located
     * @param <E> the object type
     */
    <E> E lookUpWorkingObject(E externalObject);

    /**
     * Needs to be called after use because some implementations needs to clean up their resources.
     */
    void dispose();

}
