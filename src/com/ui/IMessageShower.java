package com.ui;

import com.ui.console.EStyle;

import java.nio.channels.Pipe;

/**
 * Created by calc on 03.04.14.
 */
public interface IMessageShower {
    public IMessageShower setMessage(String message);
    public void show();
    public void header(EStyle style);
    public void print();
}
