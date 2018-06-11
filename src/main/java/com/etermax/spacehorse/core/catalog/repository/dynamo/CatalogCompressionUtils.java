package com.etermax.spacehorse.core.catalog.repository.dynamo;

import java.nio.ByteBuffer;

import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;

public class CatalogCompressionUtils {
	static public ByteBuffer compressCatalog(CatalogResponse catalogResponse) {
		ObjectMapper mapper = Jackson.newObjectMapper();

		try {
			String json = mapper.writeValueAsString(catalogResponse);

			return compressCatalogJson(json);

		} catch (Exception e) {
			throw new ApiException("Error compressing catalog", e);
		}
	}

	public static ByteBuffer compressCatalogJson(String json) {
		byte[] compressedJson = GZipCompression.compress(json);

		return ByteBuffer.wrap(compressedJson);
	}

	public static CatalogResponse decompressCatalog(ByteBuffer catalogCompressed) {
		ObjectMapper mapper = Jackson.newObjectMapper();

		try {
			String json = decompressCatalogToJson(catalogCompressed);

			CatalogResponse catalogResponse = mapper.readValue(json, CatalogResponse.class);

			return catalogResponse;

		} catch (Exception e) {
			throw new ApiException("Error decompressing catalog", e);
		}
	}

	public static String decompressCatalogToJson(ByteBuffer catalogCompressed) {
		return GZipCompression.decompress(catalogCompressed.array());
	}
}
