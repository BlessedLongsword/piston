package com.example.piston.main.profile;

import androidx.databinding.BaseObservable;

public class ProfileResult extends BaseObservable {

    public enum BirthDateError {NONE, INVALID }

    public enum EditOptions { NONE, NAME, PHONE, BIRTH_DATE }

}
