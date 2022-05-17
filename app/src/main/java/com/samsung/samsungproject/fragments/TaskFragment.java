package com.samsung.samsungproject.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.samsung.samsungproject.R;
import com.samsung.samsungproject.models.User;
import com.samsung.samsungproject.services.DatabaseService;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class TaskFragment extends Fragment {

    ImageView imageView;
    Button button;

    public TaskFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);

        imageView = rootView.findViewById(R.id.taskImageView);
        button = rootView.findViewById(R.id.buttonCheckAnswer);
        getImage();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = DatabaseService.getDatabase().getReference("users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = (String) snapshot.child("name").getValue();
                        String email = (String) snapshot.child("email").getValue();
                        String info = (String) snapshot.child("description").getValue();
                        long tasks = (long) snapshot.child("tasks").getValue();
                        String id = (String) snapshot.child("id").getValue();
                        System.out.println(tasks);
                        User user = new User(name, email, info, tasks + 1, id);
                        Map<String, Object> postValues = user.toMap();
                        DatabaseService.getDatabase().getReference("users/" + Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())).updateChildren(postValues);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("TaskFragment:onClickListener:73 " + error.getMessage());
                    }
                });
            }
        });

        return rootView;
    }

    void getImage() {

        DatabaseReference databaseReference = DatabaseService.getDatabase().getReference("users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://samsung-project-c692e.appspot.com");
                long tasks = (long) snapshot.child("tasks").getValue() + 1;
                System.out.println("tasks/" + tasks + ".png");
                StorageReference storageRef = storage.getReference("tasks/" + tasks + ".png");

                final int ONE_MEGABYTE = 1024 * 1024;
                storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        float aspectRatio = bitmap.getHeight() /
                                (float) bitmap.getWidth();
                        int width = 800;
                        int height = Math.round(width * aspectRatio);

                        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        System.out.println("TaskFragment:getImage():106 " + Arrays.toString(exception.getStackTrace()));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("TaskFragment:getImage():114 " + error.getMessage());
            }
        });
    }
}
