package com.equitybot.kite;

import com.equitybot.common.model.OrderRequestDTO;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class KiteOperation {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KiteConnection kiteConnection;

    /**
     * @param tradeRequest ""
     * @return : Place order.
     * Place order method requires a orderParams argument which contains,
     * tradingsymbol, exchange, transaction_type, order_type, quantity, product,
     * price, trigger_price, disclosed_quantity, validity squareoff_value,
     * stoploss_value, trailing_stoploss and variety (value can be regular, bo, co,
     * amo) place order will return order model which will have only orderId in the
     * order model
     * <p>
     * Following is an example param for LIMIT order, if a call fails then
     * KiteException will have error message in it Success of this call implies only
     * order has been placed successfully, not order execution.
     * <p>
     * tag is optional and it cannot be more than 8 characters and only alphanumeric is allowed
     */
    public Order placeOrder(OrderRequestDTO tradeRequest) throws KiteException, IOException {
        logger.info(" - #Start-Time : {} # $$$$$$$$$$ - Place Order - $$$$$$$$$$ - @ tradeRequest : {} ",
                System.currentTimeMillis(), tradeRequest);
        Order order = kiteConnection.session().placeOrder(transformerOrderParams(tradeRequest),
                tradeRequest.getVariety());
        logger.info(" - #End-Time   : {} # $$$$$$$$$$ - Place Order - $$$$$$$$$$ - @ order : {} ",
                System.currentTimeMillis(), order);
        return order;
    }

    /**
     * @param tradeRequest
     * @return : modify Order response
     * @throws KiteException
     * @throws IOException
     */
    public Order modifyOrder(OrderRequestDTO tradeRequest) throws KiteException, IOException {
        logger.info(" - #Start-Time : {} # $$$$$$$$$$ - Modify Order - $$$$$$$$$$ - @ tradeRequest : {} ",
                System.currentTimeMillis(), tradeRequest);
        Order order = kiteConnection.session().modifyOrder(tradeRequest.getOrderId(),
                transformerOrderParams(tradeRequest), tradeRequest.getVariety());
        logger.info(" - #End-Time   : {} # $$$$$$$$$$ - Modify Order - $$$$$$$$$$ - @ order : {} ",
                System.currentTimeMillis(), order);
        return order;
    }

    /**
     * @param tradeRequest
     * @return : cancel Order
     * @throws KiteException
     * @throws IOException
     */
    public Order cancelOrder(OrderRequestDTO tradeRequest) throws KiteException, IOException {
        logger.info(" - #Start-Time : {} # $$$$$$$$$$ - Cancel Order - $$$$$$$$$$ - @ tradeRequest : {} ",
                System.currentTimeMillis(), tradeRequest);
        Order order = kiteConnection.session().cancelOrder(tradeRequest.getOrderId(),
                tradeRequest.getVariety());
        logger.info(" - #End-Time   : {} # $$$$$$$$$$ - Cancel Order - $$$$$$$$$$ - @ order : {} ",
                System.currentTimeMillis(), order);
        return order;
    }

    /**
     * @return : Get orderBook
     * Get orders returns order model which will have list of orders inside.
     * @throws KiteException
     * @throws IOException
     */
    public List<Order> getOrders() throws KiteException, IOException {
        logger.info(" - #Start-Time : {} # $$$$$$$$$$ - Get Orders- $$$$$$$$$$ -", System.currentTimeMillis());
        List<Order> orders = kiteConnection.session().getOrders();
        /*for (Order order : orders) {
            logger.info(order.tradingSymbol);
            logger.info("  Order - @ tradingSymbol : {} @ orderId : {} @ parentOrderId : {} @ orderType : {} @" +
                            " averagePrice : {} @ exchangeTimestamp : {} @ status : {}", order.tradingSymbol, order.orderId,
                    order.parentOrderId, order.orderType, order.averagePrice, order.exchangeTimestamp, order.status);
        }*/
        logger.info(" - #End-Time   : {} # $$$$$$$$$$ - Get Orders - $$$$$$$$$$ - @ order size : {} ",
                System.currentTimeMillis(), orders.size());
        return orders;
    }

    /**
     * @param orderId
     * @return : Get order details
     * @throws KiteException
     * @throws IOException
     */
    public List<Order> getOrderHistory(String orderId) throws KiteException, IOException {
        logger.info(" - #Start-Time : {} # $$$$$$$$$$ - Get Order History - $$$$$$$$$$ -", System.currentTimeMillis());
        List<Order> orders = kiteConnection.session().getOrderHistory(orderId);
       /* for (Order order : orders) {
            logger.info(order.tradingSymbol);
            logger.info("- Order - # tradingSymbol : {} @ orderId : {} @ parentOrderId : {} @ orderType : {} @" +
                            " averagePrice : {} @ exchangeTimestamp : {} @ status : {}", order.tradingSymbol, order.orderId,
                    order.parentOrderId, order.orderType, order.averagePrice, order.exchangeTimestamp, order.status);
        }*/
        logger.info(" - #End-Time   : {} # $$$$$$$$$$ - Get Order History - $$$$$$$$$$ - @ order size : {} ",
                System.currentTimeMillis(), orders.size());
        return orders;
    }

    public Profile getProfile() throws IOException, KiteException {
        logger.info(" - #Start-Time : {} # $$$$$$$$$$ - Get Profile - $$$$$$$$$$ -", System.currentTimeMillis());
        Profile profile = kiteConnection.session().getProfile();
        logger.info(" - #End-Time   : {} # $$$$$$$$$$ - Get Profile - $$$$$$$$$$ - @ profile : {} ",
                System.currentTimeMillis(), profile);
        return profile;
    }

    /**
     * @return : available amount in account
     * <p>
     * Get margins returns margin model, you can pass equity or commodity
     * as arguments to get margins of respective segments.
     * @throws KiteException
     * @throws IOException
     */
    public double getMargins() throws KiteException, IOException {
        logger.info(" - #Start-Time : {} # $$$$$$$$$$ - Get Margins - $$$$$$$$$$ -", System.currentTimeMillis());
        double availableMargins = 0;
        Map<String, Margin> margins = kiteConnection.session().getMargins();
        if (margins.containsKey("equity")) {
            availableMargins = Double.parseDouble(margins.get("equity").available.cash);
        }
        logger.info(" - #End-Time   : {} # $$$$$$$$$$ - Get Margins - $$$$$$$$$$ - @ availableMargins : {} ",
                System.currentTimeMillis(), availableMargins);
        return availableMargins;
    }

    /**
     * @return :Get all positions.
     * <p>
     * Get positions returns position model which contains list of positions.
     * @throws KiteException
     * @throws IOException
     */
    public Map<String, List<Position>> getPositions() throws KiteException, IOException {
        logger.info(" - #Start-Time : {} # $$$$$$$$$$ - Get Positions - $$$$$$$$$$ -", System.currentTimeMillis());
        Map<String, List<Position>> position = kiteConnection.session().getPositions();
        logger.info(" - #End-Time   : {} # $$$$$$$$$$ - Get Positions - $$$$$$$$$$ - Position # net size : {} # day size : {} ",
                System.currentTimeMillis(), position.get("net").size(), position.get("day").size());

        return position;
    }

    /**
     * @return : Get all instruments that can be traded using kite connect
     * @throws KiteException
     * @throws IOException
     */
    public List<Instrument> getAllInstruments() throws KiteException, IOException {
        logger.info(" - #Start-Time : {} # $$$$$$$$$$ - Get All Instruments - $$$$$$$$$$ -", System.currentTimeMillis());
        List<Instrument> instruments = kiteConnection.session().getInstruments();
        logger.info(" - #End-Time   : {} # $$$$$$$$$$ - Get All Instruments - $$$$$$$$$$ - @ number of instruments : {}",
                System.currentTimeMillis(), instruments.size());
        return instruments;
    }

    private OrderParams transformerOrderParams(OrderRequestDTO tradeRequest) {
        OrderParams orderParams = new OrderParams();
        orderParams.exchange = tradeRequest.getExchange();
        orderParams.tradingsymbol = tradeRequest.getTradingsymbol();
        orderParams.transactionType = tradeRequest.getTransactionType();
        orderParams.quantity = tradeRequest.getQuantity();
        orderParams.price = tradeRequest.getPrice();
        orderParams.product = tradeRequest.getProduct();
        orderParams.orderType = tradeRequest.getOrderType();
        orderParams.validity = tradeRequest.getValidity();
        orderParams.disclosedQuantity = tradeRequest.getDisclosedQuantity();
        orderParams.triggerPrice = tradeRequest.getTriggerPrice();
        orderParams.squareoff = tradeRequest.getSquareoff();
        orderParams.stoploss = tradeRequest.getStopLossPrice();
        orderParams.trailingStoploss = tradeRequest.getTrailingStopLossPrice();
        orderParams.tag = tradeRequest.getTag();
        orderParams.parentOrderId = tradeRequest.getParentOrderId();
        return orderParams;
    }

}
