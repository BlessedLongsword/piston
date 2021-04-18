package com.example.piston.utilities.textwatchers;

import android.text.Editable;

import com.google.android.material.textfield.TextInputLayout;

public class CounterWatcher extends BaseTextWatcher {

    int max_num;
    TextInputLayout textField;

    public CounterWatcher(int max_num, TextInputLayout textField) {
        this.max_num = max_num;
        this.textField = textField;
    }

    @Override
    public void afterTextChanged(Editable s) {
        textField.setSuffixText(Integer.toString(max_num - s.length()));
    }

}
