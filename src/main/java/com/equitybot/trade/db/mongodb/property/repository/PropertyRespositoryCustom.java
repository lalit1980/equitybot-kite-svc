package com.equitybot.trade.db.mongodb.property.repository;

import java.util.Map;

import com.equitybot.trade.db.mongodb.property.domain.KiteProperty;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public interface PropertyRespositoryCustom {

	public UpdateResult updatePropertyByUserId(String userId, String requestToken, String access_token, String public_token);
	public UpdateResult updatePropertyByUserIdMapData(String userId, Map<String, String> secretQuestions);
	public UpdateResult updatRequestTokenByUserId(String userId, String requestToken);
	public UpdateResult updatStopLoss(String userId, double stopLoss);
	public UpdateResult updatQuantity(String userId, double quantity);
	public KiteProperty findByUserId(String userId);
	public DeleteResult deleteByUserId(String userId);
	
}
