package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class FailingJob implements JavaDelegate {

    public void execute(final DelegateExecution delegateExecution) throws Exception {
        throw new RuntimeException("expected");
    }

}
