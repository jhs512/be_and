package com.sbs.jhs.be.and;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class App {
    public static BeApiService getBeApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.78.101.245:8085")
                .addConverterFactory(GsonConverterFactory.create()) // GSON 사용
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) // RX Java 사용
                .build();

        return retrofit.create(BeApiService.class);
    }

    public static boolean isLogined() {
        return AppDatabase.getLoginAuthKey().equals("") == false;
    }

    public static void login(String loginId, String loginAuthKey) {
        AppDatabase.saveLoginId(loginId);
        AppDatabase.saveLoginAuthKey(loginAuthKey);
    }

    public static void logout() {
        AppDatabase.removeLoginId();
        AppDatabase.removeLoginAuthKey();
    }

    public static String getLoginAuthKey() {
        return AppDatabase.getLoginAuthKey();
    }
}
