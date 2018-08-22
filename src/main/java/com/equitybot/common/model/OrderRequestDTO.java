package com.equitybot.common.model;

import java.io.Serializable;

public class OrderRequestDTO implements Serializable {

    private String parentOrderId;
    private String id;
    private String orderId;
    private long instrumentToken;
    private String userId;
    private String requestToken;
    private int quantity;
    private String orderType;
    private String tradingsymbol;
    private String product;
    private String exchange;
    private String transactionType;
    private String validity;
    private double price;
    private double triggerPrice;
    private double trailingStopLossPrice;
    private double stopLossPrice;
    private double targetPrice;
    private String tag;
    private String variety;
    private Integer disclosedQuantity;
    private Double squareoff;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getInstrumentToken() {
        return instrumentToken;
    }

    public void setInstrumentToken(long instrumentToken) {
        this.instrumentToken = instrumentToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTradingsymbol() {
        return tradingsymbol;
    }

    public void setTradingsymbol(String tradingsymbol) {
        this.tradingsymbol = tradingsymbol;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTriggerPrice() {
        return triggerPrice;
    }

    public void setTriggerPrice(double triggerPrice) {
        this.triggerPrice = triggerPrice;
    }

    public double getTrailingStopLossPrice() {
        return trailingStopLossPrice;
    }

    public void setTrailingStopLossPrice(double trailingStopLossPrice) {
        this.trailingStopLossPrice = trailingStopLossPrice;
    }

    public double getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(double stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public Integer getDisclosedQuantity() {
        return disclosedQuantity;
    }

    public void setDisclosedQuantity(Integer disclosedQuantity) {
        this.disclosedQuantity = disclosedQuantity;
    }

    public Double getSquareoff() {
        return squareoff;
    }

    public void setSquareoff(Double squareoff) {
        this.squareoff = squareoff;
    }

    public String getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(String parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    @Override
    public String toString() {
        return "OrderRequestDTO{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", instrumentToken=" + instrumentToken +
                ", userId='" + userId + '\'' +
                ", requestToken='" + requestToken + '\'' +
                ", quantity=" + quantity +
                ", orderType='" + orderType + '\'' +
                ", tradingsymbol='" + tradingsymbol + '\'' +
                ", product='" + product + '\'' +
                ", exchange='" + exchange + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", validity='" + validity + '\'' +
                ", price=" + price +
                ", triggerPrice=" + triggerPrice +
                ", trailingStopLossPrice=" + trailingStopLossPrice +
                ", stopLossPrice=" + stopLossPrice +
                ", targetPrice=" + targetPrice +
                ", tag='" + tag + '\'' +
                ", variety='" + variety + '\'' +
                ", disclosedQuantity=" + disclosedQuantity +
                ", squareoff=" + squareoff +
                ", parentOrderId='" + parentOrderId + '\'' +
                '}';
    }
}