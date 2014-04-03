package com.ui.console;

/**
 * Created by calc on 02.04.14.
 *
 */
public enum EStyle {

    NONE(""),
    SHARP("#"),
    AT("@"),
    EQUAL("="),
    LINE("-");

    private final String styleString;

    EStyle(String styleString) {
        this.styleString = styleString;
    }

    public String getStyleString() {
        return styleString;
    }
}
