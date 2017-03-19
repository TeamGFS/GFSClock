package com.github.gfsclock.gfstimeclock;

import android.preference.PreferenceManager;
import android.text.TextUtils;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServiceGenerator {
    private static String apiBaseURL = PreferenceManager.getDefaultSharedPreferences(Startup.getContext()).
            getString("serverAddress", "https://sitwebclock.gfs.com/");
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()).baseUrl(apiBaseURL);
    private static Retrofit retrofit = builder.build();
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static void changeApiBaseURL(String newURL) {
        apiBaseURL = newURL;
        builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()).baseUrl(apiBaseURL);
    }

    public static <S> S createService(Class<S> serviceClass) {
            return createService(serviceClass, null, null);
            //return retrofit.create(serviceClass);
    }
    public static <S> S createService(Class<S> serviceClass, String username, String password) {
        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }
        return createService(serviceClass, null, null);
    }
    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if(!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);
            if(!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }

}
