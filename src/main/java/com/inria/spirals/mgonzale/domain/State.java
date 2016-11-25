package com.inria.spirals.mgonzale.domain;

/**
 * Valid states for Chaos Lemur <ul> <li>{@link #STARTED}</li> <li>{@link #STOPPED}</li> </ul>
 */
public enum State {

    /**
     * Chaos Lemur is active
     */
    STARTED,

    /**
     * Chaos Lemur is paused
     */
    STOPPED

}