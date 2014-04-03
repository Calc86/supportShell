package com.support;

import com.ui.console.ConsoleMessageShower;
import com.ui.console.EStyle;
import com.ui.console.Input;
import com.ui.console.MessageShower;

import java.io.IOException;

/**
 * Created by calc on 02.04.14.
 * support side start
 */
public class Support {
    public static final Support instance = new Support();

    private final Input input = new Input();
    private final MessageShower ms = new ConsoleMessageShower();

    private Server server;

    public Input getInput() {
        return input;
    }

    public MessageShower getMs() {
        return ms;
    }

    private Support() {
    }

    public static Support getInstance(){
        return instance;
    }


    private int getID(){
        try {
            return input.getInt();
        } catch (IOException e) {
            ms.setMessage("произошла ошибка получения ID").header(EStyle.AT);
            return 0;
        }
    }

    private void hello(){
        ms.setMessage(" > ").print();
    }

    void work(){
        server = new Server();
        ms.setMessage("    Support    ").header(EStyle.SHARP);

        while(true){
            hello();
            System.out.println("Введите номер подключения (-1 для выхода): ");
            int id = getID();
            if(id == -1) break;
            server.use(id);
        }

        ms.setMessage("Завершение работы").show();
    }

    public static void main(String[] args) {
        Support.getInstance().work();
    }
}
