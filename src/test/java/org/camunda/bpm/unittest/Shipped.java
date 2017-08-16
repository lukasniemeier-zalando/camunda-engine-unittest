package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shipped implements JavaDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(Shipped.class);

    public void execute(final DelegateExecution delegateExecution) throws Exception {
        LOG.info(delegateExecution.getVariable("itemId").toString());
    }
}
