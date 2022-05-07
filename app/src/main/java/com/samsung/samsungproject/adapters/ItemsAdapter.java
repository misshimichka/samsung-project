package com.samsung.samsungproject.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.samsung.samsungproject.R;
import com.samsung.samsungproject.models.User;

import java.util.ArrayList;
import java.util.Arrays;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.UserViewHolder> {
    ArrayList<User> users;

    public ItemsAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new UserViewHolder(view);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView tasksTextView;
        ImageView imageView;
        View divider;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.usersNameTextView);
            tasksTextView = itemView.findViewById(R.id.tasksTextView);
            imageView = itemView.findViewById(R.id.imageViewUsers);
            divider = itemView.findViewById(R.id.divider);
        }

        public void bind(User user) {
            nameTextView.setText(user.name);
            tasksTextView.setText("Tasks: " + user.tasks);
            System.out.println("ID: " + user.id);
            getImage(user.id);

        }

        void getImage(String id) {
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://samsung-project-c692e.appspot.com");
            StorageReference storageRef = storage.getReference("avatars/" + id);

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
                    System.out.println("ItemsAdapter:getImage():91 " + Arrays.toString(exception.getStackTrace()));
                }
            });
        }

    }
}