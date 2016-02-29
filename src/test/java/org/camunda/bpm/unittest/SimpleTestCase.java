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
import org.camunda.bpm.engine.task.Task;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;

import java.util.*;

import org.junit.Rule;
import org.junit.Test;

/**
 * @author Daniel Meyer
 * @author Martin Schimak
 */
public class SimpleTestCase {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  final String taskTop = "UserTask_1iren47";
  final String taskBottom = "UserTask_0ze0ci5";

  @Test
  @Deployment(resources = {"testProcess.bpmn"})
  public void onlyTop() {
    ProcessInstance processInstance = setUp(true, false);

    // Complete Task 1
    complete(task(processInstance));

    // Expecting Task Top but no Task Bottom
    assertThat(task(taskTop, processInstance)).isNotNull();
    assertThat(task(taskBottom, processInstance)).isNull();

    // Complete Task Top
    complete(task(taskTop, processInstance));

    // Then the process instance should be ended
    assertThat(processInstance).isEnded();
  }

  @Test
  @Deployment(resources = {"testProcess.bpmn"})
  public void onlyBottom() {
    ProcessInstance processInstance = setUp(false, true);

    // Complete Task 1
    complete(task(processInstance));

    // Expecting Task Bottom but no Task Top
    assertThat(task(taskTop, processInstance)).isNull();
    assertThat(task(taskBottom, processInstance)).isNotNull();

    // Complete Task
    complete(task(taskBottom, processInstance));

    // Then the process instance should be ended
    assertThat(processInstance).isEnded();
  }

  @Test
  @Deployment(resources = {"testProcess.bpmn"})
  public void both() {
    ProcessInstance processInstance = setUp(true, true);

    // Complete Task 1
    complete(task(processInstance));

    // Expecting Task Bottom but no Task Top
    assertThat(task(taskTop, processInstance)).isNotNull();
    assertThat(task(taskBottom, processInstance)).isNotNull();

    // Complete Tasks
    complete(task(taskTop, processInstance));
    assertThat(processInstance).isNotEnded();
    complete(task(taskBottom, processInstance));

    // Then the process instance should be ended
    assertThat(processInstance).isEnded();
  }

  @Test(expected = org.camunda.bpm.engine.ProcessEngineException.class)
  @Deployment(resources = {"testProcess.bpmn"})
  public void none() {
    ProcessInstance processInstance = setUp(false, false);

    // Complete Task 1
    complete(task(processInstance));
  }

  private ProcessInstance setUp(final boolean shouldGoTop, final boolean shouldGoBottom) {
    final Map<String, Object> variableMap = new HashMap<String, Object>();
    variableMap.put("shouldGoTop", shouldGoTop);
    variableMap.put("shouldGoBottom", shouldGoBottom);

    return runtimeService().startProcessInstanceByKey("Process_1", variableMap);
  }

}
