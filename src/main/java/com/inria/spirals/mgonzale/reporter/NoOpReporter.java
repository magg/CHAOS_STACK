package com.inria.spirals.mgonzale.reporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class NoOpReporter implements Reporter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void sendEvent(Event event) {
        this.logger.info("Chaos Stack Destruction ({})", event.getIdentifier());

        event.getMembers().stream()
            .sorted()
            .forEach(member -> this.logger.info("  • {}", member.getName()));
    }

}
