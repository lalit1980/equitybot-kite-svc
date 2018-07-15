package com.equitybot.trade.ws.service.kite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.equitybot.trade.db.mongodb.instrument.domain.InstrumentModel;
import com.equitybot.trade.db.mongodb.instrument.repository.InstrumentRepository;
import com.equitybot.trade.db.mongodb.order.dto.OrderRequestDTO;
import com.equitybot.trade.db.mongodb.property.domain.KiteProperty;
import com.equitybot.trade.db.mongodb.property.repository.PropertyRepository;
import com.equitybot.trade.util.DateFormatUtil;
import com.google.gson.Gson;
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

	@Value("${spring.kafka.producer.topic-kite-tick}")
	private String tickProducerTopic;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private IgniteCache<Long, Double> cacheLastTradedPrice;
	private IgniteCache<Long, OrderParams> cacheOrderParams;
	private IgniteCache<Long, Tick> cacheLatestTick;

	@Autowired
	PropertyRepository propertyRepository;
	
	@Autowired
	InstrumentRepository instrumentRepository;
	
	private List<KiteConnect> kiteSessionList = new ArrayList<KiteConnect>();

	private ArrayList<Tick> instrunentTicksData = new ArrayList<Tick>();

	public void getProfile(KiteConnect kiteConnect) throws IOException, KiteException {
		Profile profile = kiteConnect.getProfile();
		LOGGER.info(profile.userName);
	}
	public IgniteCache<Long, Double> getCacheLastTradedPrice() {
		return cacheLastTradedPrice;
	}

	public void setCacheLastTradedPrice(IgniteCache<Long, Double> cacheLastTradedPrice) {
		this.cacheLastTradedPrice = cacheLastTradedPrice;
	}
	public KiteConnectService() {
		IgniteConfiguration cfg = new IgniteConfiguration();
		Ignite ignite = Ignition.start(cfg);
		Ignition.setClientMode(true);
		CacheConfiguration<Long, Double> ccfg = new CacheConfiguration<Long, Double>("LastTradedPrice");
		this.cacheLastTradedPrice = ignite.getOrCreateCache(ccfg);
		
		CacheConfiguration<Long, OrderParams> ccfgOrderParams = new CacheConfiguration<Long, OrderParams>("CachedOrderParams");
		this.cacheOrderParams = ignite.getOrCreateCache(ccfgOrderParams);
		
		CacheConfiguration<Long, Tick> ccfgLatestTickParams = new CacheConfiguration<Long, Tick>("CachedLatestTick");
		this.cacheLatestTick = ignite.getOrCreateCache(ccfgLatestTickParams);
		
		
	}
	/** Gets Margin. */
	public void getMargins(KiteConnect kiteConnect) throws KiteException, IOException {
		// Get margins returns margin model, you can pass equity or commodity as
		// arguments to get margins of respective segments.
		// Margins margins = kiteConnect.getMargins("equity");
		Margin margins = kiteConnect.getMargins("equity");
		LOGGER.info(margins.available.cash);
		LOGGER.info(margins.utilised.debits);
		LOGGER.info(margins.utilised.m2mUnrealised);
	}

	/** Place order. */
	public Order placeOrder(OrderRequestDTO tradeRequest){
		/**
		 * Place order method requires a orderParams argument which contains,
		 * tradingsymbol, exchange, transaction_type, order_type, quantity, product,
		 * price, trigger_price, disclosed_quantity, validity squareoff_value,
		 * stoploss_value, trailing_stoploss and variety (value can be regular, bo, co,
		 * amo) place order will return order model which will have only orderId in the
		 * order model
		 *
		 * Following is an example param for LIMIT order, if a call fails then
		 * KiteException will have error message in it Success of this call implies only
		 * order has been placed successfully, not order execution.
		 */
		boolean statusFlag=false;
		int ctr=0;
		while (!statusFlag) {
			
			KiteConnect kiteConnect;
			try {
				kiteConnect = getKiteConnectSession(tradeRequest.getUserId(),tradeRequest.getRequestToken());
				OrderParams orderParams = new OrderParams();
				orderParams.quantity = tradeRequest.getQuantity();
				orderParams.orderType = tradeRequest.getOrderType().toUpperCase();
				InstrumentModel instrumentList=instrumentRepository.findByInstrumentToken(String.valueOf(tradeRequest.getInstrumentToken()));
				orderParams.tradingsymbol = instrumentList.getTradingSymbol();
				orderParams.product = tradeRequest.getProduct().toUpperCase();
				orderParams.exchange = instrumentList.getExchange().toUpperCase();
				orderParams.transactionType = tradeRequest.getTransactionType().toUpperCase();
				orderParams.validity = tradeRequest.getValidity().toUpperCase();
				orderParams.price=tradeRequest.getPrice();
				orderParams.triggerPrice = tradeRequest.getTriggerPrice();
				orderParams.tag = tradeRequest.getTag();
				Order order = kiteConnect.placeOrder(orderParams, tradeRequest.getVariety().toLowerCase());
				LOGGER.info(order.orderId);
				statusFlag=true;
				return order;
			} catch (JSONException | IOException | KiteException e) {
				// TODO Auto-generated catch block
				// Invoke Naresh Ji's Service Ji
				if(ctr>3) {
					// TODO Implementation need to be done here throw custom exception
					
				}
				ctr++;
				e.printStackTrace();
			}
			
			
		}
		return null;
		
	}
	/** Modify order. */
	public Order modifyOrder(OrderRequestDTO tradeRequest) throws KiteException, IOException {
		// Order modify request will return order model which will contain only
		// order_id.
		boolean statusFlag=false;
		int ctr=0;
		while (!statusFlag) {
			
			KiteConnect kiteConnect;
			try {
				kiteConnect = getKiteConnectSession(tradeRequest.getUserId(),tradeRequest.getRequestToken());
				
				OrderParams orderParams = new OrderParams();
				orderParams.quantity = tradeRequest.getQuantity();
				orderParams.orderType = tradeRequest.getOrderType().toUpperCase();
				InstrumentModel instrumentList=instrumentRepository.findByInstrumentToken(String.valueOf(tradeRequest.getInstrumentToken()));
				orderParams.tradingsymbol = instrumentList.getTradingSymbol();
				orderParams.product = tradeRequest.getProduct();
				orderParams.exchange = instrumentList.getExchange().toUpperCase();
				orderParams.transactionType = tradeRequest.getTransactionType().toUpperCase();
				orderParams.validity = tradeRequest.getValidity().toUpperCase();
				orderParams.price = tradeRequest.getPrice();

				Order order = kiteConnect.modifyOrder(tradeRequest.getOrderId(), orderParams, tradeRequest.getValidity().toLowerCase());
				LOGGER.info("Modified Order ID: ");
				return order;
			} catch (JSONException | IOException | KiteException e) {
				// TODO Auto-generated catch block
				// Invoke Naresh Ji's Service Ji
				if(ctr>3) {
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
		boolean statusFlag=false;
		int ctr=0;
		while (!statusFlag) {
			
			KiteConnect kiteConnect;
			try {
				kiteConnect = getKiteConnectSession(tradeRequest.getUserId(),tradeRequest.getRequestToken());
				Order order2 = kiteConnect.cancelOrder(tradeRequest.getOrderId(), tradeRequest.getVariety());
				LOGGER.info(order2.orderId);
			} catch (JSONException | IOException | KiteException e) {
				// TODO Auto-generated catch block
				// Invoke Naresh Ji's Service Ji
				if(ctr>3) {
					// TODO Implementation need to be done here throw custom exception
					
				}
				ctr++;
				e.printStackTrace();
			}
		}
		
	}

	/** Place bracket order. */
	public void placeBracketOrder(OrderRequestDTO tradeRequest) throws KiteException, IOException {
		/**
		 * Bracket order:- following is example param for bracket order*
		 * trailing_stoploss and stoploss_value are points and not tick or price
		 */
		KiteConnect kiteConnect=getKiteConnectSession(tradeRequest.getUserId(),tradeRequest.getRequestToken());
		OrderParams orderParams = new OrderParams();
        orderParams.quantity = 1;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.price = 30.5;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.trailingStoploss = 1.0;
        orderParams.stoploss = 2.0;
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.squareoff = 3.0;
        orderParams.product = Constants.PRODUCT_MIS;
         Order order10 = kiteConnect.placeOrder(orderParams, Constants.VARIETY_BO);
         System.out.println(order10.orderId);
	}
	
	/** Place cover order. */
	public void placeCoverOrder(KiteConnect kiteConnect) throws KiteException, IOException {
		/** Cover Order:- following is an example param for the cover order
         * key: quantity value: 1
         * key: price value: 0
         * key: transaction_type value: BUY
         * key: tradingsymbol value: HINDALCO
         * key: exchange value: NSE
         * key: validity value: DAY
         * key: trigger_price value: 157
         * key: order_type value: MARKET
         * key: variety value: co
         * key: product value: MIS
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

	/** Get orderbook. */
	public void getOrders(KiteConnect kiteConnect) throws KiteException, IOException {
		// Get orders returns order model which will have list of orders inside, which
		// can be accessed as follows,
		List<Order> orders = kiteConnect.getOrders();
		for (int i = 0; i < orders.size(); i++) {
			LOGGER.info(orders.get(i).tradingSymbol + " " + orders.get(i).orderId + " " + orders.get(i).parentOrderId
					+ " " + orders.get(i).orderType + " " + orders.get(i).averagePrice + " "
					+ orders.get(i).exchangeTimestamp);
		}
		LOGGER.info("list of orders size is " + orders.size());
	}

	/** Get order details */
	public void getOrder(KiteConnect kiteConnect) throws KiteException, IOException {
		List<Order> orders = kiteConnect.getOrderHistory("180111000561605");
		for (int i = 0; i < orders.size(); i++) {
			LOGGER.info(orders.get(i).orderId + " " + orders.get(i).status);
		}
		LOGGER.info("list size is " + orders.size());
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

	/** Get all positions. */
	public void getPositions(KiteConnect kiteConnect) throws KiteException, IOException {
		// Get positions returns position model which contains list of positions.
		Map<String, List<Position>> position = kiteConnect.getPositions();
		LOGGER.info("" + position.get("net").size());
		LOGGER.info("" + position.get("day").size());
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
	public ArrayList<List<Tick>> getHistoricalData(KiteConnect kiteConnect,
			ArrayList<Long> InstrumentToken, Date fromDate, Date toDate, String interval, boolean flag)
			throws KiteException, IOException {
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
			tickList.add(DateFormatUtil.loadHistoricalDataSeries(historicalData, instrumentToken));
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
				LOGGER.info("order update " + order.orderId);
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
				if (ticks != null && ticks.size() > 0 && ticks.get(0).getMode() != null) {
					for (int i = 0; i < ticks.size(); i++) {
						String newJson = new Gson().toJson(ticks.get(i));
						LOGGER.info("" + newJson);
						if(cacheLatestTick!=null) {
							cacheLatestTick.put(ticks.get(i).getInstrumentToken(), ticks.get(i));
							LOGGER.info("Cached Tick Instument Token: "+ cacheLatestTick.get(ticks.get(i).getInstrumentToken()).getInstrumentToken()+" Last Traded Price: "+  cacheLatestTick.get(ticks.get(i).getInstrumentToken()).getLastTradedPrice());
						}
						kafkaTemplate.send(tickProducerTopic, newJson);
					}
				} else {
					LOGGER.info("Tick Not Received");
				}
			}
		});
		tickerProvider.setTryReconnection(true);
		tickerProvider.setMaximumRetries(10);
		tickerProvider.setMaximumRetryInterval(30);
		tickerProvider.connect();
		boolean isConnected = tickerProvider.isConnectionOpen();
		LOGGER.info("" + isConnected);
		tickerProvider.setMode(tokens, KiteTicker.modeFull);
	}

	public boolean calculateTrailingStopLoss(long instrumentToken,Tick tick) {
		
		return false;
	}
	public KiteConnect getKiteConnectSession(String userId, String requestToken)
			throws JSONException, IOException, KiteException {
		KiteConnect kiteConnect = null;
		KiteProperty kitePropertyList = propertyRepository.findByUserId(userId);
		if (kitePropertyList != null) {
			kitePropertyList.setRequestToken(requestToken);
			propertyRepository.save(kitePropertyList);
		}
		if (kiteSessionList != null && kiteSessionList.size() > 0) {
			kiteConnect = kiteSessionList.stream().filter(x -> userId.equals(x.getUserId())).findFirst().orElse(null);
		} else {
			KiteProperty kiteProperty = propertyRepository.findByUserId(userId);
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

	public ArrayList<List<Tick>> startBackTesting(KiteConnect kiteconnect, ArrayList<Long> instrumentTokens, Date fromDate, Date toDate,
			String interval, boolean continuous) throws IOException, KiteException {
		ArrayList<List<Tick>> tickList = getHistoricalData(kiteconnect,
				instrumentTokens, fromDate, toDate, interval, continuous);
		if (tickList != null && tickList.size() > 0) {
			for (int i = 0; i < tickList.size(); i++) {
				if (tickList.get(i) != null && tickList.get(i).size() > 0) {
					List<Tick> list=tickList.get(i);
					for (int j = 0; j < list.size(); j++) {
						try {
							Tick tick=list.get(j);
							Thread.sleep(300);
							String newJson = new Gson().toJson(tick);
							if(this.cacheLatestTick!=null) {
								this.cacheLatestTick.put(tick.getInstrumentToken(), tick);
								LOGGER.info("Back Test Cached Tick Instument Token: "+ cacheLatestTick.get(tick.getInstrumentToken()).getInstrumentToken()+" Last Traded Price: "+  cacheLatestTick.get(tick.getInstrumentToken()).getLastTradedPrice());
							}
							ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(tickProducerTopic,newJson);
							future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
								@Override
								public void onSuccess(SendResult<String, String> result) {
									LOGGER.info("Sent message: " + result);
								}
								@Override
								public void onFailure(Throwable ex) {
									LOGGER.info("Failed to send message");
								}
							});
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return tickList;
	}
	public IgniteCache<Long, OrderParams> getCacheOrderParams() {
		return cacheOrderParams;
	}
	public void setCacheOrderParams(IgniteCache<Long, OrderParams> cacheOrderParams) {
		this.cacheOrderParams = cacheOrderParams;
	}
	public IgniteCache<Long, Tick> getCacheLatestTick() {
		return cacheLatestTick;
	}
	public void setCacheLatestTick(IgniteCache<Long, Tick> cacheLatestTick) {
		this.cacheLatestTick = cacheLatestTick;
	}
}
