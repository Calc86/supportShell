package com.ui.gui;

import com.ui.IMessageShower;
import com.ui.console.EStyle;

import javax.swing.*;

/**
 * Created by calc on 03.04.14.
 *
 */
public class UserForm implements IMessageShower{
    private String message;
    private JTextPane textLog;

    public JPanel getPanel() {
        return panel;
    }

    private JPanel panel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("UserForm");
        frame.setContentPane(new UserForm().panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public IMessageShower setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public void show() {
        textLog.setText(message);
    }

    @Override
    public void header(EStyle style) {
        textLog.setText(message);
    }

    @Override
    public void print() {
        textLog.setText(message);
    }
}
