package com.dvipersquad.heybeach.auth.provider;

import com.dvipersquad.heybeach.BuildConfig;
import com.dvipersquad.heybeach.auth.User;
import com.dvipersquad.heybeach.util.AppExecutors;
import com.dvipersquad.heybeach.util.http.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CustomAuthProvider implements AuthProvider {

    private static final String REGISTER_USER_ENDPOINT = "user/register";
    private static final String LOGIN_USER_ENDPOINT = "user/login";
    private static final String GET_USER_ENDPOINT = "user/me";
    private static final String LOGOUT_USER_ENDPOINT = "user/logout";
    private static final String REGISTER_USER_METHOD = "POST";
    private static final String LOGIN_USER_METHOD = "POST";
    private static final String GET_USER_METHOD = "GET";
    private static final String LOGOUT_USER_METHOD = "DELETE";
    private static final String TAG = CustomAuthProvider.class.getSimpleName();
    private static final String ERROR_DEFAULT = "There was an unexpected error with your request. Try again later.";
    private static final String ERROR_DUPLICATED_EMAIL = "Email already in use.";
    private static final String ERROR_INVALID_EMAIL_PASSWORD = "Invalid email / password combination.";
    private AppExecutors appExecutors;

    private static CustomAuthProvider INSTANCE = null;

    private String currentToken = null;
    private User currentUser = null;

    private CustomAuthProvider(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public static CustomAuthProvider getInstance(AppExecutors appExecutors) {
        if (INSTANCE == null) {
            INSTANCE = new CustomAuthProvider(appExecutors);
        }
        return INSTANCE;
    }

    @Override
    public void registerUser(final String email, final String password, final LoadUserCallback callback) {
        sendUserData(REGISTER_USER_METHOD, BuildConfig.baseApiUrl + REGISTER_USER_ENDPOINT, null, email, password, callback);
    }

    @Override
    public void loginUser(final String email, final String password, LoadUserCallback callback) {
        sendUserData(LOGIN_USER_METHOD, BuildConfig.baseApiUrl + LOGIN_USER_ENDPOINT, null, email, password, callback);
    }

    @Override
    public void getUser(LoadUserCallback callback) {
        if (currentUser != null) {
            callback.onUserLoaded(currentUser, currentToken);
        } else {
            if (currentToken != null && !currentToken.isEmpty()) {
                sendUserData(GET_USER_METHOD, BuildConfig.baseApiUrl + GET_USER_ENDPOINT, currentToken, null, null, callback);
            } else {
                callback.onUserNotAvailable("User not logged in.");
            }
        }
    }

    @Override
    public void logoutUser() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                HttpHelper.generateConnection(
                        LOGOUT_USER_METHOD,
                        LOGOUT_USER_ENDPOINT,
                        currentToken,
                        null,
                        new HttpHelper.HttpRequestCallback() {
                            @Override
                            public void onRequestSuccessful(String response, String token) {
                                appExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentUser = null;
                                        currentToken = null;
                                    }
                                });
                            }

                            @Override
                            public void onRequestFailed(final String response) {
                                appExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentUser = null;
                                        currentToken = null;
                                    }
                                });
                            }
                        }
                );
            }
        };
        appExecutors.networkIO().execute(runnable);

    }

    private void sendUserData(final String method, final String url, final String token, final String email, final String password, final LoadUserCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                HttpHelper.generateConnection(
                        method,
                        url,
                        token,
                        new HashMap<String, String>() {{
                            put("email", email);
                            put("password", password);
                        }},
                        new HttpHelper.HttpRequestCallback() {
                            @Override
                            public void onRequestSuccessful(String response, final String token) {
                                final User user = deserializeJsonUserResponse(response);
                                appExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        currentToken = token;
                                        currentUser = user;
                                        callback.onUserLoaded(user, token);
                                    }
                                });
                            }

                            @Override
                            public void onRequestFailed(final String response) {
                                final String errorMessage = deserializeJsonErrorResponse(response);
                                appExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onUserNotAvailable(errorMessage);
                                    }
                                });
                            }
                        }
                );
            }
        };
        appExecutors.networkIO().execute(runnable);
    }

    private String deserializeJsonErrorResponse(String json) {
        String result = ERROR_DEFAULT;

        if (json != null && !json.isEmpty()) {
            try {
                JSONObject jsonError = new JSONObject(json);
                String message = "";
                Integer code = 0;
                if (jsonError.has("errors")) {
                    JSONObject errors = jsonError.getJSONObject("errors");
                    JSONObject firstError = errors.getJSONObject(errors.keys().next());
                    message = firstError.getString("message");
                } else if (jsonError.has("code")) {
                    message = jsonError.getString("errmsg");
                    code = jsonError.getInt("code");
                }

                switch (code) {
                    case 11000:
                        result = ERROR_DUPLICATED_EMAIL;
                        break;
                    default:
                        result = message;
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            result = ERROR_INVALID_EMAIL_PASSWORD;
        }
        return result;
    }

    private User deserializeJsonUserResponse(String json) {
        User result = null;
        if (json != null && !json.isEmpty()) {
            try {
                JSONObject jsonUser = new JSONObject(json);
                result = new User(
                        jsonUser.getString("_id"),
                        jsonUser.getString("email"),
                        null
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
