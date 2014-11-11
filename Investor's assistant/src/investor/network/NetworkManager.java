/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package investor.network;

import investor.data.Currency;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import static javafx.application.Application.launch;
import javax.xml.bind.JAXBContext;


/**
 *
 * @author Tomasz
 */
public class NetworkManager {

   
    
    public static void sendGet() throws Exception {
        String uri = "http://localhost:8080/CustomerService/rest/customers/1";
        URL url = new URL(uri);
        HttpURLConnection connection
                = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        JAXBContext jc = JAXBContext.newInstance(Currency.class);
        InputStream xml = connection.getInputStream();
        Currency customer = (Currency) jc.createUnmarshaller().unmarshal(xml);

        connection.disconnect();
    }
}
