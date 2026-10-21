package com.mllfjn.simyys.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.list.yys.CharacterYYSBase;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyMultiInput;
import com.mllfjn.simyys.character.propertygetter.PropertyRequire;
import com.mllfjn.simyys.character.yuhun.EquipFactory;
import com.mllfjn.simyys.starter.Initializer;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.zip.Inflater;

public class QRCodeLoader {

    public static void loadQRCode(Window ownerWindow, Initializer initializer) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("qrCode",
                "*.jpg", "*.png")
        );
        File file = fileChooser.showOpenDialog(ownerWindow);
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            int[] pixels = new int[width * height];
            PixelReader pixelReader = image.getPixelReader();
            pixelReader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
            LuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            try {
                Result result = new MultiFormatReader().decode(bitmap);
                // 二维码内容,形如: #TA#eJxlj89rE0EUx2f2V7f
                String qrContent = result.getText();
                // 去除不知道是什么的开头
                String compressedText = qrContent.substring(4);
                // Base64解码
                byte[] compressedBytes = Base64.getDecoder().decode(compressedText);
                // zlib解压
                try (Inflater inflater = new Inflater()) {
                    inflater.setInput(compressedBytes);
                    byte[] decompressedBytes = new byte[4096];
                    int length = inflater.inflate(decompressedBytes);

                    /*StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < length; i++) {
                        builder.append(String.format("%02x", decompressedBytes[i]));
                    }
                    System.out.println(builder);*/

                    ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());
                    Data data = mapper.readValue(decompressedBytes, Data.class);
//                    System.out.println(data);

                    for (CharacterData characterData : data.characterData) {
                        CharacterFactory.CharacterMeta meta = CharacterFactory.idMap.get(characterData.hero_id);
                        if (meta != null) {
                            PropertiesHolder ph = initializer.addCharacter(meta.name());
                            characterData.get(ph);
                        }
                    }
                }

            } catch (Exception e) {
                Utils.throwException("读取阵容码时出错", e);
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("2")
        @JsonDeserialize(using = CDDeserializer.class)
        public CharacterData[] characterData;

        public Data() {
        }
    }

    public abstract static class CharacterData {
        public int hero_id;
        public int star; // 星级
        public int level; // 等级
        public boolean awake; // 觉醒

        public CharacterData() {
        }

        public abstract void get(PropertiesHolder ph);
    }

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    public static class YYSData extends CharacterData {
        public int[][] skills; // 技能
        public QiLingData qiLingData;
        public int lockSkill; // 锁定技能,妖术为11

        public YYSData() {
        }

        @Override
        public void get(PropertiesHolder ph) {
            if (lockSkill > 0 && lockSkill != 11) {
                ph.lockSkillMap.put(0, lockSkill);
            }

            PropertiesMap map = ph.propertiesMap;

            CharacterYYSBase.YYSSkill[] yysSkills = ((CharacterYYSBase) CharacterFactory.getCharacter(ph.name)).yysSkills;
            for (int i = 0; i < yysSkills.length; i++) {
                changeSkill(yysSkills[i], i, map);
            }
        }

        private void changeSkill(CharacterYYSBase.YYSSkill yysSkill, int index, PropertiesMap map) {
            if (yysSkill == null) {
                return;
            }

            int targetLevel = 0;
            int targetShuYin = 0;
            for (int[] skill : skills) {
                int skillID = skill[0];
                if (skillID == index) {
                    targetLevel = skill[1];
                    targetShuYin = qiLingData.getShuYin(index);
                    break;
                }
            }
            ((PropertyMultiInput) map.get(yysSkill.name()))
                    .setValue(0, String.valueOf(targetLevel))
                    .setValue(1, String.valueOf(targetShuYin));
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    public static class HeroData extends CharacterData {
        public int[] skills; // 技能
        public Field5 field5;
        public int noCalculating; // 不配置御魂开关,为1时应当清除御魂
        public Object field7;
        public int lockSkill; // 锁定技能,妖术为11
        public Object field9;

        public HeroData() {
        }

        @Override
        public void get(PropertiesHolder ph) {
            if (lockSkill > 0 && lockSkill != 11) {
                ph.lockSkillMap.put(0, lockSkill);
            }

            ph.propertiesMap.get(PropertyKey.SKILL_KEY).setValue(
                    skills[0] * 100 + skills[1] * 10 + skills[2]
            );

            field5.get(ph.propertiesMap);
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    public static class Field5 {
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

        public void get(PropertiesMap map) {
            map.get(PropertyKey.YU_HUN_KEY).setValue(equips);
            attributesLimit.get(map);
        }
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

                // 这里测试时发现有可能出现[2]，不知道作用
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

    @JsonDeserialize(using = ASLDeserializer.class)
    public static class AttributesLimit {
        AttributeLimit[] limits;

        public Object field8 = null;

        public AttributesLimit(AttributeLimit[] limits) {
            this.limits = limits;
        }

        public void get(PropertiesMap map) {
            Random random = new Random();
            LoadAttribute[] loadAttributes = LoadAttribute.values();
            for (int i = 0; i < limits.length; i++) {
                LoadAttribute loadAttribute = loadAttributes[i];
                float value = limits[i].get(loadAttribute.min, loadAttribute.max, random);
                if (loadAttribute == LoadAttribute.ATTACK) {
                    value -= (float) map.get(PropertyKey.GENERAL_BASE_ATTACK_KEY).getDouble();
                }
                loadAttribute.setter.apply(map);
            }

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
            return new AttributesLimit(limits);
        }
    }

    public static class AttributeLimit {
        private final float min;
        private final float max;

        public AttributeLimit(float min, float max) {
            this.min = min;
            this.max = max;
        }

        public float get(float defaultMin, float defaultMax, Random random) {
            float useMin = (min == -1) ? defaultMin : min;
            float useMax = (max == -1) ? defaultMax : max;

            return random.nextFloat(useMin, useMax);
        }
    }

    private enum LoadAttribute {
        // 顺序不可改变!
        EFFECT_RESIST_RATE(0, 300, pm -> pm.get(PropertyKey.GENERAL_EFFECT_RESIST_RATE_KEY)),
        CRIT_RATE(10, 220, pm -> pm.get(PropertyKey.GENERAL_CRIT_RATE_KEY)),
        ATTACK(2000, 20000, pm -> pm.get(PropertyKey.GENERAL_YU_HUN_ATTACK_KEY)),
        EFFECT_HIT_RATE(0, 300, pm -> pm.get(PropertyKey.GENERAL_EFFECT_HIT_RATE_KEY)),
        HP(10000, 100000, pm -> pm.get(PropertyKey.GENERAL_HP_KEY)),
        CRIT_POWER(150, 420, pm -> pm.get(PropertyKey.GENERAL_CRIT_POWER_KEY)),
        SPEED(100, 300, pm -> pm.get(PropertyKey.GENERAL_SPEED_KEY)),
        DEFENCE(100, 1200, pm -> pm.get(PropertyKey.GENERAL_DEFENSE_KEY)),
        ;
        public final float min;
        public final float max;
        public final Function<PropertiesMap, ?> setter;
        LoadAttribute(float min, float max, Function<PropertiesMap, PropertyRequire> setter) {
            this.min = min;
            this.max = max;
            this.setter = setter;
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    public static class QiLingData {
        public int id; // 猜测为契灵id
        public int star; // 星级,无用
        public int level; // 等级,无用
        public int filed3;
        public int[] shuYin; // 术印

        public QiLingData() {
        }

        public int getShuYin(int skillID) {
            int count = 0;
            for (int i : shuYin) {
                if (i == skillID) {
                    count++;
                }
            }
            return count;
        }
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
