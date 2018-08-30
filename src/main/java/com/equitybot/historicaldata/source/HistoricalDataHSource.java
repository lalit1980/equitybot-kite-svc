package com.equitybot.historicaldata.source;

import com.equitybot.kite.KiteConnection;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class HistoricalDataHSource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KiteConnection kiteConnection;

    public HistoricalData getHistoricalData(Long instrumentToken, Date fromDate, Date toDate, String interval, boolean continuous)
            throws KiteException, IOException, ParseException {
        logger.info("instrumentToken : {} # fromDate : {} # toDate : {}# ",instrumentToken,  fromDate,  toDate);
        return this.kiteConnection.session().getHistoricalData(fromDate, toDate, String.valueOf(instrumentToken), interval, continuous);

    }
}
