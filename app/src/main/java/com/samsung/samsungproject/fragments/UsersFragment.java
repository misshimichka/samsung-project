package com.samsung.samsungproject.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.samsung.samsungproject.R;
import com.samsung.samsungproject.adapters.ItemsAdapter;
import com.samsung.samsungproject.listeners.MyValueEventListener;
import com.samsung.samsungproject.models.User;
import com.samsung.samsungproject.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {

    ItemsAdapter adapter;

    public UsersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_users, container, false);

        ArrayList<User> users = new ArrayList<>();


        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        adapter = new ItemsAdapter(users);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter( adapter );

        DatabaseService.getUsers(new MyValueEventListener<List<User>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onValue(List<User> userList) {
                users.addAll(userList);
                adapter.notifyDataSetChanged();
            }
        });


        return rootView;
    }
}
