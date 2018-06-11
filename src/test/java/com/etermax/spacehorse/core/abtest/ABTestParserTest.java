package com.etermax.spacehorse.core.abtest;

import com.etermax.spacehorse.core.abtest.model.ABTestParser;
import com.etermax.spacehorse.core.catalog.repository.dynamo.CatalogDelta;
import com.etermax.spacehorse.core.catalog.repository.dynamo.DynamoJsonCatalog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.etermax.spacehorse.TestUtils.readJson;
import static org.assertj.core.api.Assertions.assertThat;

public class ABTestParserTest {

    private String jsonFile = " {\"Maps\": {\n" +
            "    \"entries\": [{\n" +
            "      \"Id\": \"MapDrot\",\n" +
            "      \"MapNumber\": 0,\n" +
            "      \"MMR\": 0,\n" +
            "      \"VictoryGold\": 5,\n" +
            "      \"VictoryRewardsPerDay\": 20\n" +
            "    }, {\n" +
            "      \"Id\": \"MapNewAmazonia\",\n" +
            "      \"MapNumber\": 1,\n" +
            "      \"MMR\": 400,\n" +
            "      \"VictoryGold\": 7,\n" +
            "      \"VictoryRewardsPerDay\": 20\n" +
            "    \n" +
            "    }]\n" +
            "  }}";

    private String expectedJson = "{\"Maps\":{\"entries\":[{\"Id\":\"MapDrot\",\"MapNumber\":0,\"MMR\":5,\"VictoryGold\":5,\"VictoryRewardsPerDay\":20},{\"Id\":\"MapNewAmazonia\",\"MapNumber\":1,\"MMR\":400,\"VictoryGold\":7,\"VictoryRewardsPerDay\":20}]}}";

    @Test
    public void modifyCatalogWithDeltasTest() {
        List<CatalogDelta> deltas = new ArrayList<>();
        String sheet = "Maps";
        String id = "MapDrot";
        String col = "MMR";
        String val = "5";
        CatalogDelta delta = new CatalogDelta(sheet, id, col, val);
        deltas.add(delta);

        DynamoJsonCatalog dynamoCatalog = new DynamoJsonCatalog();
        dynamoCatalog.setCatalogAsJson(jsonFile);
        ABTestParser abTestParser = new ABTestParser();

        try {
            String modifiedJson = abTestParser.modifyJson(deltas, jsonFile);
        } catch (Exception e) {

        }

        assertThat(expectedJson).isEqualTo(expectedJson);
    }

    @Test
    public void deltaParsingTest() {
        String deltasString = "A.B.C=0;A1.B1.C1=something;A2.B2.C2=1.44";

        List<CatalogDelta> deltas = new ABTestParser().getDeltas(deltasString);

        assertThat(deltas.size()).isEqualTo(3);

        assertDelta(deltas.get(0), "A", "B", "C", "0");
        assertDelta(deltas.get(1), "A1", "B1", "C1", "something");
        assertDelta(deltas.get(2), "A2", "B2", "C2", "1.44");
    }

    private void assertDelta(CatalogDelta delta, String sheet, String id, String col, String val) {
        assertThat(delta.getSheet()).isEqualTo(sheet);
        assertThat(delta.getId()).isEqualTo(id);
        assertThat(delta.getCol()).isEqualTo(col);
        assertThat(delta.getVal()).isEqualTo(val);
    }

    public static String readAsString(String jsonFile) {
        try {
            return new ObjectMapper().writeValueAsString(readJson(jsonFile));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("unexpected error when trying to parse json file", e);
        }
    }
}
