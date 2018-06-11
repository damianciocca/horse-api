package com.etermax.spacehorse.core.inapps.market;

import com.etermax.spacehorse.core.inapps.domain.market.Markets;
import com.etermax.spacehorse.core.inapps.domain.market.MarketsFactory;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.AndroidProperties;
import com.etermax.spacehorse.core.inapps.domain.market.ios.service.IosProperties;
import com.etermax.spacehorse.core.user.model.Platform;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MarketsFactoryTest {

    private Markets markets;

    @Before
    public void setUp() {
        String packageName = "com.etermax.store.horserace";
        String signature = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg1sMFwizzpmAQq/J0DLExnHibvSLVeCie2OLdhCNK1LEWJTzWvRws/t6O2i/DkgxxbgtpGaRCYTvZfCS8V7XBloPVNOv6j2JCosg3+q0up6vRrIs5WhNc3UTHj48wrZJSRHPyH8aGmdCAEjFRZThlLU3vxcSYRZmvRAvYb7aFnsJ7Ov+m00yVRBk7a6lAcbTU6PQ4JAPsl1fVRbTZieLeSQkmYKstoLBBrIv6GwiZ+TS/gjX49XqBPHNVMTuS0EAoxdnXhwYsVVqAGw3mM0VvqhkOIJqu0CXv+WVyNDyXBprruFxy5vP6ImHErM1oTQ08vSN5s3ehQrWNFLC1Y7VvQIDAQAB";
        AndroidProperties androidProperties = new AndroidProperties(packageName, signature);
        IosProperties iosProperties = new IosProperties(packageName);
        this.markets = MarketsFactory.buildMarkets(androidProperties, iosProperties);
    }

    @Test
    public void testMemoryMarkets() {
        assertTrue(this.markets.findById(Platform.EDITOR).isPresent());
        assertTrue(this.markets.findById(Platform.ANDROID).isPresent());
        assertTrue(this.markets.findById(Platform.IOS).isPresent());
    }

}