package com.shaheen.a5thgeneration.signupproject;


import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Shani on 9/24/2017.
 */

public class Application extends android.app.Application {

    private  static Application instance;
    public static Application get() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("com.shaheen.a5thgeneration.signupproject")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);



    }
}