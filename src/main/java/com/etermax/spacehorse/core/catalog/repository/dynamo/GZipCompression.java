package com.etermax.spacehorse.core.catalog.repository.dynamo;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

//Taken from https://stackoverflow.com/questions/16351668/compression-and-decompression-of-string-data-in-java
public class GZipCompression {
	public static byte[] compress(final String stringToCompress) {
		if (isNull(stringToCompress) || stringToCompress.length() == 0) {
			return null;
		}

		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream(); final GZIPOutputStream gzipOutput = new GZIPOutputStream(baos)) {
			gzipOutput.write(stringToCompress.getBytes(UTF_8));
			gzipOutput.finish();
			return baos.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException("Error while compression!", e);
		}
	}

	public static String decompress(final byte[] compressed) {
		if (isNull(compressed) || compressed.length == 0) {
			return null;
		}

		try (final GZIPInputStream gzipInput = new GZIPInputStream(new ByteArrayInputStream(compressed));
				final StringWriter stringWriter = new StringWriter()) {
			IOUtils.copy(gzipInput, stringWriter, UTF_8);
			return stringWriter.toString();
		} catch (IOException e) {
			throw new UncheckedIOException("Error while decompression!", e);
		}
	}
}