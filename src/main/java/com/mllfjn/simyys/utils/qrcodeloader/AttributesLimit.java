package com.mllfjn.simyys.utils.qrcodeloader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

@JsonDeserialize(using = AttributesLimit.ASLDeserializer.class)
public class AttributesLimit {
    AttributeLimit[] limits;

    public Object field8 = null; // 该字段未读,跳过了

    public AttributesLimit(AttributeLimit[] limits) {
        this.limits = limits;
    }

    public void get(PropertiesMap map, Map<LoadAttribute, Float> maxAttributeMap) {
        Random random = new Random();
        LoadAttribute[] loadAttributes = LoadAttribute.values();
        for (int i = 0; i < limits.length; i++) {
            LoadAttribute loadAttribute = loadAttributes[i];

            float max = loadAttribute.max;
            // 最高属性
            Float maxAttribute = maxAttributeMap.get(loadAttribute);
            if (maxAttribute != null) {
                max = maxAttribute;
            }

            float value = limits[i].get(loadAttribute.min, max, random);

            if (loadAttribute == LoadAttribute.ATTACK) {
                value -= (float) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY).getDouble();
            }
            loadAttribute.setter.apply(map).setValue(value);
        }

    }

    public static class ASLDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            AttributeLimit[] limits = new AttributeLimit[LoadAttribute.values().length];
            for (int i = 0; i < limits.length; i++) {
                if (p.nextToken() == JsonToken.VALUE_NUMBER_INT) {
                    limits[i] = new AttributeLimit(-1, -1);
                } else {
                    float[] value = p.readValueAs(float[].class);
                    limits[i] = new AttributeLimit(value[0], value[1]);
                }
            }

            // 跳过field8:可能是0代表空,也可能是数组
            if (p.nextToken() == JsonToken.START_ARRAY) {
                p.skipChildren();
            }
            p.nextToken();

            return new AttributesLimit(limits);
        }
    }
}
