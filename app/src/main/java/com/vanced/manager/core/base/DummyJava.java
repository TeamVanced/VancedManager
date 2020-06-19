package com.vanced.manager.core.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class DummyJava {

    private final Activity activity;

    public DummyJava(Activity activity) {
        this.activity = activity;
    }

    private void SomeVoid() {
        SharedPreferences bullshit = activity.getSharedPreferences("installPrefs", Context.MODE_PRIVATE);
    }
}
