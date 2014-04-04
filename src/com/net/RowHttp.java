package com.net;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by calc on 03.04.14.
 *
 */
public class RowHttp extends Row{
    private final String baseUrl = "http://clsupport.xsrv.su/supportShell/cmd_post.php?l=1";

    public boolean load() {
        return getRow();
    }

    public boolean save() {
        return setRow();
    }

    private URL createURL(){
        String url = baseUrl + "&id=" + getId() + "&rand=" + (int)(Math.random() * 10000);

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private URLConnection createUrlConnection(URL url, boolean isPost){
        URLConnection connection;
        try {
            connection = url.openConnection();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        if(isPost){
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        }
        return connection;
    }

    private boolean getRow(){
        URL url = createURL();
        if(url == null) return false;

        URLConnection connection = createUrlConnection(url, false);
        if(connection == null) return false;

        String response = getResponse(connection);
        if(response == null) return false;
        ByteArrayInputStream stream;
        try {
            stream = new ByteArrayInputStream(response.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Кодировка UTF-8 не поддерживается");
            stream = new ByteArrayInputStream(response.getBytes());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;
        try {
            //1
            line = reader.readLine();
            if(line.compareTo("error") == 0) return false;
            if(getId() == 0) setId(Integer.parseInt(line));  //устанавливаем только в том случае, если ID = 0
            setClosed(Boolean.parseBoolean(reader.readLine())); //2
            setAccepted(Boolean.parseBoolean(reader.readLine()));
            setCmdDone(Boolean.parseBoolean(reader.readLine()));    //3
            setCmd(reader.readLine());  //4
            String result = "";
            while((line = reader.readLine()) != null){
                result += line + "\n";
            }
            setCmdReturn(result);  //5
        } catch (IOException e) {
            System.out.println("Проблема парсинга ответа: " + e.getMessage());
            return false;
            //throw new NetException("error parse Row" + e.getMessage());
        }
        return true;
    }

    private boolean setRow(){
        URL url = createURL();
        if(url == null) return false;

        URLConnection connection = createUrlConnection(url, true);
        if(connection == null) return false;
        OutputStream output = null;
        try {
            output = connection.getOutputStream();
            output.write(toString().getBytes("UTF-8"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
            //throw new NetException(e.getMessage());
        } finally {
            try {
                if(output != null)
                    output.close();
            } catch (IOException e) {
                //ignore
            }
        }
        String response = getResponse(connection);
        return response != null && response.compareTo("OK") == 0;
    }

    private String getResponse(URLConnection connection){
        String response = "";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
        } catch (IOException e) {
            //throw new NetException(e.getMessage());
            System.out.println(e.getMessage());
            return null;
        }

        try {
            String line;
            while(( line = reader.readLine()) != null)
                response = response + line + "\n";
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                //ignore
            }
        }
        return response;
    }


    @Override
    public String toString() {
        try {
            return /*"id=" + id +*/
                    "closed=" + isClosed() +
                            "&cmd_accepted=" + isAccepted() +
                            "&cmd_done=" + isCmdDone() +
                            "&cmd=" + URLEncoder.encode(getCmd(), "UTF-8") +
                            "&cmd_ret=" + URLEncoder.encode(getCmdReturn(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return /*"id=" + id +*/
                    "closed=" + isClosed() +
                            "&cmd_accepted=" + isAccepted() +
                            "&cmd_done=" + isCmdDone() +
                            "&cmd=" + getCmd() +
                            "&cmd_ret=" + getCmdReturn();
        }
    }
}
