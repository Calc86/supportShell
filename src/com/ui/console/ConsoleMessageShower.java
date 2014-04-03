package com.ui.console;

public class ConsoleMessageShower extends MessageShower{
    public ConsoleMessageShower() {
        //Environment e = new Environment();
        //e.setEncoding();
    }

    @Override
    public void show() {
        System.out.println(getMessage());
    }

    private void showStyledLine(EStyle style, int length){
        for (int i = 0; i < length; i++) {
            System.out.print(style.getStyleString());
        }
        System.out.println();
    }

    @Override
    public void header(EStyle style) {
        showStyledLine(style,40);
        show();
        showStyledLine(style,40);
    }

    @Override
    public void print() {
        System.out.print(getMessage());
    }
}
