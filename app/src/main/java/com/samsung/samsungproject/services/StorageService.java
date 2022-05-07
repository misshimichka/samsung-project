package com.samsung.samsungproject.services;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Objects;

public class StorageService {
    public static void addImage(byte[] bytes) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://samsung-project-c692e.appspot.com");
        StorageReference storageRef = storage.getReference("avatars");
        StorageReference riversRef = storageRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        UploadTask uploadTask = riversRef.putBytes(bytes);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("ERROR");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("SUCCESS UPLOAD");
            }
        });
    }
}
