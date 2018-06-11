package com.etermax.spacehorse.core.inapps.domain.market.android.service.signature;

public interface SignatureVerifier {

    Boolean verifySignature(String signature, String data);

}
