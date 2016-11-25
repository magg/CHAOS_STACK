package com.inria.spirals.mgonzale.domain;

public enum Trigger {

    /**
     * The event was started manually, i.e. from outside Chaos Lemur
     */
    MANUAL,

    /**
     * The event was started according to Chaos Lemur's schedule
     */
    SCHEDULED

}
