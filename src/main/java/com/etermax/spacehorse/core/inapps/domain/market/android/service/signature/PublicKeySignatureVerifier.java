package com.etermax.spacehorse.core.inapps.domain.market.android.service.signature;

import com.etermax.spacehorse.core.inapps.error.InAppsErrors;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

public class PublicKeySignatureVerifier implements SignatureVerifier {

	private PublicKey publicKey;

	private static final Logger logger = LoggerFactory.getLogger(SignatureVerifier.class);

	public PublicKeySignatureVerifier(String publicKey) {
		try {
			Validate.notNull(publicKey);
			byte[] keyBytes = publicKey.getBytes();
			byte[] encodedKey = new Base64().decode(keyBytes);
			KeySpec keySpec = new X509EncodedKeySpec(encodedKey);
			this.publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
		} catch (Exception e) {
			logger.error("Cannot load public key", e);
			throw InAppsErrors.invalidConfiguration();
		}
	}

	@Override
	public Boolean verifySignature(String signature, String data) {
		try {
			Signature rsa = Signature.getInstance("SHA1withRSA");
			rsa.initVerify(publicKey);
			rsa.update(data.getBytes());
			byte [] bytes = Base64.decodeBase64(signature);
			return rsa.verify(bytes);
		} catch (Exception ex) {
			logger.warn("Cannot verify service", ex);
			return false;
		}
	}

}
