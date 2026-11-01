package com.example.assignment2excercises;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_AF;
    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private List<ItemModel> itemList;
    private FirebaseFirestore db;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        itemList = new ArrayList<>();

        // Setup the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize and assign the adapter
        adapter = new FoodAdapter(itemList);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firestore
        loadDataFromFirestore();

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();

        // SearchView functions
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // Update the list when the text in the searchbox is changed
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        btn_AF = findViewById(R.id.btnAF);

        btn_AF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewFood.class);
                startActivity(intent);
            }
        });
    }

    private void filter(String text) {
        List<ItemModel> filteredList = new ArrayList<>();

        for (ItemModel item : itemList) {
            // Search matches against item names
            if (item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Push the filtered subset to the adapter view list
        adapter.updateList(filteredList);
    }

    // Fetch the data stored in the "food" collection of the firestore database
    private void loadDataFromFirestore() {
        db.collection("food")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("FirestoreError", "Listen failed.", error);
                            Toast.makeText(MainActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (value != null) {
                            itemList.clear();

                            // Get each item from the database and add them to an arrayList
                            for (DocumentSnapshot doc : value.getDocuments()) {
                                ItemModel item = doc.toObject(ItemModel.class);
                                if (item != null) {
                                    item.setDocumentId(doc.getId());
                                    itemList.add(item);
                                }
                            }

                            adapter.updateList(new ArrayList<>(itemList));
                        }
                    }
                });
    }
}