package com.example.assignment2excercises;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ItemModel implements Serializable {
    private String name;
    private String type;
    private String colour;
    private String size;
    private double price;
    private String documentId;

    // Base constructor
    public ItemModel() {}

    // Complete constructor
    public ItemModel(String name, String type, String colour, String size, double price) {
        this.name = name;
        this.type = type;
        this.colour = colour;
        this.size = size;
        this.price = price;
    }

    // Getters and Setters for the properties of each fruit
    @Exclude
    public String getDocumentId() {
        return documentId;
    }
    @Exclude
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getColour() {
        return colour;
    }
    public void setColour(String colour) {
        this.colour = colour;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}


