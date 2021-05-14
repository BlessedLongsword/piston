package com.example.piston.main.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityProfileBinding;
import com.example.piston.main.profile.image.ProfileImageActivity;
import com.example.piston.utilities.EditPopup;
import com.example.piston.utilities.textwatchers.BaseTextWatcher;
import com.example.piston.utilities.textwatchers.CounterWatcher;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel profileViewModel;
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileViewModel = new ViewModelProvider(this)
                .get(ProfileViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setViewModel(profileViewModel);
        binding.setLifecycleOwner(this);
        binding.profileToolbar.setNavigationOnClickListener(v -> finish());
        profileViewModel.loadProfile();
    }

    public void clickImage(View view){
        Bitmap bitmap = BitmapFactory.decodeResource
                (getResources(), R.drawable.default_profile_picture); // your bitmap
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs);
        Intent intent = new Intent(this, ProfileImageActivity.class);
        intent.putExtra("byteArray", bs.toByteArray());
        startActivity(intent);
    }

    public void editName(View view) {
        profileViewModel.resetValues();
        EditPopup popup = new EditPopup(this, getString(R.string.view_profile_name),
                Objects.requireNonNull(binding.viewProfileFullNameEditText.getText()).toString());
        popup.getSaveButton().setOnClickListener(v -> profileViewModel.editName(popup.getText()));
        profileViewModel.getFinished().observe(this, finished -> {
            if (finished) {
                popup.dismiss();
                profileViewModel.loadProfile();
            }
        });
    }

    public void editPhone(View view) {
        profileViewModel.resetValues();
        EditPopup popup = new EditPopup(this, getString(R.string.view_profile_phone),
                Objects.requireNonNull(binding.viewProfilePhoneEditText.getText()).toString());
        popup.getSaveButton().setOnClickListener(v -> profileViewModel.editPhone(popup.getText()));
        popup.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        profileViewModel.getFinished().observe(this, finished -> {
            if (finished) {
                popup.dismiss();
                profileViewModel.loadProfile();
            }
        });
    }

    public void editBirth(View view) {
        profileViewModel.resetValues();
        EditPopup popup = new EditPopup(this, getString(R.string.birth_date),
                Objects.requireNonNull(binding.viewProfileDateEditText.getText()).toString());
        popup.getSaveButton().setOnClickListener(v -> profileViewModel.editBirth(popup.getText()));
        profileViewModel.getBirthDateError().observe(this, birthDateError -> {
            switch (birthDateError) {
                case NONE:
                    popup.getTextInputLayout().setError(null);
                    popup.getSaveButton().setEnabled(true);
                    break;
                case INVALID:
                    popup.getTextInputLayout().setError(view.getContext().getString(R.string.invalid_date));
                    popup.getSaveButton().setEnabled(false);
                    break;
            }
        });
        popup.getSaveButton().setEnabled(false);
        popup.getEditText().addTextChangedListener(new BaseTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                profileViewModel.birthdayUpdate(popup.getText());
            }
        });
        profileViewModel.getFinished().observe(this, finished -> {
            if (finished) {
                popup.dismiss();
                profileViewModel.loadProfile();
            }
        });
    }
}