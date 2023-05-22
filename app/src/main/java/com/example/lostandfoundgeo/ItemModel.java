package com.example.lostandfoundgeo;
import java.io.Serializable;

public class ItemModel implements Serializable {
    private String name, phone, description, date, location, isLost, latitude, longitude;
    private int id;

    public ItemModel(int id, String name, String phone, String description, String date, String location, String isLost, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.isLost = isLost;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ItemModel(String name, String phone, String description, String date, String location, String isLost) {
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.isLost = isLost;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ItemModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsLost() {
        return isLost;
    }

    public void setIsLost(String isLost) {
        this.isLost = isLost;
    }
}