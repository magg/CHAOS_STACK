package com.inria.spirals.mgonzale.config;

import com.inria.spirals.mgonzale.controllers.AbstractRestControllerStateProvider;
import com.inria.spirals.mgonzale.domain.State;

final class SimpleStateProvider extends AbstractRestControllerStateProvider {

    private final Object monitor = new Object();

    private volatile State state = State.STARTED;

    @Override
    public State get() {
        synchronized (this.monitor) {
            return this.state;
        }
    }

    @Override
    protected void set(State state) {
        synchronized (this.monitor) {
            this.state = state;
        }
    }

}