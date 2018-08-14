package com.equitybot.kite.dao.property.repository;

import com.equitybot.kite.dao.property.domain.KiteProperty;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import java.util.Map;

public interface PropertyRespositoryCustom {

    public UpdateResult updatePropertyByUserId(String userId, String requestToken, String access_token, String public_token);

    public UpdateResult updatePropertyByUserIdMapData(String userId, Map<String, String> secretQuestions);

    public UpdateResult updatRequestTokenByUserId(String userId, String requestToken);

    public KiteProperty findByUserId(String userId);

    public DeleteResult deleteByUserId(String userId);

}
