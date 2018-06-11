package com.etermax.spacehorse.core.inapps.market.android;

import com.etermax.spacehorse.configuration.EnviromentType;
import com.etermax.spacehorse.core.inapps.domain.market.android.AndroidMarket;
import com.etermax.spacehorse.core.inapps.domain.market.android.AndroidReceipt;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.AndroidProperties;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.AndroidService;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.signature.SignatureVerifier;
import com.etermax.spacehorse.core.inapps.util.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AndroidMarketTest {

    private AndroidMarket androidMarket;

    private AndroidProperties androidProperties;

    @Before
    public void setUp() {
        String packageName = "com.etermax.spacehorse2";
        this.androidProperties = new AndroidProperties(packageName, "fake");
        AndroidService androidService = new AndroidService(androidProperties, getSignatureVerifier());
        this.androidMarket = new AndroidMarket(androidService);
    }

    private SignatureVerifier getSignatureVerifier() {
        return (signature, data) -> (!"fake".equals(signature));
    }

    @Test
    public void validateGoogleReceiptWithValidSignature() throws IOException {
        AndroidReceipt androidReceipt = (AndroidReceipt) JsonObject.newFromResource("receipts/google.receipt", AndroidReceipt.class);

        assertTrue(androidMarket.validate(androidReceipt));
        assertEquals(this.androidProperties.getPackageName(), androidReceipt.getPackageName());
    }

    @Test
    public void theMarketCanBeUsedInProduction() throws IOException {
        assertTrue(androidMarket.canBeUsedIn(EnviromentType.PRODUCTION));
    }

    @Test
    public void failToValidateGoogleReceiptWithInvalidSignature() throws IOException {
        String receipt = "{\n" + "\t\"Store\": \"GooglePlay\",\n" + "\t\"TransactionID\": \"GPA.3354-5649-0281-57515\",\n" + "\t\"Payload\": {\n"
                + "\t\t\"json\": {\n" + "\t\t\t\"orderId\": \"GPA.3354-5649-0281-57515\",\n" + "\t\t\t\"packageName\": \"com.etermax.spacehorse2\",\n"
                + "\t\t\t\"productId\": \"android.gems80\",\n" + "\t\t\t\"purchaseTime\": 1502128408636,\n" + "\t\t\t\"purchaseState\": 0,\n"
                + "\t\t\t\"purchaseToken\": \"ekdjmdgdggfcjekeecjohmgi.AO-J1OxhQJqn9xK-OV-nGR2maf5PXRVkp-74fs_2UTZ--08TyGYdlTqN4pdeggykX8mCljioZrRX1HAmbVpf64uZsDTiY9w9JjYkqBE5xDIsV2sg7pOFEkkcqh0g1VqfouHtbCEkm62A\"\n"
                + "\t\t},\n"
                + "\t\t\"signature\": \"fake\"\n"
                + "\t}\n" + "}";
        AndroidReceipt androidReceipt = new AndroidReceipt(receipt);

        assertFalse(androidMarket.validate(androidReceipt));
    }

    @Test
    public void failWhenNoDataIsProvided() throws IOException {
        AndroidReceipt androidReceipt = new AndroidReceipt(null);

        assertFalse(androidMarket.validate(androidReceipt));
    }

    @Test
    public void failWhenNoSignatureIsProvided() {
        String receipt = "{\n" + "\t\"Store\": \"GooglePlay\",\n" + "\t\"TransactionID\": \"GPA.3354-5649-0281-57515\",\n" + "\t\"Payload\": {\n"
                + "\t\t\"json\": {\n" + "\t\t\t\"orderId\": \"GPA.3354-5649-0281-57515\",\n" + "\t\t\t\"packageName\": \"com.etermax.spacehorse2\",\n"
                + "\t\t\t\"productId\": \"android.gems80\",\n" + "\t\t\t\"purchaseTime\": 1502128408636,\n" + "\t\t\t\"purchaseState\": 0,\n"
                + "\t\t\t\"purchaseToken\": \"ekdjmdgdggfcjekeecjohmgi.AO-J1OxhQJqn9xK-OV-nGR2maf5PXRVkp-74fs_2UTZ--08TyGYdlTqN4pdeggykX8mCljioZrRX1HAmbVpf64uZsDTiY9w9JjYkqBE5xDIsV2sg7pOFEkkcqh0g1VqfouHtbCEkm62A\"\n"
                + "\t\t}\n"
                + "\t}\n" + "}";
        AndroidReceipt androidReceipt = new AndroidReceipt(receipt);

        assertFalse(androidMarket.validate(androidReceipt));
    }

}