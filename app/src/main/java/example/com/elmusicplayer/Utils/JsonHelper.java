package example.com.elmusicplayer.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;


public final class JsonHelper {
    private JsonHelper() {
    }

    public static String getJsonResponse(String urlString){
        String jsonResponse = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode() == HttpsURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
            }

            if(inputStream != null){
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\a");
                jsonResponse = scanner.next();
            }

            if(inputStream != null){
                inputStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }

        return  jsonResponse;
    }
}
