package com.equitybot.kite.dao.property;

import com.equitybot.kite.dao.property.domain.KiteProperty;
import com.equitybot.kite.dao.property.repository.PropertyRepository;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KitePropertyDAO {

    @Autowired
    PropertyRepository propertyRepository;

    public UpdateResult updatePropertyByUserId(String userId, String requestToken, String accessToken, String publicToken) {
        return propertyRepository.updatePropertyByUserId(userId, requestToken, accessToken, publicToken);
    }

    public UpdateResult updatePropertyByUserIdMapData(String userId, Map<String, String> secretQuestions) {
        return propertyRepository.updatePropertyByUserIdMapData(userId, secretQuestions);
    }

    public UpdateResult updatRequestTokenByUserId(String userId, String requestToken) {
        return propertyRepository.updatRequestTokenByUserId(userId, requestToken);
    }

    public KiteProperty findByUserId(String userId) {
        return propertyRepository.findByUserId(userId);
    }

    public DeleteResult deleteByUserId(String userId) {
        return propertyRepository.deleteByUserId(userId);
    }

    public void save(KiteProperty kiteProperty) {
        propertyRepository.save(kiteProperty);
    }
}