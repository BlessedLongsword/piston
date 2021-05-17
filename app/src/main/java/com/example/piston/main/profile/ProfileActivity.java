package com.example.piston.main.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.piston.R;
import com.example.piston.databinding.ActivityProfileBinding;
import com.example.piston.main.posts.PostActivity;
import com.example.piston.main.profile.image.ProfileImageActivity;
import com.example.piston.utilities.EditPopup;
import com.example.piston.utilities.MyViewModelFactory;
import com.example.piston.utilities.Values;
import com.example.piston.utilities.textwatchers.BaseTextWatcher;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel profileViewModel;
    private ActivityProfileBinding binding;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        email = getIntent().getStringExtra(Values.EMAIL);

        profileViewModel = new ViewModelProvider(this, new MyViewModelFactory(email))
                .get(ProfileViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setViewModel(profileViewModel);
        binding.setLifecycleOwner(this);
        binding.profileToolbar.setNavigationOnClickListener(v -> finish());

        profileViewModel.getImageLink().observe(this, s ->
            Glide.with(this)
                    .load(s)
                    .placeholder(R.drawable.default_profile_picture)
                    .into(binding.profilePicture)
        );

        profileViewModel.getFeaturedPost().observe(this, post -> {
            if (post == null) {
                binding.postItemCard.setVisibility(View.GONE);
                binding.viewProfileNoPost.setVisibility(View.VISIBLE);
                if (Objects.requireNonNull(profileViewModel.getIsCurrentUser().getValue()))
                    binding.viewProfileNoPost.setText(R.string.no_featured_current);
                else
                    binding.viewProfileNoPost.setText(R.string.no_featured);

            } else {
                binding.postItemCard.setVisibility(View.VISIBLE);
                binding.viewProfileNoPost.setVisibility(View.GONE);
                binding.postTitle.setText(post.getTitle());
                binding.postContent.setText(post.getContent());
                Glide.with(this)
                        .load(post.getImageLink())
                        .into(binding.postPicture);
                binding.postItemCard.setOnClickListener(v -> {
                    Intent intent = new Intent(this, PostActivity.class);
                    intent.putExtra(Values.SCOPE, Values.GLOBAL);
                    intent.putExtra(Values.SECTION_ID, post.getSectionID());
                    intent.putExtra(Values.POST_ID, post.getId());
                    intent.putExtra(Values.ORPHAN, true);
                    startActivity(intent);
                });
            }
        });
    }

    @SuppressWarnings("unused")
    public void clickImage(View view){
        Intent intent = new Intent(this, ProfileImageActivity.class);
        intent.putExtra(Values.EMAIL, email);
        intent.putExtra(Values.IS_CURRENT_USER, profileViewModel.getIsCurrentUser().getValue());
        startActivity(intent);
    }

    @SuppressWarnings("unused")
    public void editName(View view) {
        profileViewModel.resetValues();
        EditPopup popup = new EditPopup(this, getString(R.string.view_profile_name),
                Objects.requireNonNull(binding.profileFullName.getText().toString()));
        popup.getSaveButton().setOnClickListener(v -> profileViewModel.editName(popup.getText()));
        profileViewModel.getFinished().observe(this, finished -> {
            if (finished) {
                popup.dismiss();
                profileViewModel.loadProfile();
            }
        });
    }

    @SuppressWarnings("unused")
    public void editPhone(View view) {
        profileViewModel.resetValues();
        EditPopup popup = new EditPopup(this, getString(R.string.view_profile_phone),
                Objects.requireNonNull(binding.profilePhoneNumber.getText()).toString());
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
                Objects.requireNonNull(binding.profileDateOfBirth.getText()).toString());
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