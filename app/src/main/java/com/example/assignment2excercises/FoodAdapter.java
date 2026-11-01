package com.example.assignment2excercises;

// MyAdapter.java
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    private final List<ItemModel> itemList;
    private List<ItemModel> filteredList;

    // Constructor to pass the data array
    public FoodAdapter(List<ItemModel> itemList) {
        this.itemList = itemList;
        this.filteredList = new ArrayList<>(itemList);
    }

    // Creates new views
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    // Binds the data to the elements inside each view row
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemModel currentItem = filteredList.get(position);
        holder.tvName.setText(currentItem.getName());
        holder.tvType.setText(currentItem.getType());
        holder.tvPrice.setText(String.valueOf(currentItem.getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent pointing to your new DetailActivity class
                Intent intent = new Intent(v.getContext(), UpdateFood.class);

                // Pass the entire object using a unique key string
                intent.putExtra("SELECTED_ITEM", currentItem);

                // Start the new activity
                v.getContext().startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current Document ID
                String docId = currentItem.getDocumentId();

                if (docId != null) {
                    // Get the database instance, find the document by the document id and delete it
                    // Only runs if the Document ID is NOT NULL
                    FirebaseFirestore.getInstance()
                            .collection("food")
                            .document(docId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(v.getContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(v.getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }

    // Returns the size of the dataset
    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void updateList(List<ItemModel> newList) {
        this.filteredList = newList;
        notifyDataSetChanged();
    }

    // View references
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvType;
        TextView tvPrice;
        Button btnDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

