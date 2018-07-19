package com.equitybot.trade.db.mongodb.order.domain;

import java.io.Serializable;
import java.util.Arrays;

public class UserProfileDTO implements Serializable {
	public String userType;
	public String email;
	public String userName;
	public String userShortname;
	public String broker;
	public String[] exchanges;
	public String[] products;
	public String[] orderTypes;
	public String avatarURL;
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserShortname() {
		return userShortname;
	}
	public void setUserShortname(String userShortname) {
		this.userShortname = userShortname;
	}
	public String getBroker() {
		return broker;
	}
	public void setBroker(String broker) {
		this.broker = broker;
	}
	public String[] getExchanges() {
		return exchanges;
	}
	public void setExchanges(String[] exchanges) {
		this.exchanges = exchanges;
	}
	public String[] getProducts() {
		return products;
	}
	public void setProducts(String[] products) {
		this.products = products;
	}
	public String[] getOrderTypes() {
		return orderTypes;
	}
	public void setOrderTypes(String[] orderTypes) {
		this.orderTypes = orderTypes;
	}
	public String getAvatarURL() {
		return avatarURL;
	}
	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}
	@Override
	public String toString() {
		return "UserProfileDTO [userType=" + userType + ", email=" + email + ", userName=" + userName
				+ ", userShortname=" + userShortname + ", broker=" + broker + ", exchanges="
				+ Arrays.toString(exchanges) + ", products=" + Arrays.toString(products) + ", orderTypes="
				+ Arrays.toString(orderTypes) + ", avatarURL=" + avatarURL + "]";
	}
}
