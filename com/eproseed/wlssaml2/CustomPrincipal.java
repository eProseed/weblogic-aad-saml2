package com.eproseed.wlssaml2;
import weblogic.security.spi.WLSUser;
import weblogic.security.principal.*;

// https://docs.oracle.com/middleware/12213/wls/WLAPI/weblogic/security/principal/WLSAbstractPrincipal.html

public class CustomPrincipal extends WLSAbstractPrincipal implements WLSUser {

    public CustomPrincipal(String commonName) {
        super();
        System.out.println("--- CustomPrincipal --- name: " + commonName);
        this.setName(commonName);
    }
 
}