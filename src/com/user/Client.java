package com.user;

import com.net.RowHttp;
import com.ui.IMessageShower;
import com.ui.console.EStyle;
import com.ui.console.Environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by calc on 02.04.14.
 *
 */
public class Client {
    public static final int WAIT = 500;
    public static final int SHELL_TIMEOUT = 15 * 2;
    private int id;
    private RowHttp row = new RowHttp();
    private final IMessageShower ms;

    public Client(IMessageShower ms) {
        this.ms = ms;
    }

    public void create(){
        row.load();
        id = row.getId();
    }

    public String shellExec(String command){
        if(command == null) return "Нулевая команда";
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            return "Невозможно запустить процесс: " + command;
        } catch (IllegalArgumentException e){
            return "пустая команда";
        }

        /*try {*/
        boolean timeout = false;
            for (int i = 0; i <= SHELL_TIMEOUT; i++) {
                try {
                    Thread.sleep(WAIT);
                    System.out.println(p.exitValue());
                    break;
                } catch (IllegalThreadStateException e) {
                    System.out.print(".");
                    if(i == SHELL_TIMEOUT) timeout = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        p.destroy();
        if(timeout)
            return "timeout";
            //p.waitFor();
        /*} catch (InterruptedException e) {
            //
        }*/

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Environment.getEncoding()));
        } catch (UnsupportedEncodingException e) {
            ms.setMessage("encoding " + Environment.getEncoding() + " not supported");
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        }

        String line = "";
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = reader.readLine())!= null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            ms.setMessage("Ошибка получения вывода от команды: " + command).show();
            return "";
        }

        return sb.toString();
    }

    public void work(){
        create();
        ms.setMessage("Ваш ID " + id).header(EStyle.SHARP);

        while(true){
            try {
                Thread.sleep(WAIT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            row.load();

            if(row == null) System.out.print(".");
            else{
                if(row.isClosed()) break;
                if(!row.isCmdDone()){
                    row.setAccepted(true);
                    row.save();
                    ms.setMessage("Выполняем команду:").show();
                    ms.setMessage(row.getCmd()).show();
                    row.setCmdReturn(shellExec(row.getCmd()));
                    ms.setMessage("Результат:").show();
                    ms.setMessage(row.getCmdReturn()).show();
                    row.setCmdDone(true);
                    row.save();
                }
            }
        }
    }
}
