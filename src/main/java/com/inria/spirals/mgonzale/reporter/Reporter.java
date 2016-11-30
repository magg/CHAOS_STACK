package com.inria.spirals.mgonzale.reporter;

/**
 * An abstraction for interfacing with Reporters in different ways
 */
public interface Reporter {

    /**
     * Sends an Event to an Event Recorder
     *
     * @param event The {@link Event} to report
     */
    void sendEvent(Event event);

}
