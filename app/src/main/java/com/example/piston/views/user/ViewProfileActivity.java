package com.example.piston.views.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;

public class ViewProfileActivity extends AppCompatActivity {

    TextInputLayout username, fullName, phoneNumber, email, bday;
    TextView featuredPost;
    ImageView pfp;
    MaterialToolbar mat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
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

    public void popUpWindow(View anchorView, EditText edit) {

        /*

        View popupView = getLayoutInflater().inflate(R.layout.reply_post_youtube, findViewById(android.R.id.content));

        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // If you need the PopupWindow to dismiss when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setAnimationStyle(R.style.popup_window_animation_slide);
        // Adjust popup window location when keyboard pops up :)))
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 0);

        // Initialize objects from layout
        TextInputLayout ed = popupView.findViewById(R.id.popup);
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

        ed.getEditText().requestFocus();
        popupWindow.setOutsideTouchable(false);

        // force show keyboar once pop up window is open
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
        });*/
    }

    public void cancel(MenuItem item) {
        /*save.setEnabled(false);
        save.setVisibility(View.INVISIBLE);
        edit.setEnabled(true);
        edit.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.INVISIBLE);
        cancel.setEnabled(false);*/

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
        /*save.setEnabled(true);
        save.setVisibility(View.VISIBLE);
        edit.setEnabled(false);
        edit.setVisibility(View.INVISIBLE);
        cancel.setVisibility(View.VISIBLE);
        cancel.setEnabled(true);*/


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
        /*cancel = findViewById(R.id.viewProfile_cancel);
        edit = findViewById(R.id.viewProfile_edit);
        save = findViewById(R.id.viewProfile_save);*/

        username.getEditText().setInputType(InputType.TYPE_NULL);
        fullName.getEditText().setInputType(InputType.TYPE_NULL);
        phoneNumber.getEditText().setInputType(InputType.TYPE_NULL);
        email.getEditText().setInputType(InputType.TYPE_NULL);
        bday.getEditText().setInputType(InputType.TYPE_NULL);

        fullName.setEndIconOnClickListener(v -> popUpWindow(v, fullName.getEditText()));
        phoneNumber.setEndIconOnClickListener(v -> popUpWindow(v, phoneNumber.getEditText()));
        bday.setEndIconOnClickListener(v -> popUpWindow(v, bday.getEditText()));
    }

}