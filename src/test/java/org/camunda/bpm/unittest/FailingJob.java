package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.LoggerFactory;

public class FailingJob implements JavaDelegate {

    public static int ExecutionCount = 0;

    public void execute(final DelegateExecution delegateExecution) throws Exception {
        ExecutionCount++;
        LoggerFactory.getLogger(FailingJob.class).info("Executed [{}] times.", ExecutionCount);
        throw new RuntimeException("expected");
    }

}
