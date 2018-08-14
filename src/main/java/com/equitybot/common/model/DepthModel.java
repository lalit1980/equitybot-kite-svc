package com.equitybot.common.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Tick")
public class DepthModel {
    @Id
    private String id;
    private int quantity;
    private double price;
    private int orders;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "DepthModel{" +
                "id='" + id + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", orders=" + orders +
                '}';
    }
}
