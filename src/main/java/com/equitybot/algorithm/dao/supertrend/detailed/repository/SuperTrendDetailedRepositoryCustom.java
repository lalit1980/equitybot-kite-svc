package com.equitybot.algorithm.dao.supertrend.detailed.repository;

import com.equitybot.algorithm.dao.supertrend.detailed.domain.SuperTrendDetailedEntity;

import java.util.List;

public interface SuperTrendDetailedRepositoryCustom {
    public List<SuperTrendDetailedEntity> findByInstrumentToken(long instrument);

    public void saveSuperTrendDetailedEntity(SuperTrendDetailedEntity superTrendDetailedEntity);
}
