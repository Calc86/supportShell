package com.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by calc on 03.04.14.
 */
public class RowHttp extends Row{
    private final String baseUrl = "http://clsupport.xsrv.su/supportShell/cmd.php?";
    @Override
    public void load() {
        super.load();
    }

    @Override
    public void save() {
        super.save();
    }

    private URL createURL(String url){
        url = url + "&rand=" + (int)(Math.random() * 10000);
        //System.out.println(url);
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new NetException("MalformedURLException" + e.getMessage());
        }
    }

    private Row getRow(int id){
        URL url = createURL(baseUrl + "id=" + id);

        URLConnection connection;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            throw new NetException("error openConnection: " + e.getMessage());
        }
        BufferedReader reader;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            throw new NetException("error getInputStream" + e.getMessage());
        }

        Row row = new Row();
        String line;
        try {
            //1
            line = reader.readLine();
            if(line.compareTo("error") == 0)
                return null;
            row.setId(Integer.parseInt(line));
            row.setClosed(Boolean.parseBoolean(reader.readLine())); //2
            row.setCmdDone(Boolean.parseBoolean(reader.readLine()));    //3
            row.setCmd(reader.readLine());  //4
            String result = "";
            while((line = reader.readLine()) != null){
                result += line + "\n";
            }
            row.setCmdReturn(result);  //5
        } catch (IOException e) {
            throw new NetException("error parse Row" + e.getMessage());
        }

        return row;
    }

    private void setRow(Row row){
        URL url = createURL(baseUrl + row + "&do=1");
        try {
            url.getContent();
        } catch (IOException e) {
            //а хз что делать
        }
    }
}
