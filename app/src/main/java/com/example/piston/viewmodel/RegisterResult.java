package com.example.piston.viewmodel;

import androidx.annotation.Nullable;

public class RegisterResult {
    @Nullable
    private Integer error;
    @Nullable
    private boolean sucess;

    RegisterResult(@Nullable Integer error) {
        this.error = error;
    }

    RegisterResult(@Nullable boolean sucess){ this.sucess = sucess; }

    @Nullable
    public Integer getError() {
        return error;
    }

    @Nullable
    public boolean getSuccess() { return sucess; }
}
