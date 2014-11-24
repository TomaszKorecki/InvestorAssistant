/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;

import investor.data.DataRange;
import investor.data.Index;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.*;

/**
 *
 * @author Tomasz
 */
public class NetworkManager {

    public static Gson converter = new Gson();

    public static String serverUrl = "https://zpi.herokuapp.com/show/";

    private static String buildUrl(DataType dataType) {
        String url = serverUrl;
        switch (dataType) {
            case FOREX:
                url += "forex";
                break;
            case SPOL:
                url += "spol";
                break;
            case WSK:
                url += "wsk";
                break;
            case TWR:
                url += "twr";
                break;
        }

        return url;
    }
    
    private static String buildUrl(String dataType, DataRange range){
        String url = serverUrl + dataType + "/";
        
        switch(range){
            case ONEDAY:
                url = "https://zpi.herokuapp.com/today/"+dataType;
                break;
            case FIVEDAYS:
                url += "5D";
                break;
            case TENDAYS:
                url += "10D";
                break;
            case ONEMONTH:
                url += "1M";
                break;
            case THREEMONTH:
                url += "3M";
                break;
        }
        
        return url;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    // Pobranie z serwera wszystkich indeksów, spółek, walut lub towarów
    public static Index[] show(DataType type) throws IOException, JSONException {
        return downloadFromServer(buildUrl(type));
    }
    
    
    //Pobranie z serwera danych dla danego instrumentu w odpowiednim zakresie
    public static Index[] showMore(String what, DataRange range) throws IOException, JSONException {
        return downloadFromServer(buildUrl(what.toLowerCase(), range));
    }
    
   

    private static Index[] downloadFromServer(String url) throws IOException, JSONException {
        url = url.replaceAll("\\^", "%5E");
        System.out.println("Connecting to url \n" + url);
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);

            System.out.println(jsonText);
            //Type listType = new TypeToken<T[]>() {}.getType();
            return converter.fromJson(jsonText, Index[].class);
        } finally {
            is.close();
        }
    }
}
