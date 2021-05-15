package com.example.piston.main.profile.image;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.databinding.ActivityProfileImageBinding;
import com.example.piston.utilities.CheckNetwork;
import com.example.piston.utilities.MyViewModelFactory;
import com.example.piston.utilities.PickImageActivity;

public class ProfileImageActivity extends PickImageActivity {

    ProfileImageViewModel viewModel;
    ActivityProfileImageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);

        String email = getIntent().getStringExtra("email");

        viewModel = new ViewModelProvider(this, new MyViewModelFactory(email))
                .get(ProfileImageViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_image);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.profileToolbar.setNavigationOnClickListener(v -> finish());

        if (getIntent().getBooleanExtra("isCurrentUser", false))
            binding.profileToolbar.inflateMenu(R.menu.profile_image_top_app_bar);

        viewModel.getImageLink().observe(this, s -> {
            if (s != null) {
                if (!s.equals("")) {
                    Glide.with(this)
                            .load(s)
                            .into(binding.picture);
                }
            }
        });
    }

    public void changeImage(MenuItem item) {
        if (CheckNetwork.isConnected(getApplicationContext()))
            imagePick(item);
        else
            Toast.makeText(getApplicationContext(), "No connection!", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void setUri(Uri imageUri) {
        viewModel.setImage(imageUri);
    }
}
