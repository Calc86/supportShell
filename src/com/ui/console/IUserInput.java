package com.ui.console;

import java.io.IOException;

/**
 * Created by calc on 02.04.14.
 *
 */
public interface IUserInput {
    public int getInt() throws IOException;
    public String getString() throws IOException;
}
