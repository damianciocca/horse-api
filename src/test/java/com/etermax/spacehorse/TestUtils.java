package com.etermax.spacehorse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

	public static JsonNode readJson(String jsonFileName) {
		try {
			InputStream expectedJsonStream = TestUtils.class.getClassLoader().getResourceAsStream(jsonFileName);
			String jsonAsString = IOUtils.toString(expectedJsonStream, Charset.defaultCharset());
			return new ObjectMapper().reader().readTree(jsonAsString);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static JsonNode mapToJson(Object object) {
		try {
			return new ObjectMapper().reader().readTree(new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(object));
		} catch (IOException e) {
			throw new RuntimeException("An error occurred when trying to convert from obj to json " + object, e);
		}
	}

	public static <T> T mapJsonNodeToObject(JsonNode jsonNode, Class<T> clazz) {
		try {
			return new ObjectMapper().treeToValue(jsonNode, clazz);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("An error occurred when trying to convert from json obj " + jsonNode, e);
		}
	}

}
