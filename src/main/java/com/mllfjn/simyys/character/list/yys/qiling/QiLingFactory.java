package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyRequire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class QiLingFactory {
    public static final Map<String, QiLingMeta> NAME_MAP = new HashMap<>();
    public static final Map<Integer, QiLingMeta> ID_MAP = new HashMap<>();

    private static final List<String> QI_LING_LIST = new ArrayList<>();

    static {
        putQiLingMeta(100, QiLingZhenMuShou.QiLingName, QiLingZhenMuShou::install);
        putQiLingMeta(101, QiLingHuoLing.QiLingName, QiLingHuoLing::install);
        putQiLingMeta(104, QiLingZhenNv.QiLingName, QiLingZhenNv::install);
        putQiLingMeta(105, QiLingTiHun.QiLingName, QiLingTiHun::install);
        putQiLingMeta(106, QiLingYueMoTu.QiLingName, QiLingYueMoTu::install);
    }

    private static void putQiLingMeta(int id, String name, Consumer<Character> installer) {
        QiLingMeta meta = new QiLingMeta(id, name, installer);
        ID_MAP.put(id, meta);
        NAME_MAP.put(name, meta);
        QI_LING_LIST.add(name);
    }

    public static void addOptions(Map<String, Map<String, List<String>>> map) {
        map.put(PropertyKey.QI_LING_KEY, Map.of("契灵", QI_LING_LIST));
    }

    public static void addQiLing(PropertiesMap map, Character character) {
        PropertyRequire propertyRequire = map.get(PropertyKey.QI_LING_KEY);
        if (propertyRequire == null) {
            return;
        }
        String s = propertyRequire.getString();
        if (s == null || s.isEmpty()) {
            return;
        }

        QiLingMeta meta = NAME_MAP.get(s);
        if (meta != null) {
            meta.installer.accept(character);
        }
    }

    public static void yuHunEffect(Character character, String name) {
        character.bp.interactive.addYuHunEffectLog(character, name);
    }

    public record QiLingMeta(int id, String name, Consumer<Character> installer) {}
}