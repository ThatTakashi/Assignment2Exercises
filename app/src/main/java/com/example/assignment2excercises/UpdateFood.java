package com.example.assignment2excercises;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateFood extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText etName, etType, etColour, etSize, etPrice;
    double price;
    Button btnUpdateFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_update_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up View references
        etName = findViewById(R.id.etName);
        etType = findViewById(R.id.etType);
        etColour = findViewById(R.id.etColour);
        etSize = findViewById(R.id.etSize);
        etPrice = findViewById(R.id.etPrice);
        btnUpdateFood = findViewById(R.id.btnUpdateFood);

        // Fetch the passed data object
        ItemModel selectedItem = (ItemModel) getIntent().getSerializableExtra("SELECTED_ITEM");

        // Populate layout parameters if the selectedItem exists
        if (selectedItem != null) {
            etName.setText(selectedItem.getName());
            etType.setText(selectedItem.getType());
            etColour.setText(selectedItem.getColour());
            etSize.setText(selectedItem.getSize());
            etPrice.setText(String.format("%.2f", selectedItem.getPrice()));
        }

        // onclick listener for updateFood button
        btnUpdateFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current document ID
                String docId = selectedItem.getDocumentId();

                try {
                    price = Double.parseDouble(etPrice.getText().toString().trim());
                } catch (NumberFormatException e) {
                    Toast.makeText(UpdateFood.this, "Please enter a number for the price", Toast.LENGTH_LONG).show();
                }

                Map<String, Object> updates = new HashMap<>();
                updates.put("name", etName.getText().toString().trim());
                updates.put("colour", etColour.getText().toString().trim());
                updates.put("size", etSize.getText().toString().trim());
                updates.put("type", etType.getText().toString().trim());
                updates.put("price", price); // Your parsed double variable

                // 4. Send the update request
                db.collection("food")
                        .document(docId)
                        .update(updates)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("UpdateFood", "Document successfully updated!");
                            Toast.makeText(UpdateFood.this, "Record updated!", Toast.LENGTH_SHORT).show();
                            finish(); // Go back to your RecyclerView list
                        })
                        .addOnFailureListener(e -> {
                            Log.w("UpdateFood", "Error updating document", e);
                            Toast.makeText(UpdateFood.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}