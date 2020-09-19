package com.sbs.jhs.be.and;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 이 코드는 그 어떤 액티비티가 실행되기 전에 실행된다.
        AppDatabase.init(this);
    }

}
