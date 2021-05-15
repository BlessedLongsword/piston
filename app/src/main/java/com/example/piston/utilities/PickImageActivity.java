package com.example.piston.utilities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
public abstract class PickImageActivity extends AppCompatActivity {

    protected byte[] image;
    protected Uri imageUri;

    public void imagePick(View v) {
        ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)
                .start();
    }

    public void imagePick(MenuItem item) {
        ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)
                .start();
    }

    /**
     * @deprecated using deprecated method for old Android versions....
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            imageUri = data.getData();
            setUri(imageUri);
        }
    }

    protected abstract void setUri(Uri imageUri);
}
