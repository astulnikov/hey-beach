package com.dvipersquad.heybeach.util.http;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpHelper {

    private static final String TAG = HttpHelper.class.getSimpleName();

    public static void generateConnection(String method, String url, String token, Map<String, String> body, HttpRequestCallback callback) {
        Log.d(TAG, method + " request to:" + url);
        HttpURLConnection connection = null;
        try {
            URL urlObject = new URL(url);
            connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestMethod(method);
            connection.setDoInput(true);

            if (body != null && !body.isEmpty()) {
                connection.setDoOutput(true);
            }

            connection.setUseCaches(false);
            if (token != null && !token.isEmpty()) {
                connection.setRequestProperty("x-auth", token);
                Log.d(TAG, "Token:" + token);
            }
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            if (body != null && !body.isEmpty()) {
                JSONObject jsonBody = new JSONObject(body);
                DataOutputStream printout;
                printout = new DataOutputStream(connection.getOutputStream());
                String finalBody = jsonBody.toString();
                printout.writeBytes(finalBody);
                printout.flush();
                printout.close();
                Log.d(TAG, "Body:" + finalBody);
            }

            Log.d(TAG, "Response code:" + connection.getResponseCode());
            Log.d(TAG, "Response message:" + connection.getResponseMessage());
            Log.d(TAG, "Response token:" + connection.getHeaderField("x-auth"));

            InputStream stream;
            if (connection.getResponseCode() >= 400) {
                stream = connection.getErrorStream();
            } else {
                stream = connection.getInputStream();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            Log.d(TAG, "Response content:" + buffer.toString());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                callback.onRequestSuccessful(buffer.toString(), connection.getHeaderField("x-auth"));
            } else {
                callback.onRequestFailed(buffer.toString());
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            callback.onRequestFailed(e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
            callback.onRequestFailed(e.getLocalizedMessage());

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public interface HttpRequestCallback {

        void onRequestSuccessful(String response, String token);

        void onRequestFailed(String response);
    }

}
