package com.equitybot.kite.controller;


import com.equitybot.kite.KiteConnection;
import com.equitybot.kite.dao.property.KitePropertyDAO;
import com.equitybot.kite.dao.property.domain.KiteProperty;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/kite/connection/v1.0")
public class KiteController {

    @Autowired
    KitePropertyDAO kitePropertyDAO;
    @Autowired
    private KiteConnection kiteConnection;

    @GetMapping("/connect")
    public void connect(@RequestParam("userId") String userId,
                        @RequestParam("requestToken") String requestToken) throws KiteException, IOException {
        kiteConnection.getKiteConnectSession(userId, requestToken);
    }

    @GetMapping("/status")
    public String status() {
        return kiteConnection.status() ? "connected" : "disconnected";
    }

    @GetMapping("/disconnect")
    public void disConnect() throws IOException, KiteException {
        kiteConnection.disconnect();
    }

    private void setProperty() {
        KiteProperty kiteProperty = new KiteProperty();
        kiteProperty.setId(1 + "");
        kiteProperty.setApiKey("2tzf3nwdhkggh61w");
        kiteProperty.setUserId("WU6870");
        kiteProperty.setApiSecret("lmsqz4pg52sgmnmp6pqpj74sg1xrt18z");
        kitePropertyDAO.save(kiteProperty);

    }

}
