package com.equitybot.kite;

import com.equitybot.kite.dao.property.domain.KiteProperty;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class KiteConnection {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private com.equitybot.kite.dao.property.KitePropertyDAO kitePropertyDAO;


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
        KiteProperty kiteProperty = this.kitePropertyDAO.findByUserId(userId);
        if (kiteProperty != null) {
            kiteProperty.setRequestToken(requestToken);
            this.kiteConnectSession = new KiteConnect(kiteProperty.getApiKey());
            this.kiteConnectSession.setUserId(kiteProperty.getUserId());
            this.kiteConnectSession.setEnableLogging(true);
            this.kiteConnectSession.setSessionExpiryHook(new SessionExpiryHook() {
                @Override
                public void sessionExpired() {
                    logger.error(" ---- session expired for userId : {}", userId);
                    kiteConnectSession = null;
                    userId = null;
                }
            });
            logger.info(" ----- Request Token : {} # Api Secret : {}", kiteProperty.getRequestToken(), kiteProperty.getApiSecret());
            User userModel = kiteConnectSession.generateSession(kiteProperty.getRequestToken(), kiteProperty.getApiSecret());
            kiteConnectSession.setAccessToken(userModel.accessToken);
            kiteConnectSession.setPublicToken(userModel.publicToken);
            kitePropertyDAO.updatePropertyByUserId(userId, kiteProperty.getRequestToken(), userModel.accessToken, userModel.publicToken);
        } else {
            logger.error(" --- kiteProperty : {} is null", kiteProperty);
            throw new RuntimeException(" --- kiteProperty is null");
        }
        return kiteConnectSession;
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
