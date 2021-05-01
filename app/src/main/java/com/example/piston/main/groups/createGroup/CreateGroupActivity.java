package com.example.piston.main.groups.createGroup;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.piston.R;
import com.example.piston.databinding.ActivityCreateGroupBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreateGroupActivity extends AppCompatActivity {

    private CreateGroupViewModel createGroupViewModel;
    private ClipboardManager clipboard;
    private ClipData clip;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_group);

        createGroupViewModel = new ViewModelProvider(this).get(CreateGroupViewModel.class);
        ActivityCreateGroupBinding  binding = DataBindingUtil.setContentView(this, R.layout.activity_create_group);
        binding.setViewModel(createGroupViewModel);
        binding.setLifecycleOwner(this);

        createGroupViewModel.generateGroupID();

        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        binding.groupLink.setEndIconOnClickListener(v -> {
            clip = ClipData.newPlainText("GroupCode", binding.groupLink.getEditText().getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, R.string.link_copied, Toast.LENGTH_LONG).show();
        });

        binding.createGroupTopAppBar.setNavigationOnClickListener(v -> finish());

        createGroupViewModel.getCreateError().observe(this, aBoolean -> {
            if (aBoolean) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getResources().getString(R.string.error))
                        .setMessage(getResources().getString(R.string.create_group_error_message))
                        .setPositiveButton(getResources().getString(R.string.confirmation_long), (dialog, which) -> {
                        })
                        .show();
            }
        });
        createGroupViewModel.getFinishCreateGroup().observe(this, aBoolean -> {
            if (aBoolean)
                finish();
        });

    }

    public void createGroup(MenuItem item) {
        createGroupViewModel.createGroup();
    }

    public void imagePick(View v) {
        ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                ImageView im = findViewById(R.id.group_picture);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                im.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] image = baos.toByteArray();
                createGroupViewModel.uploadImage(image);
            } catch (IOException e) {
                Log.w("DBReadTAG", "c murio");
                e.printStackTrace();
            }
        }
    }

}
