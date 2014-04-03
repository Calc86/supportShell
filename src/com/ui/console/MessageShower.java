package com.ui.console;

import com.ui.IMessageShower;

/**
 * Created by calc on 02.04.14.
 *
 */
public abstract class MessageShower implements IMessageShower {
    private String message;

    protected MessageShower() {
    }

    public String getMessage() {
        return message;
    }

    public MessageShower setMessage(String message) {
        this.message = message;
        return this;
    }

    public abstract void show();
    public abstract void header(EStyle style);
    public abstract void print();
}
