package com.mllfjn.simyys.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.File;
import java.util.Base64;
import java.util.zip.Inflater;

public class QRCodeLoader {

    public static void loadQRCode(Window ownerWindow) {
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
                    System.out.println(data);
                }

            } catch (Exception e) {
                Utils.throwException("读取阵容码时出错", e);
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        @JsonProperty("2")
        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        public HeroData[] heroData;

        public Data() {}
    }

    public static class HeroData {
        public int hero_id;
        public int star; // 星级
        public int level; // 等级
        public boolean awake; // 觉醒
        public Object[] skills; // 技能,阴阳师为二维数组,式神一维

        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        public Field5 field5;

        public Object field6;
        public Object field7;
        public Object lockSkill; // 锁定技能,妖术为11
        public Object field9;

        public HeroData() {}
    }

    public static class Field5 {
        public Object field0;
        public Object field1;
        public Object field2;
        public Object field3;
        public Object[] equips; // 御魂 [0]为四件套,[1]为二件套

        @JsonFormat(shape = JsonFormat.Shape.ARRAY)
        public AttributeLimit attributeLimit;

        public Object field6; // 二四六主属性,此处无用
    }

    public static class AttributeLimit {
        public Object attack;
        public Object crit_rate;
        public Object crit_power;
        public Object speed;
        public Object defense;
        public Object hp;
        public Object effect_hit_rate;
        public Object effect_resist_rate;
        public Object field8;
    }
}
