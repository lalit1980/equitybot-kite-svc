package com.equitybot.trade.ws.service.kite;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.equitybot.trade.converter.CustomTickBarList;
import com.equitybot.trade.db.mongodb.instrument.domain.InstrumentModel;
import com.equitybot.trade.db.mongodb.instrument.repository.InstrumentRepository;
import com.equitybot.trade.db.mongodb.order.domain.OrderRequestDTO;
import com.equitybot.trade.db.mongodb.order.domain.OrderResponse;
import com.equitybot.trade.db.mongodb.order.domain.TrailStopLossResponse;
import com.equitybot.trade.db.mongodb.order.repository.OrderResponseRepository;
import com.equitybot.trade.db.mongodb.order.repository.TrailStopLossRepository;
import com.equitybot.trade.db.mongodb.property.domain.KiteProperty;
import com.equitybot.trade.db.mongodb.property.repository.PropertyRepository;
import com.equitybot.trade.db.mongodb.tick.repository.TickRepository;
import com.equitybot.trade.ignite.configs.IgniteConfig;
import com.equitybot.trade.util.DateFormatUtil;
import com.equitybot.trade.util.TickSerializer;
import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.Depth;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Holding;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.MFHolding;
import com.zerodhatech.models.MFInstrument;
import com.zerodhatech.models.MFOrder;
import com.zerodhatech.models.MFSIP;
import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import com.zerodhatech.models.Position;
import com.zerodhatech.models.Profile;
import com.zerodhatech.models.Quote;
import com.zerodhatech.models.Tick;
import com.zerodhatech.models.Trade;
import com.zerodhatech.models.TriggerRange;
import com.zerodhatech.models.User;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnDisconnect;
import com.zerodhatech.ticker.OnError;
import com.zerodhatech.ticker.OnOrderUpdate;
import com.zerodhatech.ticker.OnTicks;

@Component
public class KiteConnectService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Value("${trailStopLossFeature}")
	private String trailStopLossFeature;
	
	private IgniteCache<Long, Double> cacheLastTradedPrice;
	private IgniteCache<Long, String> cacheTradeOrder;
	private IgniteCache<Long, Tick> cacheLatestTick;
	private IgniteCache<String, Double> cacheAvailableFund;
	private IgniteCache<Long, Double> cachePurchasedPrice;
	private IgniteCache<Long, Double> cacheMaxTrailStopLoss;
	//private IgniteCache<Long, Boolean> cacheTrailStopLossSignal;
	private IgniteCache<Long, Double> cacheTotalProfit;
	private IgniteCache<String, Double> cacheStopLoss;
	private IgniteCache<String, Integer> cacheQuantity;
	private IgniteCache<Long, Instrument> cacheInstrument;
	
	private boolean backTestFlag;

	@Autowired
	private CustomTickBarList customTickBarList;

	@Autowired
	TickRepository repository;

	@Autowired
	TickSerializer serializer;

	@Autowired
	IgniteConfig igniteConfig;

	private String userId;

	@Autowired
	PropertyRepository propertyRepository;

	@Autowired
	InstrumentRepository instrumentRepository;

	@Autowired
	OrderResponseRepository orderResponseRepository;
	
	@Autowired
	TrailStopLossRepository trailStopLossRepository;

	private List<KiteConnect> kiteSessionList = new ArrayList<KiteConnect>();

	private ArrayList<Tick> instrunentTicksData = new ArrayList<Tick>();

	public Profile getProfile(String userId, String requestToken) throws IOException, KiteException {
		KiteConnect kiteConnect = getKiteConnectSession(userId, requestToken);
		Profile profile = kiteConnect.getProfile();
		LOGGER.info(profile.userName);
		return profile;
	}

	public IgniteCache<Long, Double> getCacheLastTradedPrice() {
		return cacheLastTradedPrice;
	}

	public void setCacheLastTradedPrice(IgniteCache<Long, Double> cacheLastTradedPrice) {
		this.cacheLastTradedPrice = cacheLastTradedPrice;
	}

	public KiteConnectService() {

		CacheConfiguration<Long, Double> ccfg = new CacheConfiguration<Long, Double>("LastTradedPrice");
		this.cacheLastTradedPrice = igniteConfig.getInstance().getOrCreateCache(ccfg);

		CacheConfiguration<Long, String> ccfgOrderDetails = new CacheConfiguration<Long, String>("CachedTradeOrder");
		this.cacheTradeOrder = igniteConfig.getInstance().getOrCreateCache(ccfgOrderDetails);

		CacheConfiguration<Long, Tick> ccfgLatestTickParams = new CacheConfiguration<Long, Tick>("CachedLatestTick");
		this.cacheLatestTick = igniteConfig.getInstance().getOrCreateCache(ccfgLatestTickParams);

		CacheConfiguration<String, Double> ccfgCacheAvailableFund = new CacheConfiguration<String, Double>("CacheAvailableFund");
		this.cacheAvailableFund = igniteConfig.getInstance().getOrCreateCache(ccfgCacheAvailableFund);
		
		CacheConfiguration<Long, Double> ccfgCachePurchasedPrice = new CacheConfiguration<Long, Double>("CachePurchasedPrice");
		this.cachePurchasedPrice = igniteConfig.getInstance().getOrCreateCache(ccfgCachePurchasedPrice);
		
		CacheConfiguration<Long, Double> ccfgcacheMaxTrailStopLoss = new CacheConfiguration<Long, Double>("CacheMaxTrailStopLoss");
		this.cacheMaxTrailStopLoss = igniteConfig.getInstance().getOrCreateCache(ccfgcacheMaxTrailStopLoss);
		
		//CacheConfiguration<Long, Boolean> ccfgcacheTrailStopLossSignal = new CacheConfiguration<Long, Boolean>("CacheTrailStopLossSignal");
		//this.cacheTrailStopLossSignal = igniteConfig.getInstance().getOrCreateCache(ccfgcacheTrailStopLossSignal);
		
		 CacheConfiguration<Long, Double> ccfgcacheTotalProfit = new CacheConfiguration<Long, Double>("CacheTotalProfit");
	     this.cacheTotalProfit = igniteConfig.getInstance().getOrCreateCache(ccfgcacheTotalProfit);
	     
	     CacheConfiguration<Long, Instrument> ccfgcacheInstrument = new CacheConfiguration<Long, Instrument>("CacheInstrument");
		 this.cacheInstrument = igniteConfig.getInstance().getOrCreateCache(ccfgcacheInstrument);
		 
		 CacheConfiguration<String, Double> ccfgcStopLoss = new CacheConfiguration<String, Double>("CacheStopLoss");
		 this.cacheStopLoss = igniteConfig.getInstance().getOrCreateCache(ccfgcStopLoss);
			
		 CacheConfiguration<String, Integer> ccfgcQuantity = new CacheConfiguration<String, Integer>("CacheQuantity");
		 this.cacheQuantity = igniteConfig.getInstance().getOrCreateCache(ccfgcQuantity);
		
	     this.backTestFlag=false;
		
	}

	/** Gets Margin. */
	public double getMargins(String userId, String requestToken) throws KiteException, IOException {
		// Get margins returns margin model, you can pass equity or commodity as
		// arguments to get margins of respective segments.
		// Margins margins = kiteConnect.getMargins("equity");
		KiteConnect kiteConnect = getKiteConnectSession(userId, requestToken);
		double avialbleFund=0;
		//Margin margins = kiteConnect.getMargins(segment);
		Map<String, Margin> margins=kiteConnect.getMargins();
		for (Map.Entry<String, Margin> entry : margins.entrySet()) {
			if("equity".equals(entry.getKey())){
				avialbleFund=Double.parseDouble(entry.getValue().available.cash);
				LOGGER.info("Avaivle Fund: "+avialbleFund);
			}
		}
		return avialbleFund;
	}
	/** Get all positions. */
	public Map<String, List<Position>> getPositions(String userId, String requestToken) throws KiteException, IOException {
		// Get positions returns position model which contains list of positions.
		KiteConnect kiteConnect = getKiteConnectSession(userId, requestToken);
		Map<String, List<Position>> position = kiteConnect.getPositions();
		LOGGER.info("" + position.get("net").size());
		LOGGER.info("" + position.get("day").size());
		return position;
		
	}
	/** Get orderbook. */
	public List<Order> getOrders(String userId, String requestToken) throws KiteException, IOException {
		// Get orders returns order model which will have list of orders inside, which
		// can be accessed as follows,
		LOGGER.info("Logged in User ID: " + userId);
		KiteConnect kiteConnect = getKiteConnectSession(userId, requestToken);
		List<Order> orders = kiteConnect.getOrders();
		for (int i = 0; i < orders.size(); i++) {
			LOGGER.info(orders.get(i).tradingSymbol + " " + orders.get(i).orderId + " " + orders.get(i).parentOrderId
					+ " " + orders.get(i).orderType + " " + orders.get(i).averagePrice + " "
					+ orders.get(i).exchangeTimestamp + orders.get(i).status);
		}
		LOGGER.info("list of orders size is " + orders.size());
		return orders;
	}

	/** Get order details */
	public List<Order> getOrder(String userId, String requestToken, String orderId) throws KiteException, IOException {
		KiteConnect kiteConnect = getKiteConnectSession(userId, requestToken);
		List<Order> orders = kiteConnect.getOrderHistory(orderId);
		for (int i = 0; i < orders.size(); i++) {
			LOGGER.info(orders.get(i).orderId + " " + orders.get(i).status);
		}
		LOGGER.info("list size is " + orders.size());
		return orders;
	}
	
	/** Place order. */
	public Order placeOrder(OrderRequestDTO tradeRequest) {
		LOGGER.info("Place Order Service: "+ tradeRequest.getTradingsymbol()+" Order Type: "+ tradeRequest.getTransactionType()+" Price: "+cacheLastTradedPrice.get(tradeRequest.getInstrumentToken())+" Tag: "+tradeRequest.getTag());
			KiteConnect kiteConnect;
			int quantity;
			try {
				LOGGER.info("Logged in User ID: " + tradeRequest.getUserId());
				kiteConnect = getKiteConnectSession(tradeRequest.getUserId(), tradeRequest.getRequestToken());
				OrderParams orderParams = new OrderParams();
				InstrumentModel instrument = instrumentRepository.findByInstrumentToken(String.valueOf(tradeRequest.getInstrumentToken()));
				quantity=this.cacheQuantity.get(cacheInstrument.get(instrument.getInstrumentToken()).getTradingsymbol());
				if(quantity==0) {
					quantity=2;
				}
				orderParams.quantity = instrument.getLot_size()* quantity;
				orderParams.orderType = Constants.ORDER_TYPE_MARKET;
				orderParams.tradingsymbol = instrument.getTradingSymbol();
				orderParams.product = Constants.PRODUCT_MIS;
				orderParams.exchange = Constants.EXCHANGE_NFO;
				orderParams.transactionType = tradeRequest.getTransactionType().toUpperCase();
				orderParams.validity = Constants.VALIDITY_DAY;
				orderParams.tag = tradeRequest.getTag(); // tag is optional and it cannot be more than 8 characters
				Order order=null;
				if(tradeRequest.getTransactionType().equalsIgnoreCase("Buy")) {
					cachePurchasedPrice.put(tradeRequest.getInstrumentToken(), cacheLastTradedPrice.get(instrument.getInstrumentToken()));
					order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
				}
				if(tradeRequest.getTransactionType().toUpperCase().equalsIgnoreCase("Sell") && cacheTradeOrder.containsKey(instrument.getInstrumentToken())) {
					LOGGER.info("Placing sell order... "+tradeRequest.getInstrumentToken());
					order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
				}
				checkOrderStatus(tradeRequest,order);
				return order;
				

			} catch (JSONException | IOException | KiteException e) {
				
				e.printStackTrace();
			}
		return null;

	}
	private void checkOrderStatus(OrderRequestDTO tradeRequest, Order order) throws KiteException, IOException {
		if(order!=null) {
			List<Order> orderList = getOrder(tradeRequest.getUserId(), tradeRequest.getRequestToken(), order.orderId);
			if (orderList != null && orderList.size() > 0) {
				for (Order order2 : orderList) {
					OrderResponse result = convertOrderResponse(order2, tradeRequest);
					
					if (order2.orderId.equalsIgnoreCase(order.orderId) && order2 != null && order2.transactionType.equalsIgnoreCase("Buy") && order2.status.equalsIgnoreCase("Complete")) {
						cacheTradeOrder.put(tradeRequest.getInstrumentToken(), Constants.TRANSACTION_TYPE_BUY);
						orderResponseRepository.save(result);
					} else if (order2.orderId.equalsIgnoreCase(order.orderId) && order2 != null && order2.transactionType.equalsIgnoreCase("Sell") && order2.status.equalsIgnoreCase("Complete")) {
						cacheTradeOrder.remove(tradeRequest.getInstrumentToken());
						orderResponseRepository.save(result);
					}else {
						LOGGER.info("No order id matched......");
					}
					
				}
			}
		}else {
			LOGGER.info("Place order send null to checkOrderStatus");
		}
		
		
	}
	protected void calculateTrailStoLoss(long instrumentToken) {
		if(this.cachePurchasedPrice!=null && this.cachePurchasedPrice.get(instrumentToken)!=null) {
			double stopLoss=0.0;
			double stopLossLimit=this.cacheMaxTrailStopLoss.get(instrumentToken);
			double purcahsePrice=this.cachePurchasedPrice.get(instrumentToken);
			//double stopLossDistance=5;
			stopLoss=this.cacheStopLoss.get(cacheInstrument.get(instrumentToken).getTradingsymbol());
			if(stopLoss<=0) {
				stopLoss=20;
			}
			double stopLossDistance=((purcahsePrice*stopLoss)/100);
			double currentPrice=this.cacheLastTradedPrice.get(instrumentToken);
			if (stopLossLimit==0) {
	            stopLossLimit = purcahsePrice-stopLossDistance;
	        }
	        double currentValue = currentPrice;
	        double referenceValue = stopLossLimit+stopLossDistance;
	        if (currentValue>referenceValue) {
	            stopLossLimit = currentValue-stopLossDistance;
	        }
	        if(currentPrice<=stopLossLimit) {
	        	OrderRequestDTO tradeRequest=new OrderRequestDTO();
	        	tradeRequest.setUserId("XS2241");
	        	tradeRequest.setInstrumentToken(instrumentToken);
	        	tradeRequest.setTransactionType("Sell");
	        	tradeRequest.setTradingsymbol(this.cacheInstrument.get(instrumentToken).getTradingsymbol());
	        	tradeRequest.setTag("TrailST");
	        	this.cacheMaxTrailStopLoss.put(instrumentToken, 0.0);
	        	LOGGER.info("King got fucked Stop Loss got hit for stock: "+this.cacheInstrument.get(instrumentToken).getTradingsymbol()+" Purchase Price: "+ purcahsePrice+" stopLossLimit:"+stopLossLimit+" stopLossDistance: "+stopLossDistance+" currentPrice:"+currentPrice);
	        	if(isBackTestFlag()) {
	        		LOGGER.info("Inside Trail Stop Loss Hit to place Mock sell order: ");
	        		placeMockOrder(tradeRequest);
	    		}else {
	    			LOGGER.info("Inside Trail Stop Loss Hit to place actual sell order: ");
	    			placeOrder(tradeRequest);
	    		}
	        	
	        }else{
	        	LOGGER.info("King Chutiye won't sell stock: "+this.cacheInstrument.get(instrumentToken).getTradingsymbol()+" Purchase Price: "+ purcahsePrice+" stopLossLimit:"+stopLossLimit+" stopLossDistance: "+stopLossDistance+" currentPrice:"+currentPrice);
	        	this.cacheMaxTrailStopLoss.put(instrumentToken, stopLossLimit);
	        }
	        
		}else {
			LOGGER.info("Purchase price is null");
		}
    }
	/*
	public TrailStopLossResponse convertToTrailDataObj(long instrumentToken,Order order) {
		TrailStopLossResponse obj=null;
		if(order.transactionType.equalsIgnoreCase("Buy")) {
			obj=new TrailStopLossResponse();
			obj.setId(order.orderId);
			obj.setInstrumentToken(instrumentToken);
			obj.setTradingSymbol(order.tradingSymbol);
			obj.setType(order.transactionType);
			obj.setBuyPrice(this.cachePurchasedPrice.get(instrumentToken));
			obj.setSellPrice(0.0);
			obj.setBuyTransactionTime(DateFormatUtil.getCurrentISTTime(order.exchangeTimestamp));
			obj.setBuyTransactionTime(null);
			obj.setProfitAndLoss(0.0);
			obj.setTotalProfitLoss(0.0);
		}else {
			obj=new TrailStopLossResponse();
			obj.setId(order.orderId);
			obj.setInstrumentToken(instrumentToken);
			obj.setTradingSymbol(order.tradingSymbol);
			obj.setType(order.transactionType);
			obj.setBuyPrice(this.cachePurchasedPrice.get(instrumentToken));
			obj.setSellPrice(Double.parseDouble(order.averagePrice));
			obj.setBuyTransactionTime(DateFormatUtil.getCurrentISTTime(order.exchangeTimestamp));
			obj.setBuyTransactionTime(null);
			obj.setProfitAndLoss(Double.parseDouble(order.averagePrice)-this.cachePurchasedPrice.get(instrumentToken));
			obj.setTotalProfitLoss(0.0);
		}
		return obj;
	}*/
	
	public Order placeMockOrder(OrderRequestDTO tradeRequest) {
		LOGGER.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Processing Mock Trade Order in Kite $$$$$$$$$$$ "+ tradeRequest.getTradingsymbol()+" Order Type: "+ tradeRequest.getTransactionType()+" Price: "+cacheLastTradedPrice.get(tradeRequest.getInstrumentToken())+" Tag: "+tradeRequest.getTag());
		KiteConnect kiteConnect=null;
		int quantity;
		try {
			kiteConnect = getKiteConnectSession(tradeRequest.getUserId(), tradeRequest.getRequestToken());
			OrderParams orderParams = new OrderParams();
			InstrumentModel instrument = instrumentRepository.findByInstrumentToken(String.valueOf(tradeRequest.getInstrumentToken()));
			quantity=this.cacheQuantity.get(cacheInstrument.get(instrument.getInstrumentToken()).getTradingsymbol());
			if(quantity==0) {
				quantity=2;
			}
				orderParams.quantity = instrument.getLot_size()*quantity;
				orderParams.orderType = Constants.ORDER_TYPE_MARKET;
				orderParams.tradingsymbol = instrument.getTradingSymbol();
				orderParams.product = Constants.PRODUCT_MIS;
				orderParams.exchange = Constants.EXCHANGE_NFO;
				orderParams.transactionType = tradeRequest.getTransactionType().toUpperCase();
				
				orderParams.validity = Constants.VALIDITY_DAY;
				orderParams.tag = tradeRequest.getTag(); // tag is optional and it cannot be more than 8 characters
				Order order=null;
				
				if(tradeRequest.getTransactionType().equalsIgnoreCase("Buy")) {
					cachePurchasedPrice.put(tradeRequest.getInstrumentToken(), cacheLastTradedPrice.get(instrument.getInstrumentToken()));
					order=new Order();
					order.exchangeOrderId="1400000006412993";
					order.disclosedQuantity="0";
					order.validity="Day";
					order.tradingSymbol=instrument.getTradingSymbol();
					order.orderVariety=Constants.VARIETY_REGULAR;
					order.userId=tradeRequest.getUserId();
					order.orderType=orderParams.orderType;
					order.triggerPrice="0.0";
					order.statusMessage="Mock Test Success";
					order.price="0.0";
					order.status="Complete";
					order.product=orderParams.product;
					order.accountId=tradeRequest.getUserId();
					order.exchange=orderParams.exchange;
					order.orderId=UUID.randomUUID().toString();
					order.symbol="";
					order.pendingQuantity="0.0";
					order.orderTimestamp=new Date();
					order.exchangeTimestamp=new Date();
					order.averagePrice=""+cacheLastTradedPrice.get(instrument.getInstrumentToken());
					order.transactionType=orderParams.transactionType;
					order.filledQuantity=""+orderParams.quantity;
					order.parentOrderId="";
					order.tag=tradeRequest.getTag();
					cacheTradeOrder.put(tradeRequest.getInstrumentToken(), Constants.TRANSACTION_TYPE_BUY);
					//TrailStopLossResponse data=convertToTrailDataObj(instrument.getInstrumentToken(), order);
					//trailStopLossRepository.save(data);
					mockCheckOrderStatus(tradeRequest,order);
				}
				if(tradeRequest.getTransactionType().toUpperCase().equalsIgnoreCase("Sell") && cacheTradeOrder.containsKey(instrument.getInstrumentToken())) {
						LOGGER.info("Placing sell order overidding super trend... "+tradeRequest.getInstrumentToken());
						order=new Order();
						order.exchangeOrderId="1400000006412993";
						order.disclosedQuantity="0";
						order.validity="Day";
						order.tradingSymbol=instrument.getTradingSymbol();
						order.orderVariety=Constants.VARIETY_REGULAR;
						order.userId=tradeRequest.getUserId();
						order.orderType=orderParams.orderType;
						order.triggerPrice="0.0";
						order.statusMessage="Mock Test Success";
						order.price="0.0";
						order.status="Complete";
						order.product=orderParams.product;
						order.accountId=tradeRequest.getUserId();
						order.exchange=orderParams.exchange;
						order.orderId=UUID.randomUUID().toString();
						order.symbol="";
						order.pendingQuantity="0.0";
						order.orderTimestamp=new Date();
						order.exchangeTimestamp=new Date();
						order.averagePrice=""+cacheLastTradedPrice.get(instrument.getInstrumentToken());
						order.transactionType=orderParams.transactionType;
						order.filledQuantity=""+orderParams.quantity;
						order.parentOrderId="";
						order.tag=tradeRequest.getTag();
						//TrailStopLossResponse data=convertToTrailDataObj(instrument.getInstrumentToken(), order);
						//trailStopLossRepository.save(data);
						cacheTradeOrder.remove(tradeRequest.getInstrumentToken());
						mockCheckOrderStatus(tradeRequest,order);
				}else {
					LOGGER.info("Order Sold by Trail stop loss.......");
				}
				
				return order;
			

		} catch (JSONException | IOException | KiteException e) {
			
			e.printStackTrace();
		}
	return null;

	}
	private void mockCheckOrderStatus(OrderRequestDTO tradeRequest, Order order) throws KiteException, IOException {
		OrderResponse result = convertOrderResponse(order, tradeRequest);
		orderResponseRepository.save(result);
		if( tradeRequest.getTransactionType().equalsIgnoreCase("Buy")) {
			cacheTradeOrder.put(tradeRequest.getInstrumentToken(), Constants.TRANSACTION_TYPE_BUY);
		} else if(tradeRequest.getTransactionType().equalsIgnoreCase("Sell")) {
			cacheTradeOrder.remove(tradeRequest.getInstrumentToken());
		}
	}
	
	
	/** Place bracket order. */
	public Order placeBracketOrder(OrderRequestDTO tradeRequest){
		/**
		 * Bracket order:- following is example param for bracket order*
		 * trailing_stoploss and stoploss_value are points and not tick or price
		 */
		LOGGER.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Processing Bracket Order in Kite$$$$$$$$$$$ "+ tradeRequest.toString());
		try {
		KiteConnect kiteConnect = getKiteConnectSession(tradeRequest.getUserId(), tradeRequest.getRequestToken());
		OrderParams orderParams = new OrderParams();
		InstrumentModel instrumentList = instrumentRepository
				.findByInstrumentToken(String.valueOf(tradeRequest.getInstrumentToken()));
		int lot_size = instrumentList.getLot_size();
		int quantity = tradeRequest.getQuantity();
		if (quantity % lot_size == 0 && kiteConnect != null) {
			
		}
		double lastTradedPrice=this.cacheLastTradedPrice.get(tradeRequest.getInstrumentToken());
		orderParams.quantity = 160;
		orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
		orderParams.price = lastTradedPrice+.5;
		orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
		orderParams.tradingsymbol = instrumentList.getTradingSymbol();
		orderParams.trailingStoploss = 1.0;
		orderParams.stoploss = 5.0;
		orderParams.exchange = Constants.EXCHANGE_NSE;
		orderParams.validity = Constants.VALIDITY_DAY;
		orderParams.squareoff = this.cacheLastTradedPrice.get(tradeRequest.getInstrumentToken())+200;
		orderParams.product = Constants.PRODUCT_MIS;
		Order order10 = kiteConnect.placeOrder(orderParams, Constants.VARIETY_BO);
		System.out.println(order10.orderId);
		checkOrderStatus(tradeRequest,order10);
		return order10;

		} catch (JSONException | IOException | KiteException e) {
			
			e.printStackTrace();
		}
		return null;
	}

	
	/** Modify order. */
	public Order modifyOrder(OrderRequestDTO tradeRequest) throws KiteException, IOException {
		// Order modify request will return order model which will contain only
		// order_id.
		boolean statusFlag = false;
		int ctr = 0;
		while (!statusFlag) {

			KiteConnect kiteConnect;
			try {
				kiteConnect = getKiteConnectSession(tradeRequest.getUserId(), tradeRequest.getRequestToken());
				OrderParams orderParams = new OrderParams();
				orderParams.quantity = tradeRequest.getQuantity();
				orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
				orderParams.price = tradeRequest.getPrice();
				orderParams.transactionType = tradeRequest.getTransactionType().toUpperCase();
				InstrumentModel instrumentList = instrumentRepository
						.findByInstrumentToken(String.valueOf(tradeRequest.getInstrumentToken()));
				orderParams.tradingsymbol = instrumentList.getTradingSymbol();
				orderParams.trailingStoploss = tradeRequest.getTrailingStopLossPrice();
				orderParams.stoploss = tradeRequest.getStopLossPrice();
				orderParams.exchange = Constants.EXCHANGE_NSE;
				orderParams.validity = Constants.VALIDITY_DAY;
				orderParams.squareoff = tradeRequest.getTargetPrice();
				orderParams.product = Constants.PRODUCT_MIS;
				Order order10 = kiteConnect.placeOrder(orderParams, Constants.VARIETY_BO);
				LOGGER.info(order10.orderId);
				if (tradeRequest.getTransactionType().equalsIgnoreCase("Buy") && order10.orderId != null) {
					this.getCacheTradeOrder().put(tradeRequest.getInstrumentToken(), Constants.TRANSACTION_TYPE_BUY);
				} else if (tradeRequest.getTransactionType().equalsIgnoreCase("Sell") && order10.orderId != null) {
					if (this.getCacheTradeOrder().containsKey(tradeRequest.getInstrumentToken())) {
						this.getCacheTradeOrder().remove(tradeRequest.getInstrumentToken());
					}
				}
				return order10;
			} catch (JSONException | IOException | KiteException e) {
				// TODO Auto-generated catch block
				// Invoke Naresh Ji's Service Ji
				if (ctr > 3) {
					// TODO Implementation need to be done here throw custom exception
				}
				ctr++;
				e.printStackTrace();
			}
		}
		return null;
	}

	/** Cancel an order */
	public void cancelOrder(OrderRequestDTO tradeRequest) throws KiteException, IOException {
		boolean statusFlag = false;
		int ctr = 0;
		while (!statusFlag) {

			KiteConnect kiteConnect;
			try {
				kiteConnect = getKiteConnectSession(tradeRequest.getUserId(), tradeRequest.getRequestToken());
				Order order2 = kiteConnect.cancelOrder(tradeRequest.getOrderId(), tradeRequest.getVariety());
				LOGGER.info(order2.orderId);
			} catch (JSONException | IOException | KiteException e) {
				// TODO Auto-generated catch block
				// Invoke Naresh Ji's Service Ji
				if (ctr > 3) {
					// TODO Implementation need to be done here throw custom exception

				}
				ctr++;
				e.printStackTrace();
			}
		}

	}

	

	/** Place cover order. */
	public void placeCoverOrder(KiteConnect kiteConnect) throws KiteException, IOException {
		/**
		 * Cover Order:- following is an example param for the cover order key: quantity
		 * value: 1 key: price value: 0 key: transaction_type value: BUY key:
		 * tradingsymbol value: HINDALCO key: exchange value: NSE key: validity value:
		 * DAY key: trigger_price value: 157 key: order_type value: MARKET key: variety
		 * value: co key: product value: MIS
		 */
		OrderParams orderParams = new OrderParams();
		orderParams.price = 0.0;
		orderParams.quantity = 1;
		orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
		orderParams.orderType = Constants.ORDER_TYPE_MARKET;
		orderParams.tradingsymbol = "SOUTHBANK";
		orderParams.exchange = Constants.EXCHANGE_NSE;
		orderParams.validity = Constants.VALIDITY_DAY;
		orderParams.triggerPrice = 30.5;
		orderParams.product = Constants.PRODUCT_MIS;

		Order order11 = kiteConnect.placeOrder(orderParams, Constants.VARIETY_CO);
		System.out.println(order11.orderId);
	}

	/** Get trigger range. */
	public void getTriggerRange(KiteConnect kiteConnect) throws KiteException, IOException {
		// You need to send transaction_type, exchange and tradingsymbol to get trigger
		// range.
		String[] instruments = { "BSE:INFY", "NSE:APOLLOTYRE", "NSE:SBIN" };
		Map<String, TriggerRange> triggerRangeMap = kiteConnect.getTriggerRange(instruments,
				Constants.TRANSACTION_TYPE_BUY);
		LOGGER.info("" + triggerRangeMap.get("NSE:SBIN").lower);
		LOGGER.info("" + triggerRangeMap.get("NSE:APOLLOTYRE").upper);
		LOGGER.info("" + triggerRangeMap.get("BSE:INFY").percentage);
	}

	/** Get tradebook */
	public void getTrades(KiteConnect kiteConnect) throws KiteException, IOException {
		// Returns tradebook.
		List<Trade> trades = kiteConnect.getTrades();
		for (int i = 0; i < trades.size(); i++) {
			LOGGER.info(trades.get(i).tradingSymbol + " " + trades.size());
		}
		LOGGER.info("" + trades.size());
	}

	/** Get trades for an order. */
	public void getTradesWithOrderId(KiteConnect kiteConnect) throws KiteException, IOException {
		// Returns trades for the given order.
		List<Trade> trades = kiteConnect.getOrderTrades("180111000561605");
		LOGGER.info("" + trades.size());
	}

	/** Modify first leg bracket order. */
	public void modifyFirstLegBo(KiteConnect kiteConnect) throws KiteException, IOException {
		OrderParams orderParams = new OrderParams();
		orderParams.quantity = 1;
		orderParams.price = 31.0;
		orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
		orderParams.tradingsymbol = "SOUTHBANK";
		orderParams.exchange = Constants.EXCHANGE_NSE;
		orderParams.validity = Constants.VALIDITY_DAY;
		orderParams.product = Constants.PRODUCT_MIS;
		orderParams.tag = "myTag";
		orderParams.triggerPrice = 0.0;

		Order order = kiteConnect.modifyOrder("180116000798058", orderParams, Constants.VARIETY_BO);
		LOGGER.info(order.orderId);
	}

	public void modifySecondLegBoSLM(KiteConnect kiteConnect) throws KiteException, IOException {

		OrderParams orderParams = new OrderParams();
		orderParams.parentOrderId = "180116000798058";
		orderParams.tradingsymbol = "SOUTHBANK";
		orderParams.exchange = Constants.EXCHANGE_NSE;
		orderParams.product = Constants.PRODUCT_MIS;
		orderParams.validity = Constants.VALIDITY_DAY;
		orderParams.triggerPrice = 30.5;
		orderParams.price = 0.0;
		orderParams.orderType = Constants.ORDER_TYPE_SLM;
		orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;

		Order order = kiteConnect.modifyOrder("180116000812154", orderParams, Constants.VARIETY_BO);
		LOGGER.info(order.orderId);
	}

	public void modifySecondLegBoLIMIT(KiteConnect kiteConnect) throws KiteException, IOException {
		OrderParams orderParams = new OrderParams();
		orderParams.parentOrderId = "180116000798058";
		orderParams.tradingsymbol = "SOUTHBANK";
		orderParams.exchange = Constants.EXCHANGE_NSE;
		orderParams.quantity = 1;
		orderParams.product = Constants.PRODUCT_MIS;
		orderParams.validity = Constants.VALIDITY_DAY;
		orderParams.price = 35.3;
		orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
		orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;

		Order order = kiteConnect.modifyOrder("180116000812153", orderParams, Constants.VARIETY_BO);
		LOGGER.info(order.orderId);
	}

	public void exitBracketOrder(KiteConnect kiteConnect) throws KiteException, IOException {
		Order order = kiteConnect.cancelOrder("180116000812153", "180116000798058", Constants.VARIETY_BO);
		LOGGER.info(order.orderId);
	}

	

	/** Get holdings. */
	public void getHoldings(KiteConnect kiteConnect) throws KiteException, IOException {
		// Get holdings returns holdings model which contains list of holdings.
		List<Holding> holdings = kiteConnect.getHoldings();
		LOGGER.info("" + holdings.size());
	}

	/** Converts position */
	public void converPosition(KiteConnect kiteConnect) throws KiteException, IOException {
		// Modify product can be used to change MIS to NRML(CNC) or NRML(CNC) to MIS.
		JSONObject jsonObject6 = kiteConnect.convertPosition("ASHOKLEY", Constants.EXCHANGE_NSE,
				Constants.TRANSACTION_TYPE_BUY, Constants.POSITION_DAY, Constants.PRODUCT_MIS, Constants.PRODUCT_CNC,
				1);
		LOGGER.info("" + jsonObject6);
	}

	/** Get instruments for the desired exchange. */
	public void getInstrumentsForExchange(KiteConnect kiteConnect) throws KiteException, IOException {
		// Get instruments for an exchange.
		List<Instrument> nseInstruments = kiteConnect.getInstruments("CDS");
		LOGGER.info("" + nseInstruments.size());
	}

	/** Get quote for a scrip. */
	public void getQuote(KiteConnect kiteConnect) throws KiteException, IOException {
		// Get quotes returns quote for desired tradingsymbol.
		String[] instruments = { "256265", "BSE:INFY", "NSE:APOLLOTYRE", "NSE:NIFTY 50" };
		Map<String, Quote> quotes = kiteConnect.getQuote(instruments);
		LOGGER.info(quotes.get("NSE:APOLLOTYRE").instrumentToken + "");
		LOGGER.info(quotes.get("NSE:APOLLOTYRE").oi + "");
		LOGGER.info("" + quotes.get("NSE:APOLLOTYRE").depth.buy.get(4).getPrice());
		LOGGER.info("" + quotes.get("NSE:APOLLOTYRE").timestamp);
	}

	/*
	 * Get ohlc and lastprice for multiple instruments at once. Users can either
	 * pass exchange with tradingsymbol or instrument token only. For example
	 * {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}
	 */
	public void getOHLC(KiteConnect kiteConnect) throws KiteException, IOException {
		String[] instruments = { "256265", "BSE:INFY", "NSE:INFY", "NSE:NIFTY 50" };
		LOGGER.info("" + kiteConnect.getOHLC(instruments).get("256265").lastPrice);
		LOGGER.info("" + kiteConnect.getOHLC(instruments).get("NSE:NIFTY 50").ohlc.open);
	}

	/**
	 * Get last price for multiple instruments at once. USers can either pass
	 * exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY
	 * 50, BSE:SENSEX} or {256265, 265}
	 */
	public void getLTP(KiteConnect kiteConnect) throws KiteException, IOException {
		String[] instruments = { "256265", "BSE:INFY", "NSE:INFY", "NSE:NIFTY 50" };
		LOGGER.info("" + kiteConnect.getLTP(instruments).get("256265").lastPrice);
	}

	/** Get historical data for an instrument. */
	public ArrayList<List<Tick>> getHistoricalData(KiteConnect kiteConnect, ArrayList<Long> InstrumentToken,
			Date fromDate, Date toDate, String interval, boolean flag) throws KiteException, IOException {
		/**
		 * Get historical data dump, requires from and to date, intrument token,
		 * interval, continuous (for expired F&O contracts) returns historical data
		 * object which will have list of historical data inside the object.
		 */
		ArrayList<List<Tick>> tickList = new ArrayList<List<Tick>>();
		for (Iterator<Long> iterator = InstrumentToken.iterator(); iterator.hasNext();) {
			Long instrumentToken = iterator.next();
			HistoricalData historicalData = kiteConnect.getHistoricalData(fromDate, toDate,
					String.valueOf(instrumentToken), interval, false);
			try {
				tickList.add(DateFormatUtil.getTickList(historicalData, instrumentToken));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		return tickList;
	}

	/** Retrieve mf instrument dump */
	public void getMFInstruments(KiteConnect kiteConnect) throws KiteException, IOException {
		List<MFInstrument> mfList = kiteConnect.getMFInstruments();
		LOGGER.info("size of mf instrument list: " + mfList.size());
	}

	/* Get all mutualfunds holdings */
	public void getMFHoldings(KiteConnect kiteConnect) throws KiteException, IOException {
		List<MFHolding> MFHoldings = kiteConnect.getMFHoldings();
		LOGGER.info("mf holdings " + MFHoldings.size());
	}

	/* Place a mutualfunds order */
	public void placeMFOrder(KiteConnect kiteConnect) throws KiteException, IOException {
		LOGGER.info("place order: "
				+ kiteConnect.placeMFOrder("INF174K01LS2", Constants.TRANSACTION_TYPE_BUY, 5000, 0, "myTag").orderId);
	}

	/* cancel mutualfunds order */
	public void cancelMFOrder(KiteConnect kiteConnect) throws KiteException, IOException {
		kiteConnect.cancelMFOrder("668604240868430");
		LOGGER.info("cancel order successful");
	}

	/* retrieve all mutualfunds orders */
	public void getMFOrders(KiteConnect kiteConnect) throws KiteException, IOException {
		List<MFOrder> MFOrders = kiteConnect.getMFOrders();
		LOGGER.info("mf orders: " + MFOrders.size());
	}

	/* retrieve individual mutualfunds order */
	public void getMFOrder(KiteConnect kiteConnect) throws KiteException, IOException {
		LOGGER.info("mf order: " + kiteConnect.getMFOrder("106580291331583").tradingsymbol);
	}

	/* place mutualfunds sip */
	public void placeMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
		LOGGER.info("mf place sip: " + kiteConnect.placeMFSIP("INF174K01LS2", "monthly", 1, -1, 5000, 1000).sipId);
	}

	/* modify a mutual fund sip */
	public void modifyMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
		kiteConnect.modifyMFSIP("weekly", 1, 5, 1000, "active", "504341441825418");
	}

	/* cancel a mutualfunds sip */
	public void cancelMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
		kiteConnect.cancelMFSIP("504341441825418");
		LOGGER.info("cancel sip successful");
	}

	/* retrieve all mutualfunds sip */
	public void getMFSIPS(KiteConnect kiteConnect) throws KiteException, IOException {
		List<MFSIP> sips = kiteConnect.getMFSIPs();
		LOGGER.info("mf sips: " + sips.size());
	}

	/* retrieve individual mutualfunds sip */
	public void getMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
		LOGGER.info("mf sip: " + kiteConnect.getMFSIP("291156521960679").instalments);
	}

	/** Logout user. */
	public void logout(KiteConnect kiteConnect) throws KiteException, IOException {
		/** Logout user and kill session. */
		JSONObject jsonObject10 = kiteConnect.logout();
		LOGGER.info("" + jsonObject10);
	}

	/** Get all instruments that can be traded using kite connect. */
	public List<Instrument> getAllInstruments(KiteConnect kiteConnect) throws KiteException, IOException {
		return kiteConnect.getInstruments();
	}

	public void tickerUsage(KiteConnect kiteConnect, final ArrayList<Long> tokens)
			throws IOException, WebSocketException, KiteException {
		final KiteTicker tickerProvider = new KiteTicker(kiteConnect.getAccessToken(), kiteConnect.getApiKey());
		tickerProvider.setOnConnectedListener(new OnConnect() {
			
			@Override
			public void onConnected() {
				tickerProvider.subscribe(tokens);
				tickerProvider.setMode(tokens, KiteTicker.modeFull);
			}
		});

		tickerProvider.setOnDisconnectedListener(new OnDisconnect() {
			@Override
			public void onDisconnected() {
				// your code goes here
			}
		});

		/** Set listener to get order updates. */
		tickerProvider.setOnOrderUpdateListener(new OnOrderUpdate() {
			@Override
			public void onOrderUpdate(Order order) {
				LOGGER.info("Stok: "+order.tradingSymbol+" order update " + order.orderId+"Order Price: "+order.price+" Order Average Price: "+order.averagePrice+" Order Status: "+order.status+" Order Tag: "+order.tag+" Order Type: "+order.transactionType);
			}
		});

		/** Set error listener to listen to errors. */
		tickerProvider.setOnErrorListener(new OnError() {
			@Override
			public void onError(Exception exception) {
				// handle here.
			}

			@Override
			public void onError(KiteException kiteException) {
				// handle here.
			}
		});
		tickerProvider.setOnTickerArrivalListener(new OnTicks() {
			@Override
			public void onTicks(ArrayList<Tick> ticks) {
				backTestFlag=false;
				if (ticks != null && ticks.size() > 0 && ticks.get(0).getMode() != null) {
					for(Tick tick :ticks) { 
						com.equitybot.trade.db.mongodb.tick.domain.Tick tickz = convertTickModel(tick);
						customTickBarList.addTick(tickz);
						cacheLatestTick.put(tick.getInstrumentToken(), tick);
						cacheLastTradedPrice.put(tick.getInstrumentToken(),tick.getLastTradedPrice());
						if(cacheTradeOrder!=null && cacheTradeOrder.containsKey(tick.getInstrumentToken())) {
							calculateTrailStoLoss(tick.getInstrumentToken());
						}
					}
				}
			}
		});
		tickerProvider.setTryReconnection(true);
		tickerProvider.setMaximumRetries(10);
		tickerProvider.setMaximumRetryInterval(30);
		tickerProvider.connect();
		boolean isConnected = tickerProvider.isConnectionOpen();
		LOGGER.info("Socket connection: " + isConnected);
		tickerProvider.setMode(tokens, KiteTicker.modeFull);
	}


	public KiteConnect getKiteConnectSession(String userId, String requestToken)
			throws JSONException, IOException, KiteException {
		KiteConnect kiteConnect = null;
		KiteProperty kitePropertyList = propertyRepository.findByUserId(userId);
		if (kitePropertyList != null && requestToken != null) {
			kitePropertyList.setRequestToken(requestToken);
			propertyRepository.save(kitePropertyList);
		}
		if (kiteSessionList != null && kiteSessionList.size() > 0) {
			kiteConnect = kiteSessionList.stream().filter(x -> userId.equals(x.getUserId())).findFirst().orElse(null);
			if (kiteConnect == null) {
				LOGGER.info("Kite Connect is Null");
			}

		} else {
			KiteProperty kiteProperty = propertyRepository.findByUserId(userId);
			LOGGER.info("Inside Else: " + kiteProperty.toString());
			kiteConnect = new KiteConnect(kiteProperty.getApiKey());
			kiteConnect.setUserId(kiteProperty.getUserId());
			kiteConnect.setEnableLogging(true);
			String url = kiteConnect.getLoginURL();
			kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
				@Override
				public void sessionExpired() {
					LOGGER.info("session expired");
				}
			});
			LOGGER.info("kiteProperty.getRequestToken(): " + kiteProperty.getRequestToken()
					+ " kiteProperty.getApiSecret(): " + kiteProperty.getApiSecret());
			User user = kiteConnect.generateSession(kiteProperty.getRequestToken(), kiteProperty.getApiSecret());
			kiteConnect.setAccessToken(user.accessToken);
			kiteConnect.setPublicToken(user.publicToken);
			kiteSessionList.add(kiteConnect);
			propertyRepository.updatePropertyByUserId(kiteProperty.getUserId(), kiteProperty.getRequestToken(),
					user.accessToken, user.publicToken);
		}
		return kiteConnect;
	}

	public ArrayList<Tick> getInstrunentTicksData() {
		return instrunentTicksData;
	}

	public void setInstrunentTicksData(ArrayList<Tick> instrunentTicksData) {
		this.instrunentTicksData = instrunentTicksData;
	}

	public List<KiteConnect> getKiteSessionList() {
		return kiteSessionList;
	}

	public void setKiteSessionList(List<KiteConnect> kiteSessionList) {
		this.kiteSessionList = kiteSessionList;
	}

	public ArrayList<List<Tick>> startBackTesting(KiteConnect kiteconnect, ArrayList<Long> instrumentTokens,
			Date fromDate, Date toDate, String interval, boolean continuous) throws IOException, KiteException {
		this.setBackTestFlag(true);
		ArrayList<List<Tick>> tickList = getHistoricalData(kiteconnect, instrumentTokens, fromDate, toDate, interval,
				continuous);
		if (tickList != null && tickList.size() > 0) {
			for (int i = 0; i < tickList.size(); i++) {
				if (tickList.get(i) != null && tickList.get(i).size() > 0) {
					List<Tick> list = tickList.get(i);
					for (int j = 0; j < list.size(); j++) {
						try {
							Tick tick = list.get(j);
							com.equitybot.trade.db.mongodb.tick.domain.Tick tickz = convertTickModel(tick);
							LOGGER.info("Back test: "+tickz.toString());
							customTickBarList.backTest(tickz);
							cacheLatestTick.put(tick.getInstrumentToken(), tick);
							cacheLastTradedPrice.put(tick.getInstrumentToken(),tick.getLastTradedPrice());
							if(trailStopLossFeature.equalsIgnoreCase("true") && cacheTradeOrder!=null && cacheTradeOrder.containsKey(tick.getInstrumentToken())) {
								calculateTrailStoLoss(tick.getInstrumentToken());
							}
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return tickList;
	}

	public IgniteCache<Long, Tick> getCacheLatestTick() {
		return cacheLatestTick;
	}

	public void setCacheLatestTick(IgniteCache<Long, Tick> cacheLatestTick) {
		this.cacheLatestTick = cacheLatestTick;
	}

	public IgniteCache<Long, String> getCacheTradeOrder() {
		return cacheTradeOrder;
	}

	public void setCacheTradeOrder(IgniteCache<Long, String> cacheTradeOrder) {
		this.cacheTradeOrder = cacheTradeOrder;
	}

	public com.equitybot.trade.db.mongodb.tick.domain.Tick convertTickModel(com.zerodhatech.models.Tick tick) {
		com.equitybot.trade.db.mongodb.tick.domain.Tick tickModel = null;
		if (tick != null) {
			tickModel = new com.equitybot.trade.db.mongodb.tick.domain.Tick();
			tickModel.setAverageTradePrice(tick.getAverageTradePrice());
			tickModel.setChange(tick.getChange());
			tickModel.setClosePrice(tick.getClosePrice());
			tickModel.setHighPrice(tick.getHighPrice());
			tickModel.setId(UUID.randomUUID().toString());
			tickModel.setInstrumentToken(tick.getInstrumentToken());
			tickModel.setLastTradedPrice(tick.getLastTradedPrice());
			tickModel.setLastTradedQuantity(tick.getLastTradedQuantity());
			tickModel.setLastTradedTime(tick.getLastTradedTime());
			tickModel.setLowPrice(tick.getLowPrice());
			tickModel.setMode(tick.getMode());
			tickModel.setOi(tick.getOi());
			tickModel.setOiDayHigh(tick.getOpenInterestDayHigh());
			tickModel.setOiDayLow(tick.getOpenInterestDayLow());
			tickModel.setOpenPrice(tick.getOpenPrice());
			tickModel.setTickTimestamp(tick.getTickTimestamp());
			tickModel.setTotalBuyQuantity(tick.getTotalBuyQuantity());
			tickModel.setTotalSellQuantity(tick.getTotalSellQuantity());
			tickModel.setTradable(tick.isTradable());
			tickModel.setVolumeTradedToday(tick.getVolumeTradedToday());
			Map<String, ArrayList<com.equitybot.trade.db.mongodb.tick.domain.Depth>> depth = new HashMap<String, ArrayList<com.equitybot.trade.db.mongodb.tick.domain.Depth>>();
			if (tick.getMarketDepth() != null && tick.getMarketDepth().size() > 0) {
				Map<String, ArrayList<Depth>> marketDepth = tick.getMarketDepth();
				marketDepth.forEach((k, v) -> {
					List<Depth> depthList = v;
					ArrayList<com.equitybot.trade.db.mongodb.tick.domain.Depth> mongoDepthList = new ArrayList<com.equitybot.trade.db.mongodb.tick.domain.Depth>();
					depthList.forEach(item -> {
						com.equitybot.trade.db.mongodb.tick.domain.Depth depthObj = new com.equitybot.trade.db.mongodb.tick.domain.Depth();
						depthObj.setId(UUID.randomUUID().toString());
						depthObj.setOrders(item.getOrders());
						depthObj.setPrice(item.getPrice());
						depthObj.setQuantity(item.getQuantity());
						mongoDepthList.add(depthObj);
					});
					depth.put(k, mongoDepthList);
				});
				tickModel.setDepth(depth);
			}

		}

		return tickModel;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public OrderResponse convertOrderResponse(Order order2, OrderRequestDTO tradeRequest) {

		OrderResponse response = new OrderResponse();
		response.setAccountId(order2.accountId);
		response.setAveragePrice(order2.averagePrice);
		response.setDisclosedQuantity(order2.disclosedQuantity);
		response.setExchange(order2.exchange);
		response.setExchangeOrderId(order2.exchangeOrderId);
		response.setExchangeTimestamp(DateFormatUtil.getCurrentISTTime(new Date()));
		response.setFilledQuantity(order2.filledQuantity);
		response.setInstrumentToken(tradeRequest.getInstrumentToken());
		response.setOrderId(order2.orderId);
		response.setOrderTimestamp(DateFormatUtil.getCurrentISTTime(new Date()));
		response.setOrderType(order2.orderType);
		response.setOrderVariety(order2.orderVariety);
		response.setParentOrderId(order2.parentOrderId);
		response.setPendingQuantity(order2.pendingQuantity);
		response.setPrice(order2.price);
		response.setProduct(order2.product);
		response.setQuantity(order2.quantity);
		response.setStatus(order2.status);
		response.setStatusMessage(order2.statusMessage);
		response.setSymbol(order2.symbol);
		response.setTag(order2.tag);
		response.setTradingSymbol(order2.tradingSymbol);
		response.setTransactionType(order2.transactionType);
		response.setTriggerPrice(order2.triggerPrice);
		response.setUserId(order2.userId);
		response.setValidity(order2.validity);
		return response;

	}

	public boolean isBackTestFlag() {
		return backTestFlag;
	}

	public void setBackTestFlag(boolean backTestFlag) {
		this.backTestFlag = backTestFlag;
	}

	public String getTrailStopLossFeature() {
		return trailStopLossFeature;
	}

	public void setTrailStopLossFeature(String trailStopLossFeature) {
		this.trailStopLossFeature = trailStopLossFeature;
	}
}
