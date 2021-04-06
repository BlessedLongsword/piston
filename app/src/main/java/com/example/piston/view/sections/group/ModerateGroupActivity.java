package com.example.piston.view.sections.group;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piston.R;
import com.google.android.material.textfield.TextInputLayout;

public class ModerateGroupActivity extends AppCompatActivity {

    TextInputLayout groupName, desc, members, link;
    ClipboardManager clipboard;
    ClipData clip;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_moderate_group);

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        link = findViewById(R.id.group_link);
        groupName = findViewById(R.id.moderate_group_name);
        members = findViewById(R.id.moderate_group_members);
        desc = findViewById(R.id.input_group_desc);

        desc.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        link.setEndIconOnClickListener(v -> {
            clip = ClipData.newPlainText("CategoryCode", link.getEditText().getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, R.string.link_copied, Toast.LENGTH_LONG).show();
        });
    }

    public void saveChanges (View v) {
        finish();
    }

    public void deleteGroup (View v) {
        finish();
    }
}
