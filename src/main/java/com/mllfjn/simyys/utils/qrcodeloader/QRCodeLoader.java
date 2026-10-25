package com.mllfjn.simyys.utils.qrcodeloader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian.DiZhenNian;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou.HuangKuLou;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou.ShenQiLou;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu.TuZhiZhu;
import com.mllfjn.simyys.character.propertygetter.*;
import com.mllfjn.simyys.starter.Initializer;
import com.mllfjn.simyys.utils.Utils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.File;
import java.util.*;
import java.util.zip.Inflater;

public class QRCodeLoader {

    private static final Map<Integer, String> TYPE_MAP = Map.of(
            10020219, ShenQiLou.CharacterName,
            10020221, TuZhiZhu.CharacterName,
            10020223, HuangKuLou.CharacterName,
            10020225, DiZhenNian.CharacterName
    );

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
                Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
                hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
                hints.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.of(BarcodeFormat.QR_CODE));

                Result result = new MultiFormatReader().decode(bitmap, hints);
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

                    /*// 测试用
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < length; i++) {
                        builder.append(String.format("%02x", decompressedBytes[i]));
                    }
                    System.out.println("QRCodeLoader:\n" + builder);*/

                    ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());
                    Data data = mapper.readValue(decompressedBytes, Data.class);

                    Map<LoadAttribute, Float> maxAttributeMap = new EnumMap<>(LoadAttribute.class);
                    for (CharacterData characterData : data.characterData) {
                        CharacterFactory.CharacterMeta meta = CharacterFactory.idMap.get(characterData.hero_id);
                        if (meta != null) {
                            PropertiesHolder ph = initializer.addCharacter(meta.name());
                            characterData.get(ph, maxAttributeMap);
                        }
                    }

                    if (data.type != 0) {
                        CharacterFactory.CharacterMeta meta = CharacterFactory.nameMap.get(TYPE_MAP.get(data.type));
                        if (meta != null) {
                            initializer.addCharacter(meta.name());
                        }
                    }
                }

            } catch (NotFoundException e) {
                Utils.error("未识别到二维码");
            } catch (Exception e) {
                Utils.throwException("读取阵容码时出错", e);
            }
        }
    }
}
