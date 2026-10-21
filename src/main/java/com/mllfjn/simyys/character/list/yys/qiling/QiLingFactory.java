package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyRequire;
import com.mllfjn.simyys.character.status.*;

import java.util.List;
import java.util.Map;

public class QiLingFactory {

    public static void addOptions(Map<String, Map<String, List<String>>> map) {
        map.put(PropertyKey.QI_LING_KEY, Map.of("契灵", List.of(QiLingHuoLing.QiLingName,
                QiLingZhenMuShou.QiLingName,
                QiLingTiHun.QiLingName,
                QiLingYueMoTu.QiLingName,
                QiLingZhenNv.QiLingName))
        );
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
        switch (s) {
            case QiLingHuoLing.QiLingName -> QiLingHuoLing.install(character);
            case QiLingZhenMuShou.QiLingName -> QiLingZhenMuShou.install(character);
            case QiLingTiHun.QiLingName -> QiLingTiHun.install(character);
            case QiLingYueMoTu.QiLingName -> QiLingYueMoTu.install(character);
            case QiLingZhenNv.QiLingName -> QiLingZhenNv.install(character);
        }
    }

    public static void yuHunEffect(Character character, String name) {
        character.bp.interactive.addYuHunEffectLog(character, name);
    }
}