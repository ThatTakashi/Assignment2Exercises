package com.example.assignment2excercises;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewFood extends AppCompatActivity {

    private FirebaseFirestore db;
    Button btnAddFood;
    EditText etName, etColour, etSize, etType, etPrice;
    double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_new_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Create firestore instance
        db = FirebaseFirestore.getInstance();

        btnAddFood = findViewById(R.id.btnAddFood);
        etName = findViewById(R.id.etFoodName);
        etColour = findViewById(R.id.etFoodColour);
        etSize = findViewById(R.id.etFoodSize);
        etType = findViewById(R.id.etFoodType);
        etPrice = findViewById(R.id.etFoodPrice);

        // onClick listener for adding a new food
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the price is entered or the wrong format
                try {
                    price = Double.parseDouble(etPrice.getText().toString().trim());
                } catch (NumberFormatException e) {
                    Toast.makeText(NewFood.this, "Please enter a number for the price", Toast.LENGTH_LONG).show();
                }

                // Make a new hashmap of everything
                Map<String, Object> food = new HashMap<>();
                food.put("name", etName.getText().toString().trim());
                food.put("colour", etColour.getText().toString().trim());
                food.put("size", etSize.getText().toString().trim());
                food.put("type", etType.getText().toString().trim());
                food.put("price", price);

                // Send request to database with the new data
                db.collection("food")
                        .add(food)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });
    }

}