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

    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

         profileViewModel = new ViewModelProvider(this)
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
                popUpWindow(R.layout.popup_profile_edit_phone_number, findViewById(R.id.edit_phone_number_text_field));
                break;
            case BIRTH_DATE:
                popUpWindow(R.layout.popup_profile_edit_birth_date, findViewById(R.id.edit_birth_date_text_field));
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

    }

    /*


    public void cancel(MenuItem item) {

        fullName.getEditText().setInputType(InputType.TYPE_NULL);
        phoneNumber.getEditText().setInputType(InputType.TYPE_NULL);
        bday.getEditText().setInputType(InputType.TYPE_NULL);

        fullName.getEditText().setFocusableInTouchMode(false);
        phoneNumber.getEditText().setFocusableInTouchMode(false);
        bday.getEditText().setFocusableInTouchMode(false);

        fullName.getEditText().clearFocus();
        phoneNumber.getEditText().clearFocus();
        bday.getEditText().clearFocus();
    }

    public void edit(MenuItem item) {

        fullName.getEditText().setFocusableInTouchMode(true);
        phoneNumber.getEditText().setFocusableInTouchMode(true);
        bday.getEditText().setFocusableInTouchMode(true);

        fullName.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        phoneNumber.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        bday.getEditText().setInputType(InputType.TYPE_CLASS_DATETIME);

        fullName.getEditText().requestFocus();
    }

    private void init () {
        pfp = findViewById(R.id.profile_picture);
        pfp.setImageBitmap(BitmapFactory.decodeResource
                (getResources(), R.drawable.jojo));
        username = findViewById(R.id.viewProfile_username);
        fullName = findViewById(R.id.viewProfile_fullName);
        phoneNumber = findViewById(R.id.viewProfile_phone);
        email = findViewById(R.id.viewProfile_email);
        bday = findViewById(R.id.viewProfile_date);
        featuredPost = findViewById(R.id.viewProfile_featPost);
        mat = findViewById(R.id.profile_toolbar);
        mat.setTitleTextColor(Color.WHITE);
        //cancel = findViewById(R.id.viewProfile_cancel);
        //edit = findViewById(R.id.viewProfile_edit);
        //save = findViewById(R.id.viewProfile_save);

        username.getEditText().setInputType(InputType.TYPE_NULL);
        fullName.getEditText().setInputType(InputType.TYPE_NULL);
        phoneNumber.getEditText().setInputType(InputType.TYPE_NULL);
        email.getEditText().setInputType(InputType.TYPE_NULL);
        bday.getEditText().setInputType(InputType.TYPE_NULL);

        fullName.setEndIconOnClickListener(v -> popUpWindow(v, fullName.getEditText()));
        phoneNumber.setEndIconOnClickListener(v -> popUpWindow(v, phoneNumber.getEditText()));
        bday.setEndIconOnClickListener(v -> popUpWindow(v, bday.getEditText()));
    }

            ed.getEditText().setText(edit.getText().toString());
        ed.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(ed.getEditText().getText().toString().length() > 0) {
                    ed.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    ed.setEndIconDrawable(R.drawable.outline_send_black_24);
                }
                else {
                    ed.setEndIconMode(TextInputLayout.END_ICON_NONE);
                }
            }
        });
*/
}