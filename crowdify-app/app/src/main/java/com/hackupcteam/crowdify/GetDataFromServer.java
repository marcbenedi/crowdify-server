package com.hackupcteam.crowdify;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hermes on 08/10/2016.
 */
public class GetDataFromServer extends AsyncTask<String,Void,String> {

    private String resultConnection = new String ("Valor inicial");

    @Override
    protected String doInBackground(String... strings) {
        //ERROR NetworkOnMainThreadException
        //Possible solució: Crear un nou thread
        final String url = strings[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try{
            URL url3 = new URL(url);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url3.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            //Guardem el resultat
            resultConnection = buffer.toString();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultConnection;
        //String with all the JSON response from the API
    }
}
