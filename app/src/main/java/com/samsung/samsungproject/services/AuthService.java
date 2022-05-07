package com.samsung.samsungproject.services;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.samsung.samsungproject.models.User;

public class AuthService {
    public static Task<AuthResult> signIn(String email, String password) {
        return FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password);
    }

    public static boolean isSignIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static Task<AuthResult> createUser(String email, String password) {
        return FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User user = new User("username", email, "", 0, FirebaseAuth.getInstance().getUid());
                        System.out.println(user);
                        DatabaseService.addUser(user);
                    }
                });
    }

    public static void logOut() {
        FirebaseAuth.getInstance().signOut();
    }
}

