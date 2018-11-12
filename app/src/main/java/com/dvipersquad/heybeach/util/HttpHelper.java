package com.dvipersquad.heybeach.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            if (token != null && !token.isEmpty()) {
                connection.setRequestProperty("x-auth", token);
                Log.d(TAG, "Token:" + token);
            }
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            if (body != null && !body.isEmpty()) {
                JSONObject jsonBody = new JSONObject(body);
                DataOutputStream printout;
                printout = new DataOutputStream(connection.getOutputStream());
                printout.writeBytes(URLEncoder.encode(jsonBody.toString(), "UTF-8"));
                printout.flush();
                printout.close();
                Log.d(TAG, "Body:" + jsonBody.toString());
            }

            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            Log.d(TAG, "Response code:" + connection.getResponseCode());
            Log.d(TAG, "Response message:" + connection.getResponseMessage());
            Log.d(TAG, "Response content:" + buffer.toString());
            Log.d(TAG, "Response token:" + connection.getHeaderField("x-auth"));
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                callback.onRequestSuccessful(buffer.toString(), connection.getHeaderField("x-auth"));
            } else {
                callback.onRequestFailed(deserializeJsonErrorResponse(buffer.toString()));
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String deserializeJsonErrorResponse(String json) {
        String result = null;
        if (json != null && !json.isEmpty()) {
            try {
                JSONObject jsonError = new JSONObject(json);
                result = jsonError.getString("errmsg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public interface HttpRequestCallback {

        void onRequestSuccessful(String response, String token);

        void onRequestFailed(String error);
    }

}
