package com.support;

import com.net.RowHttp;
import com.support.Support;
import com.ui.console.EStyle;
import com.ui.console.Input;
import com.ui.console.MessageShower;

import java.io.IOException;

/**
 * Created by calc on 02.04.14.
 * Support side
 */
public class Server {
    private String command = "";
    private final MessageShower ms;
    private final Input input;
    private RowHttp row = new RowHttp();

    public Server() {
        ms = Support.getInstance().getMs();
        input = Support.getInstance().getInput();
    }

    public boolean use(int id){
        if(id == 0) return false;

        row.setId(id);
        row.load();
        if(row.getId() == 0) return false;

        work();

        return false;
    }

    private void close(){
        row.setClosed(true);
        row.save();
    }

    private void hello(){
        ms.setMessage("(" + row.getId() + ") > ").print();
    }

    private void work(){
        ms.setMessage("Командный интерфейс").header(EStyle.EQUAL);
        while(true){
            hello();
            try {
                command = input.getString();
            } catch (IOException e) {
                command = "exit";
                ms.setMessage("Ошибка в получении команды").header(EStyle.AT);
            }
            if(command.compareTo("exit") == 0) break;
            if(command.compareTo("close") == 0){
                close();
                break;
            }

            ms.setMessage("Выполняем: " + command).show();
            row.setCmd(command);
            row.setCmdDone(false);
            row.save();

            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //
                }
                row.load();
                if(row.isCmdDone())
                    break;
            }

            ms.setMessage("Результат: ").show();
            ms.setMessage(row.getCmdReturn()).header(EStyle.LINE);
        }
    }
}
