package com.etermax.spacehorse.core.abtest.model;

import com.etermax.spacehorse.core.catalog.model.ABTesterEntry;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.dynamo.CatalogDelta;
import com.etermax.spacehorse.core.catalog.repository.dynamo.DynamoCatalog;
import com.etermax.spacehorse.core.catalog.repository.dynamo.DynamoJsonCatalog;
import com.etermax.spacehorse.core.catalog.repository.dynamo.TagDoesntExistException;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.error.DeltaFormatException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ABTestParser {

    private final ObjectMapper objectMapper;

    public ABTestParser() {
        objectMapper = Jackson.newObjectMapper();
    }


    static public ABTesterEntry getAbTesterEntry(Catalog catalog, ABTag abTag) {
        return catalog.getAbTesterCollection()
                .getEntries().stream()
                .filter(entry -> entry.getId().equals(abTag.toString()))
                .findFirst()
               .orElseThrow(TagDoesntExistException::new);
    }

    public DynamoCatalog modifyCatalogWithDeltas(String catalogId, List<CatalogDelta> catalogDeltas, DynamoJsonCatalog dynamoCatalog) {
        ObjectMapper objectMapper = Jackson.newObjectMapper();
        String rawJson = dynamoCatalog.getCatalogAsJson();

        try {
            String modifiedJson = modifyJson(catalogDeltas, rawJson);
            CatalogResponse newCatalogContent = objectMapper.readValue(modifiedJson, CatalogResponse.class);
            return new DynamoCatalog(catalogId, true, newCatalogContent);

        } catch (IOException e) {
            throw new RuntimeException("ABTestParser failed to parse json.", e);
        }
    }

    public String modifyJson(List<CatalogDelta> catalogDeltas, String rawJson) {
        try {
            JsonNode jsonNode = objectMapper.readTree(rawJson);
            catalogDeltas.forEach(modifySheet(jsonNode));
            return jsonNode.toString();
        } catch (Exception e) {
            throw new ApiException("ABTest exception trying to modify json catalog.", e);
        }
    }

    private Consumer<CatalogDelta> modifySheet(JsonNode jsonNode) {
        return delta -> {
            JsonNode sheetNode = jsonNode.get(delta.getSheet());
            sheetNode.get("entries").elements().forEachRemaining(modifyRow(delta));
        };
    }

    private Consumer<JsonNode> modifyRow(CatalogDelta delta) {
        return entry -> {
            if (entry.get("Id").asText().equals(delta.getId())) {
                String col = delta.getCol();
                ValueNode newValueNode = getNewValueNodeByType(delta, entry.get(col));
                ((ObjectNode) entry).replace(col, newValueNode);
            }
        };
    }

    private ValueNode getNewValueNodeByType(CatalogDelta delta, JsonNode jsonNodeCol) {
        ValueNode newValueNode = null;
        switch (jsonNodeCol.getNodeType()) {
            case STRING:
                newValueNode = new TextNode(delta.getVal());
                break;
            case NUMBER:
                newValueNode = new IntNode(Integer.valueOf(delta.getVal()));
                break;
            case BOOLEAN:
                newValueNode = BooleanNode.valueOf(Boolean.valueOf(delta.getVal()));
                break;
        }
        return newValueNode;
    }

    public List<CatalogDelta> getDeltas(String deltas) {
        if (deltas == null || deltas.isEmpty()) {
            return new ArrayList<>();
        }
        return processDeltas(deltas);
    }

    private List<CatalogDelta> processDeltas(String deltas) {
        String[] splitedDeltaList = deltas.split(";");
        List<CatalogDelta> catalogDeltas = new ArrayList<>();

        for (int i = 0; i < splitedDeltaList.length; i++) {
            CatalogDelta delta = getDelta(splitedDeltaList[i]);
            if(delta!=null){
                catalogDeltas.add(delta);
            }
        }
        return catalogDeltas;
    }

    private CatalogDelta getDelta(String s) {
        if(s==null || s.isEmpty()) {
            return null;
        }

        String[] splitedDelta = s.split("=");

        if (splitedDelta.length != 2) {
            throw new DeltaFormatException("Invalid ABTester delta format");
        }

        String[] ids = splitedDelta[0].split("\\.");
        if (ids.length != 3) {
            throw new DeltaFormatException("Invalid ABTester delta format");
        }

        return new CatalogDelta(ids[0], ids[1], ids[2], splitedDelta[1]);
    }
}
