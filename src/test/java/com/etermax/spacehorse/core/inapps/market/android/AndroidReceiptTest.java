package com.etermax.spacehorse.core.inapps.market.android;

import com.etermax.spacehorse.core.inapps.domain.market.android.AndroidReceipt;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AndroidReceiptTest {

    private String packageName;

    private String signature;

    private String data;

    private String receipt;

    @Before
    public void setUp() {
        packageName = "com.etermax.spacehorse2";
        signature = "\"signature\":\"BJup7haTRYor9tQ45F+62Ua4pWM5FrgGBrbUM\\/Ry53e3\\/NkcJ1wOZ75vUrSWeABD6tyxVlfgNI9nbIJ4CrYYYmJK+8TNds9+dhJDTAF8WPbVWYtPTCFeLrNRg8k9vKFf2XhYetvlggKN+XwJfcc8L1nQeGNNrGtAhRiJDEqVh7G7ehPbQJgCECn9Evxtu+eHZ0mKydz1EuFWlZqtI6\\/0gki9cWoDkyckKq++zxjZAU2VjzBhDMzs9IAjNL6pNVYEYj9m9Ibc7MzTijlBjcnoVNaaZo1qnkgrAUxmF4zkTcRz3VafgnsQbghssHC2tuQ\\/IYp+aIAdeTRptHzMlNDtbg==\"";
        data = "{\"packageName\":\"" + packageName + "\","
                + "\"orderId\":\"GPA.3354-5649-0281-57515\","
                + "\"productId\":\"android.gems80\","
                + "\"purchaseTime\":1502128408636,"
                + "\"purchaseState\":0,"
                + "\"purchaseToken\":\"fake\""
                + "}";
        receipt = "{\"Payload\" : { \"json\":" + data + "," + signature + "} }";
    }

    @Test
    public void testGetPackageName() {
        AndroidReceipt androidReceipt = new AndroidReceipt(receipt);

        assertEquals(packageName, androidReceipt.getPackageName());
    }

    @Test
    public void testFailToGetPackageName() {
        AndroidReceipt androidReceipt = new AndroidReceipt(null);

        assertNull(androidReceipt.getPackageName());
    }

    @Test
    public void testGetDataAsString() {
        AndroidReceipt androidReceipt = new AndroidReceipt(receipt);

        assertTrue(androidReceipt.getDataAsString().contains("productId"));
        assertTrue(androidReceipt.getDataAsString().contains("packageName"));
    }

    @Test
    public void testFailToGetDataAsString() {
        AndroidReceipt androidReceipt = new AndroidReceipt(null);

        assertNull(androidReceipt.getDataAsString());
    }

}