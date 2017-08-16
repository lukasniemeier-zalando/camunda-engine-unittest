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

import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.util.Lists.newArrayList;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.jobQuery;
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
    public void shouldExecuteProcess() {
        final Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("itemIds", newArrayList("1", "2", "3"));
        final ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("testProcess", variables);

        execute();

        final MessageCorrelationResult result = runtimeService().createMessageCorrelation("message-item-shipped")
                .processInstanceId(processInstance.getId())
                .localVariableEquals("itemId", "1")
                .correlateWithResult();

        assertThat(processInstance).isEnded();
    }

    private void execute() {
        List<Job> jobs;
        jobQuery().active().list();
        while (!(jobs = jobQuery().active().list()).isEmpty()) {
            for (Job job : jobs) {
                ProcessEngineTests.execute(job);
            }
        }
    }

}
