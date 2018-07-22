package com.equitybot.trade.db.mongodb.position.repository;

import java.util.List;

import com.equitybot.trade.db.mongodb.order.domain.Position;

public interface PositionCustom {

	public List<Position> findAllByUserId(String userId);
	public List<Position> findAllByTradingSymbol(String tradingSymbol);
}
