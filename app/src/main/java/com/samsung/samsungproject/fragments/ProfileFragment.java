package com.samsung.samsungproject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.samsung.samsungproject.ChangeInfoActivity;
import com.samsung.samsungproject.R;
import com.samsung.samsungproject.services.DatabaseService;
import com.samsung.samsungproject.services.StorageService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    Button buttonChangeInfo;
    FloatingActionButton floatingActionButton;
    TextView textViewInfo;
    TextView textViewName;
    ImageView imageView;
    private final int Pick_image = 1;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        getUser();
        getImage();

        textViewName = rootView.findViewById(R.id.nicknameView);
        textViewInfo = rootView.findViewById(R.id.textViewInfo);
        imageView = rootView.findViewById(R.id.userImageView);

        floatingActionButton = rootView.findViewById(R.id.floatingActionButton2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Pick_image);
            }
        });

        buttonChangeInfo = rootView.findViewById(R.id.buttonChangeInfo);
        buttonChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Objects.requireNonNull(getActivity()).getBaseContext(), ChangeInfoActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pick_image) {
            if (resultCode == Activity.RESULT_OK) {
                final Uri imageUri = data.getData();
                final InputStream imageStream;
                try {
                    imageStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(imageUri);
                    final Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    try {
                        if (bytes.length > 1024 * 1024) {
                            Toast.makeText(getContext(), "Maximum size is 1MB", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        StorageService.addImage(bytes);
                        imageView.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Maximum size is 1 MB", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void getUser() {
        DatabaseReference database = DatabaseService.getDatabase().getReference("users/" + Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nickname = dataSnapshot.child("name").getValue(String.class);
                String info = dataSnapshot.child("description").getValue(String.class);
                System.out.println(nickname + " " + info);
                textViewInfo.setText(info);
                textViewName.setText(nickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("ProfileFragment:getUser():134 " + databaseError.getMessage());
            }
        });
    }

    void getImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://samsung-project-c692e.appspot.com");
        StorageReference storageRef = storage.getReference("avatars/" + FirebaseAuth.getInstance().getUid());

        final int ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                float aspectRatio = bitmap.getWidth() /
                        (float) bitmap.getHeight();
                int height = 300;
                int width = Math.round(height * aspectRatio);

                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("ProfileFragment:getImage():159 " + Arrays.toString(exception.getStackTrace()));

            }
        });
    }
}
