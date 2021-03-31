package com.example.piston.view;

import android.widget.EditText;

import androidx.lifecycle.Observer;

public class LiveTextObserver implements Observer<String> {

    private final EditText editText;

    public LiveTextObserver(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onChanged(String state) {
        if (state != null)
            editText.setError(state);
    }
}
