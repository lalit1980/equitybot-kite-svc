package com.equitybot.algorithm.dao.supertrend.profit.repository;


import com.equitybot.algorithm.dao.supertrend.profit.domain.SuperTrendProfitEntity;

import java.util.List;

public interface SuperTrendProfitRepositoryCustom {

    public void saveData(SuperTrendProfitEntity superTrendProfitEntity);

    public long updateByInstrument(SuperTrendProfitEntity superTrendProfitEntity);

    public void saveUpdate(SuperTrendProfitEntity superTrendProfitEntity);

    public long deleteByInstrumentToken(SuperTrendProfitEntity superTrendProfitEntity);

    public void deleteAll();

    public List<SuperTrendProfitEntity> findByInstrumentToken(long token);

    public List<SuperTrendProfitEntity> getMexProfitDataList(int numOfMexRecord);

    public List<SuperTrendProfitEntity> getMexLossDataList(int numOfMenRecord);


}
