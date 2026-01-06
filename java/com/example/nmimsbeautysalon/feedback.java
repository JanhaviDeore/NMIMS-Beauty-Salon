package com.example.nmimsbeautysalon;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class feedback extends AppCompatActivity {

    private RatingBar rate;
    private Button button;
    private EditText feedbackText;

    // Firebase references
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference feedbackRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Initialize Firebase instances
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        feedbackRef = database.getReference("feedback");

        // Initialize UI elements
        rate = findViewById(R.id.rate);
        button = findViewById(R.id.submit);
        feedbackText = findViewById(R.id.feedbacktext);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input (rating and feedback message)
                float rating = rate.getRating();
                String feedbackMessage = feedbackText.getText().toString().trim();

                if (feedbackMessage.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your feedback.", Toast.LENGTH_SHORT).show();
                } else if (rating == 0) {
                    Toast.makeText(getApplicationContext(), "Please provide a rating.", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the current user's UID and email
                    String userId = auth.getCurrentUser().getUid();
                    String email = auth.getCurrentUser().getEmail();

                    // Show feedback message before sending to Firebase
                    Toast.makeText(getApplicationContext(), "Rating: " + rating + " stars\nFeedback: " + feedbackMessage, Toast.LENGTH_LONG).show();

                    // Create a Feedback object
                    Feedback feedback = new Feedback(userId, email, rating, feedbackMessage);

                    // Store the feedback under the user's unique node (feedback/email)
                    feedbackRef.child(email.replace(".", ",")).push().setValue(feedback);

                    // Navigate to the Thank You page
                    Intent intent = new Intent(feedback.this, thankyou.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Toast.makeText(this, "Navigating to Home Page", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(feedback.this, MainActivity2.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Feedback class to model feedback data
    public static class Feedback {
        public String userId;
        public String email;
        public float rating;
        public String message;

        // Default constructor required for calls to DataSnapshot.getValue(Feedback.class)
        public Feedback() {}

        // Constructor to set values
        public Feedback(String userId, String email, float rating, String message) {
            this.userId = userId;
            this.email = email;
            this.rating = rating;
            this.message = message;
        }
    }
}
