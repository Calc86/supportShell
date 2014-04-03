package com.net;

import com.ui.IMessageShower;
import com.ui.console.ConsoleMessageShower;
import com.ui.console.EStyle;
import com.ui.console.MessageShower;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by calc on 02.04.14.
 *
 */
public class Client {
    private final Http http = new Http();
    private int id;
    private Row row;
    private final IMessageShower ms;

    public Client(IMessageShower ms) {
        this.ms = ms;
    }

    public void create(){
        row = http.getRow(0);
        id = row.getId();
    }

    public String shellExec(String command){
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            return "Невозможно запустить процесс: " + command;
        } catch (IllegalArgumentException e){
            return "пустая команда";
        }

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            //
        }

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(p.getInputStream()));

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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            row = http.getRow(id);
            if(row == null) System.out.print(".");
            else{
                if(row.isClosed()) break;
                if(!row.isCmdDone()){
                    ms.setMessage("Выполняем команду:").show();
                    ms.setMessage(row.getCmd()).show();
                    row.setCmdReturn(shellExec(row.getCmd()));
                    ms.setMessage("Результат:").show();
                    ms.setMessage(row.getCmdReturn()).show();
                    row.setCmdDone(true);
                    http.setRow(row);
                }
            }
        }
    }
}
