/* Licensed under the Apache License, Version 2.0 (the "License");
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
package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.runtime.Incident;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.runtimeService;

/**
 * @author Daniel Meyer
 * @author Martin Schimak
 */
public class SimpleTestCase {

    @Rule
    public ProcessEngineRule rule = new ProcessEngineRule();

    @Test
    @Deployment(resources = {"testProcess.bpmn"})
    public void logsExceptionThreeTimes() throws Exception {
        runtimeService().startProcessInstanceByKey("testProcess");

        final List<Incident> incidents = supplyAsync(pollIncidents()).get(15, TimeUnit.SECONDS);
        assertThat(incidents.size()).isEqualTo(1);
        assertThat(incidents.get(0).getIncidentMessage()).contains("expected");
    }

    private Supplier<List<Incident>> pollIncidents() {
        return () -> {
            while (runtimeService().createIncidentQuery().count() < 1L) {
                // no operation
            }
            return runtimeService().createIncidentQuery().list();
        };
    }

}
