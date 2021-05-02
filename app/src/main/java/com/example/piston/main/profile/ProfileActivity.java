package com.example.piston.main.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityProfileBinding;
import com.example.piston.main.profile.image.ProfileImageActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfileViewModel profileViewModel = new ViewModelProvider(this)
                .get(ProfileViewModel.class);

        ActivityProfileBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setViewModel(profileViewModel);
        binding.setLifecycleOwner(this);
        binding.profileToolbar.setNavigationOnClickListener(v -> finish());
        profileViewModel.viewProfile();
        profileViewModel.getEditOption().observe(this, editOptions -> {
        switch (editOptions){
            case NONE:
                break;
            case NAME:
                popUpWindow(R.layout.popup_profile_edit_name, binding.viewProfileFullNameEditText);
                break;
            case PHONE:
                popUpWindow(R.layout.popup_profile_edit_phone_number, binding.viewProfilePhoneEditText);
                break;
            case BIRTH_DATE:
                popUpWindow(R.layout.popup_profile_edit_birth_date, binding.viewProfileDateEditText);
                break;
        }
        });
    }

    public void clickImage(View view){
        Bitmap bitmap = BitmapFactory.decodeResource
                (getResources(), R.drawable.jojo); // your bitmap
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bs);
        Intent intent = new Intent(this, ProfileImageActivity.class);
        intent.putExtra("byteArray", bs.toByteArray());
        startActivity(intent);
    }

    public void popUpWindow(int layoutResource, TextInputEditText textField) {

        View popupView = getLayoutInflater().inflate(layoutResource, null);

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // If you need the PopupWindow to dismiss when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setAnimationStyle(R.style.popup_window_animation_slide);
        // Adjust popup window location when keyboard pops up
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

        Objects.requireNonNull(textField).requestFocus();
        popupWindow.setOutsideTouchable(false);

        // force show keyboard once pop up window is open
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);

        // dims background when popup window shows up
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha=0.5f;
        getWindow().setAttributes(lp);

        // restore dim
        popupWindow.setOnDismissListener(() -> {
            lp.alpha=1f;
            getWindow().setAttributes(lp);
        });

        popupView.findViewById(R.id.cancel_button).setOnClickListener((view) -> popupWindow.dismiss());
        popupView.findViewById(R.id.save_button).setOnClickListener((view) -> {
            Toast.makeText(getApplicationContext(), "Not yet implemented", Toast.LENGTH_LONG).show();
            popupWindow.dismiss();
        });
    }
}