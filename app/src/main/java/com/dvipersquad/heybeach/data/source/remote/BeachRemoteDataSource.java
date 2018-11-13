package com.dvipersquad.heybeach.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.dvipersquad.heybeach.BuildConfig;
import com.dvipersquad.heybeach.data.Beach;
import com.dvipersquad.heybeach.data.source.BeachDataSource;
import com.dvipersquad.heybeach.util.AppExecutors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BeachRemoteDataSource implements BeachDataSource {

    private final static String TAG = BeachRemoteDataSource.class.getSimpleName();
    private final static String GET_BEACHES_METHOD = "GET";
    private final static String GET_BEACHES_ENDPOINT = "beaches?";
    private final static String GET_BEACHES_PAGE_PARAMETER = "page=";
    private static final int HTTP_OK = 200;
    private static final int DEFAULT_PAGE = 0;
    private static BeachRemoteDataSource INSTANCE;

    private AppExecutors appExecutors;

    public static BeachRemoteDataSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (BeachRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BeachRemoteDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    // Prevent remote instantiation
    private BeachRemoteDataSource(@NonNull AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    @Override
    public void getBeaches(final LoadBeachesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Beach> beaches = getBeachesFromAPI(DEFAULT_PAGE);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (beaches == null || beaches.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onBeachesLoaded(beaches);
                        }
                    }
                });
            }
        };

        appExecutors.networkIO().execute(runnable);
    }

    @Override
    public void getBeachesNextPage(final int page, final LoadBeachesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Beach> beaches = getBeachesFromAPI(page);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (beaches == null || beaches.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onBeachesLoaded(beaches);
                        }
                    }
                });
            }
        };

        appExecutors.networkIO().execute(runnable);
    }

    private List<Beach> getBeachesFromAPI(int page) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            String finalUrl = BuildConfig.baseApiUrl + GET_BEACHES_ENDPOINT + GET_BEACHES_PAGE_PARAMETER + page;
            Log.d(TAG, "Request Url:" + finalUrl);
            URL url = new URL(finalUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET_BEACHES_METHOD);
            connection.connect();

            // to log the response code of your request
            int responseCode = connection.getResponseCode();
            Log.d(TAG, "response code: " + responseCode);
            // to log the response message from your server after you have tried the request.
            String responseMessage = connection.getResponseMessage();
            Log.d(TAG, "response message: " + responseMessage);

            if (responseCode == HTTP_OK) {
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                Log.d(TAG, "response content: " + buffer.toString());
                return deserializeJsonBeachesResponse(buffer.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<Beach> deserializeJsonBeachesResponse(String json) {
        List<Beach> result = null;
        if (json != null && !json.isEmpty()) {
            try {
                result = new ArrayList<>();
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonImage = array.getJSONObject(i);
                    Beach beach = new Beach(
                            jsonImage.getString("_id"),
                            jsonImage.getString("name"),
                            jsonImage.getString("url"),
                            jsonImage.getInt("width"),
                            jsonImage.getInt("height")
                    );
                    result.add(beach);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
