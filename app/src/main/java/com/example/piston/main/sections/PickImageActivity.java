package com.example.piston.main.sections;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class PickImageActivity extends AppCompatActivity {

    protected byte[] image;

    public void imagePick(View v) {
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
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap;
                if (Build.VERSION.SDK_INT < 28)
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                else {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageUri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                }
                setImage(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                image = baos.toByteArray();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void setImage(Bitmap bitmap);
}
