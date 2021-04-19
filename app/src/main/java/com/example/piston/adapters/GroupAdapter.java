package com.example.piston.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piston.R;
import com.example.piston.views.posts.ViewPostsActivity;
import com.example.piston.viewmodels.GroupFragmentViewModel;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupHolder> {

    private final FragmentActivity localActivity;
    private final GroupFragmentViewModel viewModel;

    public static class GroupHolder extends RecyclerView.ViewHolder {

        private final FrameLayout layout;
        private final TextView groupTitle, groupDesc;

        public GroupHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.group_item_card);
            groupTitle = view.findViewById(R.id.group_title);
            groupDesc = view.findViewById(R.id.group_desc);
        }

        public TextView getgroupTitle() {
            return groupTitle;
        }

        public TextView getgroupDesc() {
            return groupDesc;
        }

        public FrameLayout getLayout() {
            return layout;
        }
    }

    public GroupAdapter(FragmentActivity activity) {
        localActivity = activity;
        viewModel = new ViewModelProvider(activity).get(GroupFragmentViewModel.class);
        viewModel.getGroups().observe(activity, cosa -> notifyDataSetChanged());
    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_group, viewGroup, false);
        return new GroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupHolder groupHolder, int position) {
        groupHolder.getgroupTitle().setText(viewModel.getGroups().getValue().get(position).getTitle());
        groupHolder.getgroupDesc().setText(viewModel.getGroups().getValue().get(position).getDescription());
        groupHolder.getLayout().setOnClickListener(view -> {
            Intent intent = new Intent(localActivity, ViewPostsActivity.class);
            intent.putExtra("title", viewModel.getGroups().getValue().get(position).getTitle());
            intent.putExtra("description", viewModel.getGroups().getValue().get(position).getDescription());
            Log.d("what","hihi");
            localActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return viewModel.getGroups().getValue().size();
    }

}
