package com.mllfjn.simyys.utils.qrcodeloader;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.yuhun.EquipFactory;

import java.io.IOException;
import java.util.Map;
import java.util.StringJoiner;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class Field5 {
    public Object field0;
    public Object field1;
    public Object field2;
    public Object field3;

    @JsonDeserialize(using = EquipsDeserializer.class)
    public String equips; // 御魂

    public AttributesLimit attributesLimit;
    public Object field6; // 二四六主属性,无用

    public Field5() {
    }

    public void get(PropertiesMap map, Map<LoadAttribute, Float> maxAttributeMap) {
        map.get(PropertyKey.YU_HUN_KEY).setValue(equips);
        attributesLimit.get(map, maxAttributeMap);
    }

    public static class EquipsDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            if (p.currentToken() == JsonToken.VALUE_NUMBER_INT) {
                // 直接为0,代表没有御魂
                return null;
            } else {
                // [0]为四件套,[1]为二件套,注意[1]中可能会出现四件套,在此应当忽略

                StringJoiner sj = new StringJoiner(",");
                if (p.nextToken() == JsonToken.START_ARRAY) {
                    // 四件套存在
                    addEquip(sj, p.readValueAs(int[].class), 4);
                }

                if (p.nextToken() == JsonToken.START_ARRAY) {
                    // 二件套存在
                    addEquip(sj, p.readValueAs(int[].class), 2);
                }

                // 这里测试时发现有可能出现[2]
                do {
                    p.nextToken();
                } while (p.currentToken() != JsonToken.END_ARRAY);

                if (sj.length() > 0) {
                    return sj.toString();
                } else {
                    return null;
                }
            }
        }

        private void addEquip(StringJoiner sj, int[] ids, int setCount) {
            for (int id : ids) {
                EquipFactory.EquipMeta equipMeta = EquipFactory.ID_MAP.get(300000 + id);
                if (equipMeta != null && equipMeta.setCount() == setCount) {
                    sj.add(equipMeta.name());
                }
            }
        }
    }
}
