package com.equitybot.kite;

import com.equitybot.common.config.YAMLConfig;
import com.equitybot.kite.cache.KiteCache;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class KiteConnection {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private YAMLConfig yamlConfig;
    
    @Autowired
    private KiteCache kiteCache;

    private KiteConnect kiteConnectSession;
    private String userId;

    public KiteConnect getKiteConnectSession(String userId, String requestToken) throws KiteException, IOException {
        logger.info(" --- Get Session  # User Id : {} #  Request Token : {} ", userId, requestToken);
        if (kiteConnectSession != null) {
            throw new RuntimeException(" ***  kiteConnectSession is already connected ");
        } else if (requestToken == null || userId == null) {
            throw new RuntimeException(" ***  userId  or requestToken is null ");
        }
        return connect(userId, requestToken);
    }

    private KiteConnect connect(final String user, String requestToken)
            throws KiteException, IOException {
        this.userId = user;
        String[] userValue = this.yamlConfig.getUsersValue().get(userId);
        if (userValue != null) {
            this.kiteConnectSession = new KiteConnect(userValue[1]);
            this.kiteConnectSession.setUserId(userValue[0]);
            this.kiteConnectSession.setEnableLogging(true);
            this.kiteConnectSession.setSessionExpiryHook(new SessionExpiryHook() {
                @Override
                public void sessionExpired() {
                    logger.error(" ---- session expired for userId : {}", userId);
                    kiteConnectSession = null;
                    userId = null;
                }
            });
            logger.info(" ----- Request Token : {} # Api Secret : {}", requestToken, userValue[2]);
            User userModel = this.kiteConnectSession.generateSession(requestToken, userValue[2]);
            this.kiteConnectSession.setAccessToken(userModel.accessToken);
            this.kiteConnectSession.setPublicToken(userModel.publicToken);
            updateInstrumentsInCache();
        } else {
            logger.error(" --- user is not configure : {} is null", userValue);
            throw new RuntimeException(" --- kiteProperty is null");
        }
        return this.kiteConnectSession;
    }
    
    /**
     * @return : Get all instruments that can be traded using kite connect
     * @throws KiteException
     * @throws IOException
     */
    private void updateInstrumentsInCache() throws KiteException, IOException {
        logger.info(" - #Start-Time : {} # $$$$$$$$$$ - Get All Instruments - $$$$$$$$$$ -", System.currentTimeMillis());
        List<Instrument> instruments = this.kiteConnectSession.getInstruments();
        logger.info(" - #End-Time   : {} # $$$$$$$$$$ - Get All Instruments - $$$$$$$$$$ - @ number of instruments : {}",
                System.currentTimeMillis(), instruments.size());
        
        for(Instrument instrumentObj : instruments) {
        	kiteCache.putOnInstrumentCache(instrumentObj.getInstrument_token(), instrumentObj);
        }
    }

    public KiteConnect session() {
        return kiteConnectSession;
    }

    public boolean status() {
        return kiteConnectSession != null;
    }

    public void disconnect() throws IOException, KiteException {
        kiteConnectSession.invalidateAccessToken();
    }


}
