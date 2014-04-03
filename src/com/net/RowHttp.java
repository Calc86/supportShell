package com.net;

import com.ui.console.Environment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by calc on 03.04.14.
 *
 */
public class RowHttp extends Row{
    private final String baseUrl = "http://clsupport.xsrv.su/supportShell/cmd.php?l=1";

    public void load() {
        getRow();
    }

    public void save() {
        setRow();
    }

    private URL createURL(boolean isSet){
        String url = baseUrl;
        if(isSet){
            url = url + toString()  + "&do=1";
        }
        url = url + "&id=" + getId() + "&rand=" + (int)(Math.random() * 10000);

        //System.out.println(url);

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new NetException("MalformedURLException" + e.getMessage());
        }
    }

    private void getRow(){
        URL url = createURL(false);

        URLConnection connection;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            throw new NetException("error openConnection: " + e.getMessage());
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"));   //читаем в UTF-8
        } catch (IOException e) {
            throw new NetException("error getInputStream" + e.getMessage());
        }

        String line;
        try {
            //1
            line = reader.readLine();
            if(line.compareTo("error") == 0) return;
            if(getId() == 0) setId(Integer.parseInt(line)); //устанавливаем только в том случае, если ID = 0
            setClosed(Boolean.parseBoolean(reader.readLine())); //2
            setCmdDone(Boolean.parseBoolean(reader.readLine()));    //3
            setCmd(reader.readLine());  //4
            String result = "";
            while((line = reader.readLine()) != null){
                result += line + "\n";
            }
            setCmdReturn(result);  //5
        } catch (IOException e) {
            throw new NetException("error parse Row" + e.getMessage());
        }
    }

    private void setRow(){
        URL url = createURL(true);
        try {
            url.getContent();
        } catch (IOException e) {
            //а хз что делать
        }
    }

    @Override
    public String toString() {
        try {
            return /*"id=" + id +*/
                    "&closed=" + isClosed() +
                            "&cmd_done=" + isCmdDone() +
                            "&cmd=" + URLEncoder.encode(getCmd(), "UTF-8") +
                            "&cmd_ret=" + URLEncoder.encode(getCmdReturn(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new NetException(e.getMessage());
        }
    }
}
