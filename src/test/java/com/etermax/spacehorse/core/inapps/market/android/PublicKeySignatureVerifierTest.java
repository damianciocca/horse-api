package com.etermax.spacehorse.core.inapps.market.android;

import com.etermax.spacehorse.core.inapps.domain.market.android.service.signature.PublicKeySignatureVerifier;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertFalse;

public class PublicKeySignatureVerifierTest {

    @Test
    public void verifyInvalidSignature() throws IOException {
        String signature = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg1sMFwizzpmAQq/J0DLExnHibvSLVeCie2OLdhCNK1LEWJTzWvRws/t6O2i/DkgxxbgtpGaRCYTvZfCS8V7XBloPVNOv6j2JCosg3+q0up6vRrIs5WhNc3UTHj48wrZJSRHPyH8aGmdCAEjFRZThlLU3vxcSYRZmvRAvYb7aFnsJ7Ov+m00yVRBk7a6lAcbTU6PQ4JAPsl1fVRbTZieLeSQkmYKstoLBBrIv6GwiZ+TS/gjX49XqBPHNVMTuS0EAoxdnXhwYsVVqAGw3mM0VvqhkOIJqu0CXv+WVyNDyXBprruFxy5vP6ImHErM1oTQ08vSN5s3ehQrWNFLC1Y7VvQIDAQAB";
        File file = new File(getClass().getClassLoader().getResource("receipts/google.receipt").getFile());
        String data = new Scanner(file).useDelimiter("\\Z").next();
        PublicKeySignatureVerifier publicKeySignatureVerifier = new PublicKeySignatureVerifier(signature);
        assertFalse(publicKeySignatureVerifier.verifySignature("fake", data));
    }

}
