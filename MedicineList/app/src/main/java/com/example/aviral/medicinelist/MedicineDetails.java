package com.example.aviral.medicinelist;

public class MedicineDetails {

    String key;
    String medicine_names;
    String amount_purchased;
    String dosage;
    String timings;
    Boolean notifications;

    public MedicineDetails() {
    }

    public MedicineDetails(String key, String medicine_names, String amount_purchased, String dosage, String timings, Boolean notifications) {
        this.key = key;
        this.medicine_names = medicine_names;
        this.amount_purchased = amount_purchased;
        this.dosage = dosage;
        this.timings = timings;
        this.notifications = notifications;
    }

    public String getMedicine_names() {
        return medicine_names;
    }

    public String getAmount_purchased() {
        return amount_purchased;
    }

    public String getDosage() {
        return dosage;
    }

    public String getTimings() {
        return timings;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setMedicine_names(String medicine_names) {
        this.medicine_names = medicine_names;
    }

    public void setAmount_purchased(String amount_purchased) {
        this.amount_purchased = amount_purchased;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
