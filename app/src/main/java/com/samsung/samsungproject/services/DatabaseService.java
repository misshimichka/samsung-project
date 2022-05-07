package com.samsung.samsungproject.services;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.samsung.samsungproject.listeners.MyValueEventListener;
import com.samsung.samsungproject.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DatabaseService {
    public static FirebaseDatabase getDatabase() {
        return FirebaseDatabase.getInstance("https://samsung-project-c692e-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    public static void getUsers(MyValueEventListener<List<User>> listener) {
        getDatabase().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = child.getValue(User.class);
                    assert user != null;
                    user.id = (String) child.getKey();
                    System.out.println(user);
                    users.add(user);
                }

                listener.onValue(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("DatabaseService:getUsers():43 " + error.getMessage());

            }
        });
    }

    public static void addUser(User user) {
        getDatabase().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(user);
    }

    public static void changeUser(String nickname, String info) {
        DatabaseReference database = getDatabase().getReference("users/" + Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String email = (String) dataSnapshot.child("email").getValue();
                long tasks = (long) dataSnapshot.child("tasks").getValue();
                User user = new User(nickname, email, info, tasks, (String) dataSnapshot.getKey());
                Map<String, Object> postValues = user.toMap();
                getDatabase().getReference("users/" + Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())).updateChildren(postValues);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("DatabaseService:changeUser():69 " + databaseError.getMessage());

            }
        });
    }
}