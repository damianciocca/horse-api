package com.etermax.spacehorse.core.catalog.repository.dynamo;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.mock.MockUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.dropwizard.jackson.Jackson;

public class CatalogCompressionUtilsTest {

	private CatalogResponse catalog;

	private ByteBuffer compressedCatalog;
	private CatalogResponse decompressedCatalog;

	@Before
	public void setUp() {
		catalog = new CatalogResponse(MockUtils.mockCatalog());
	}

	@After
	public void tearDown() {
		catalog = null;
		compressedCatalog = null;
	}

	@Test
	public void compressingACatalogWorks() {
		whenCompressingCatalog();
		thenCompressedCatalogIsNotEmpty();
	}

	public void thenCompressedCatalogIsNotEmpty() {
		assertThat(compressedCatalog.array().length).isGreaterThan(0);
	}

	public void whenCompressingCatalog() {
		compressedCatalog = CatalogCompressionUtils.compressCatalog(catalog);
	}

	@Test
	public void decompressingACompressedCatalogGivesTheSameCatalog() {
		whenCompressingCatalog();
		whenDecompressingCatalog();

		thenOriginalCatalogIsEqualToDecompressedCatalog();
	}

	private void thenOriginalCatalogIsEqualToDecompressedCatalog() {
		String jsonOriginal = convertCatalogToJson(catalog);
		String jsonDecompressed = convertCatalogToJson(decompressedCatalog);
		assertThat(jsonDecompressed).isEqualTo(jsonOriginal);
	}

	private static String convertCatalogToJson(CatalogResponse catalog) {
		try {
			return Jackson.newObjectMapper().writeValueAsString(catalog);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private void whenDecompressingCatalog() {
		decompressedCatalog = CatalogCompressionUtils.decompressCatalog(compressedCatalog);
	}

}