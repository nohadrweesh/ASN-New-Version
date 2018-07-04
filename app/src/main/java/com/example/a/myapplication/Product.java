package com.example.a.myapplication;

public class Product {
    private String carID;
    private String model;
    private String soldBought;
    private String ownerName;
    private String ownerPhone;
    private String price;
    private String status;

    public Product(String carID, String model, String price,String ownerName,String ownerPhone) {
        this.carID = carID;
        this.model = model;

        this.price = price;

        // this.soldBought = soldBought;
        this.ownerName=ownerName;
        this.ownerPhone=ownerPhone;
    }

    public String getCarID() {
        return carID;
    }

    public String getModel() {
        return model;
    }




    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }
    public String getSoldBought() {
        return soldBought;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public String getOwnerPhone() {
        return ownerPhone;
    }
}