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

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.managementService;
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
        managementService().suspendJobByProcessDefinitionKey("testProcess");
        final List<ProcessInstance> instances = range(0, 50)
                .mapToObj($ -> runtimeService().startProcessInstanceByKey("testProcess"))
                .collect(toList());

        managementService().activateJobByProcessDefinitionKey("testProcess");
        runAsync(pollInstanceFinished()).get(20, TimeUnit.SECONDS);

        instances.forEach(instance -> assertThat(instance).isEnded());
    }

    private Runnable pollInstanceFinished() {
        return () -> {
            while (runtimeService().createProcessInstanceQuery().active().count() > 0L) {
                // no operation
            }
        };
    }

}
