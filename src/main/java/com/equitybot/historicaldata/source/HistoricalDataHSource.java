package com.equitybot.historicaldata.source;

import com.equitybot.kite.KiteConnection;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class HistoricalDataHSource {


    @Autowired
    private KiteConnection kiteConnection;

    public HistoricalData getHistoricalData(Long instrumentToken, Date fromDate, Date toDate, String interval, boolean continuous)
            throws KiteException, IOException, ParseException {


        return this.kiteConnection.session().getHistoricalData(fromDate, toDate, String.valueOf(instrumentToken), interval, continuous);

    }
}
