package com.equitybot.trade.db.mongodb.property.domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "KiteProperty")
public class KiteProperty {

	

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 7040460629552792649L;
	@Id
	private String id;
	private String apiKey;
	@Indexed(unique = true)
	private String userId;
	private String requestToken;
	private String apiSecret;
	private String public_token;
	private String access_token;
	private String refresh_token;
	private String password;
	private double stopLoss;
	private int quantity;
	private Map<String, String> secretQuestions=new HashMap<String, String>();
	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
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

	public String getApiSecret() {
		return apiSecret;
	}

	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPublic_token() {
		return public_token;
	}

	public void setPublic_token(String public_token) {
		this.public_token = public_token;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, String> getSecretQuestions() {
		return secretQuestions;
	}

	public void setSecretQuestions(Map<String, String> secretQuestions) {
		this.secretQuestions = secretQuestions;
	}

	public double getStopLoss() {
		return stopLoss;
	}

	public void setStopLoss(double stopLoss) {
		this.stopLoss = stopLoss;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "KiteProperty [id=" + id + ", apiKey=" + apiKey + ", userId=" + userId + ", requestToken=" + requestToken
				+ ", apiSecret=" + apiSecret + ", public_token=" + public_token + ", access_token=" + access_token
				+ ", refresh_token=" + refresh_token + ", password=" + password + ", stopLoss=" + stopLoss
				+ ", quantity=" + quantity + ", secretQuestions=" + secretQuestions + "]";
	}

	

}
