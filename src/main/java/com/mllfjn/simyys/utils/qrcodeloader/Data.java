package com.mllfjn.simyys.utils.qrcodeloader;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    @JsonProperty("2")
    @JsonDeserialize(using = CDDeserializer.class)
    public CharacterData[] characterData;

    @JsonProperty("6")
    public int type; // 阵容适用的副本

    public Data() {
    }

    public static class CDDeserializer extends JsonDeserializer<CharacterData[]> {

        @Override
        public CharacterData[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode treeNode = p.getCodec().readTree(p);
            CharacterData[] result = new CharacterData[treeNode.size()];

            for (int i = 0; i < treeNode.size(); i++) {
                JsonNode node = treeNode.get(i);
                Class<? extends CharacterData> targetType = (i == 0) ? YYSData.class : HeroData.class;
                result[i] = ctxt.readTreeAsValue(node, targetType);
            }
            return result;
        }
    }
}
