package com.mllfjn.simyys.utils.qrcodeloader;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;

import java.io.IOException;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class HeroData extends CharacterData {
    @JsonDeserialize( using = SkillDeserializer.class)
    public String skills; // 技能
    public Field5 field5;
    public int noCalculating; // 不配置御魂开关,为1时应当清除御魂
    public int[] maxAttribute; // 最高属性
    public int lockSkill; // 锁定技能,妖术为11
    public Object field9;

    public HeroData() {
    }

    @Override
    public void get(PropertiesHolder ph, Map<LoadAttribute, Float> maxAttributeMap) {
        if (lockSkill > 0 && lockSkill != 11) {
            ph.lockSkillMap.put(1, lockSkill);
        }

        ph.propertiesMap.get(PropertyKey.SKILL_KEY).setValue(skills);

        field5.get(ph.propertiesMap, maxAttributeMap);

        if (maxAttribute != null) {
            for (int i : maxAttribute) {
                LoadAttribute loadAttribute = LoadAttribute.values()[i];
                maxAttributeMap.put(
                        loadAttribute, ((PropertyInput) loadAttribute.setter.apply(ph.propertiesMap)).getFloat()
                );
            }
        }
    }

    @JsonDeserialize
    public static class SkillDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ArrayNode arr = p.getCodec().readTree(p);
            if (arr.size() == 3) {
                // [5,5,5]
                return join(arr.get(0), arr.get(1), arr.get(2));
            } else {
                // [0,0,0,[4,5],[5,1],[6,5]]
                return join(
                        arr.get(3).get(1),
                        arr.get(4).get(1),
                        arr.get(5).get(1)
                );
            }
        }

        private String join(JsonNode s1,  JsonNode s2, JsonNode s3) {
            return String.valueOf(s1.asInt()) + s2.asInt() + s3.asInt();
        }
    }
}
