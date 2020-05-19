package com.vanced.manager.core.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.google.android.material.card.MaterialCardView;
import com.vanced.manager.R;
import com.vanced.manager.core.base.BaseFragment;

//This java code is stinky
public class about_7tap extends BaseFragment {

    private int count = 0;
    private long startMillSec = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialCardView aboutHeader = requireView().findViewById(R.id.about_header_wrapper);
        aboutHeader.setOnTouchListener((v, event) -> {

            int eventAction = event.getAction();
            if (eventAction == MotionEvent.ACTION_UP) {
                long time = System.currentTimeMillis();
                if (startMillSec == 0 || (time - startMillSec > 3000)) {

                    startMillSec = time;
                    count = 1;

                } else {
                    count++;
                }

                if (count == 5) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
                    prefs.edit().putBoolean("devSettings", true).apply();

                }

                return true;
            }

            return false;
        });

    }



}
