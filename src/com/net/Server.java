package com.net;

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
    private int id = 0;
    private String command = "";
    private final MessageShower ms;
    private final Input input;
    private final Http http = new Http();
    private Row row = new Row();

    public Server() {
        ms = Support.getInstance().getMs();
        input = Support.getInstance().getInput();
    }

    public boolean use(int id){
        if(id == 0) return false;

        this.id = id;
        row = http.getRow(id);
        if(row == null) return false;

        work();

        return false;
    }

    private void close(){
        row.setClosed(true);
        http.setRow(row);
    }

    private void hello(){
        ms.setMessage("(" + id + ") > ").print();
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
            http.setRow(row);

            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //
                }
                Row newRow = http.getRow(row.getId());
                if(newRow != null){
                    row = newRow;
                    if(row.isCmdDone())
                        break;
                }
            }

            ms.setMessage("Результат: ").show();
            ms.setMessage(row.getCmdReturn()).header(EStyle.LINE);
        }
    }
}
