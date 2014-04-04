package com.support;

import com.net.RowHttp;
import com.ui.console.EStyle;
import com.ui.console.Input;
import com.ui.console.MessageShower;

import java.io.IOException;

/**
 * Created by calc on 02.04.14.
 * Support side
 */
public class Server {
    public static final int WAIT = 500;
    public static final int CMD_TIMEOUT = 15 * 2;   //15 sec
    public static final int ACCEPT_WAIT = 10;   // 5 sec
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
        if(!row.load()) return false;
        if(row.getId() == 0) return false;
        if(row.isClosed()){
            ms.setMessage("подключение " + row.getId() + " закрыто");
            return false;
        }

        work();

        return true;
    }

    private void close(){
        row.setClosed(true);
        row.save();
    }

    private void hello(){
        ms.setMessage("(" + row.getId() + ") > ").print();
    }

    private void work(){
        ms.setMessage("Командный интерфейс").header(EStyle.AT);
        ms.setMessage("   Linux & Mac    |    Windows").header(EStyle.EQUAL);
        ms.setMessage("ifconfig          | ipconfig").show();
        ms.setMessage("ping -c4 www.ru   | ping -n 4 www.ru").show();
        ms.setMessage("traceroute www.ru | tracert -d www.ru").show();
        ms.setMessage("route             | route print").show();
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
            row.setAccepted(false);
            row.setCmdReturn("");
            row.save();

            boolean timeout;
            timeout = false;
            ms.setMessage("handshaking").print();
            for (int i = 0; i <= ACCEPT_WAIT; i++) {
                try {
                    Thread.sleep(WAIT);
                } catch (InterruptedException e) {
                    //
                }
                ms.setMessage(".").print();
                row.load();
                if(row.isAccepted())
                    break;
                timeout = i == ACCEPT_WAIT;
                if(timeout) break;
            }
            if(timeout){
                ms.setMessage("client not present").show();
                continue;
            }
            timeout = false;
            ms.setMessage("wait result").print();
            for (int i = 0; i <= CMD_TIMEOUT; i++) {
                try {
                    Thread.sleep(WAIT);
                } catch (InterruptedException e) {
                    //
                }
                row.load();

                if(row.isCmdDone())
                    break;
                timeout = i == CMD_TIMEOUT;
                if(timeout) break;
                ms.setMessage(".").print();
            }
            if(timeout){
                ms.setMessage("timeout").show();
                continue;
            }

            ms.setMessage("Результат: ").show();
            ms.setMessage(row.getCmdReturn()).header(EStyle.LINE);
        }
    }
}
