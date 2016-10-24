package org.grupovialibre.dev.appluchar;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by joan on 15/10/16.
 */

public class FireApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
    }
}
