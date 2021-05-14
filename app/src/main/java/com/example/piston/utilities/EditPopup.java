package com.example.piston.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.piston.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class EditPopup {

    private final PopupWindow popupWindow;
    private final TextInputLayout textInputLayout;
    private final Button saveButton;

    public EditPopup(Activity activity, String title, String value) {

        View popupView = activity.getLayoutInflater().inflate(R.layout.popup_edit,
                new LinearLayout(activity));

        ((TextView) popupView.findViewById(R.id.edit_field)).setText(title);

        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // If you need the PopupWindow to dismiss when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setAnimationStyle(R.style.popup_window_animation_slide);

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Using location, the PopupWindow will be displayed right under anchorView
        popupWindow.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);

        // dims background when popup window shows up
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha=0.5f;
        activity.getWindow().setAttributes(lp);

        // restore dim
        popupWindow.setOnDismissListener(() -> {
            lp.alpha=1f;
            activity.getWindow().setAttributes(lp);
        });

        popupView.findViewById(R.id.cancel_button).setOnClickListener((view) -> popupWindow.dismiss());

        textInputLayout = popupView.findViewById(R.id.edit_text);
        Objects.requireNonNull(textInputLayout.getEditText()).requestFocus();
        saveButton = popupView.findViewById(R.id.save_button);
        getEditText().setText(value);
        getEditText().selectAll();

        // Show keyboard once pop up has finished the animation
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
                    }
                },
                500
        );
    }

    public TextInputLayout getTextInputLayout() {
        return textInputLayout;
    }

    public EditText getEditText() {
        return textInputLayout.getEditText();
    }

    public String getText() {
        return getEditText().getText().toString();
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void dismiss() {
        popupWindow.dismiss();
    }
}
