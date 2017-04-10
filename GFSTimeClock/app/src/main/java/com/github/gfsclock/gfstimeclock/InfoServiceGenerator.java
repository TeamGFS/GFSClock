package com.github.gfsclock.gfstimeclock;

import android.preference.PreferenceManager;
import android.text.TextUtils;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class InfoServiceGenerator {
    //private static final String BASE_URL = "https://mysit.gfs.com";
    private static String apiBaseURL = PreferenceManager.getDefaultSharedPreferences(Startup.getContext()).
            getString("employeeAddress", "https://mysit.gfs.com");
    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(apiBaseURL)
            .addConverterFactory(JacksonConverterFactory.create());
    private static Retrofit retrofit = builder.build();
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    public static <S> S createService(Class<S> serviceClass) {
            return createService(serviceClass, null, null);
            //return retrofit.create(serviceClass);
    }
    public static void changeApiBaseURL(String newURL) {
        apiBaseURL = newURL;
        builder = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create()).baseUrl(apiBaseURL);
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
                
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(logging);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }

}
