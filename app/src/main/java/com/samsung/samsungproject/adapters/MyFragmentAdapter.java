package com.samsung.samsungproject.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.samsung.samsungproject.fragments.ProfileFragment;
import com.samsung.samsungproject.fragments.TaskFragment;
import com.samsung.samsungproject.fragments.UsersFragment;

public class MyFragmentAdapter extends FragmentStateAdapter {
    TaskFragment taskFragment = new TaskFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    UsersFragment usersFragment = new UsersFragment();

    public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return taskFragment;
            case 1:
                return usersFragment;
            case 2:
                return profileFragment;
            default:
                throw new IllegalArgumentException("Unknown position");
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
