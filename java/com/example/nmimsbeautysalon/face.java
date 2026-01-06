package com.example.nmimsbeautysalon;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class face extends AppCompatActivity {

    Button toDo1, toDo2, toDo3, toDo4;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);

        // Initialize toolbar
        Toolbar toolbar = findViewById(R.id.my_awesome_toolbar_face);
        setSupportActionBar(toolbar);

        // Set up buttons
        toDo1 = findViewById(R.id.toDo1);
        toDo2 = findViewById(R.id.toDo2);
        toDo3 = findViewById(R.id.toDo3);
        toDo4 = findViewById(R.id.toDo4);

        // Add items to to-do list (example)
        toDo1.setOnClickListener(v -> addItemToToDoList("Facial", 500));
        toDo2.setOnClickListener(v -> addItemToToDoList("Face Massage", 300));
        toDo3.setOnClickListener(v -> addItemToToDoList("Threading", 30));
        toDo4.setOnClickListener(v -> addItemToToDoList("UpperLips", 20));
    }

    private void addItemToToDoList(String itemName, int price) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String emailKey = currentUser.getEmail() != null ? currentUser.getEmail().replace(".", ",") : "default_email";
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(emailKey).child("toDoItems");

            // Create a unique entry for each item by using push()
            DatabaseReference newItemRef = databaseReference.push();
            newItemRef.child(itemName).setValue(price);

            Toast.makeText(this, itemName + " added to your To-Do list", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.toDoList) {
            Toast.makeText(this, "Navigating to To-Do List", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(face.this, toDo.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.home) {
            Toast.makeText(this, "Navigating to Home Page", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(face.this, MainActivity2.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

