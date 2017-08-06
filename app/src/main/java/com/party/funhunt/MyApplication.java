package com.party.funhunt;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Ermike on 3/29/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/JosefinSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


    }
}
