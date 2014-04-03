package com.ui.console;

import java.io.*;

/**
 * Created by calc on 02.04.14.
 * console input
 */
public class Input implements IUserInput {
    BufferedReader bufferRead;

    public Input() {
        bufferRead = new BufferedReader(new InputStreamReader(System.in));
    }

    private String getLine() throws IOException {
        return bufferRead.readLine();
    }

    @Override
    public int getInt() throws IOException {
        try {
            return Integer.parseInt(getLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String getString() throws IOException {
        return getLine();
    }
}
