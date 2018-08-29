package com.equitybot.kite.controller;


import com.equitybot.kite.KiteConnection;
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



}
