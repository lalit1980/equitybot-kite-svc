package com.equitybot.trade.db.mongodb.order.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Position")
public class Position{
	@Id
	private String id;
	private String positionType;
	private List<PositionData> positionData;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPositionType() {
		return positionType;
	}
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	public List<PositionData> getPositionData() {
		return positionData;
	}
	public void setPositionData(List<PositionData> positionData) {
		this.positionData = positionData;
	}
	@Override
	public String toString() {
		return "Position [id=" + id + ", positionType=" + positionType + ", positionData=" + positionData + "]";
	}
}
