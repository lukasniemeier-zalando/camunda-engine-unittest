package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cancelled implements JavaDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(Cancelled.class);

    public void execute(final DelegateExecution delegateExecution) throws Exception {
        LOG.info(delegateExecution.getVariable("itemId").toString());
    }
}
