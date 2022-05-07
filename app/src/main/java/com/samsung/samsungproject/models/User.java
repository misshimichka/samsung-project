package com.samsung.samsungproject.models;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User {
    public String id = UUID.randomUUID().toString();
    public String name;
    public String email;
    public Date creationDate;
    public String description;
    public long tasks;

    public User() {
    }

    public User(String name, String email, String description, long tasks, String id) {
        this.name = name;
        this.email = email;
        this.description = description;
        this.tasks = tasks;
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", creationDate=" + creationDate +
                ", description=" + description +
                ", id=" + id +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("description", description);
        result.put("tasks", tasks);
        result.put("id", id);

        return result;
    }
}
