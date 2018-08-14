package com.equitybot.kite.dao.property.repository;

import com.equitybot.kite.dao.property.domain.KiteProperty;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PropertyRepository extends MongoRepository<KiteProperty, Long>, PropertyRespositoryCustom {

}
