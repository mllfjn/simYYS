package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesMap;
import com.mllfjn.simyys.character.propertygetter.PropertyRequire;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.collections.StringGroup;

public class QiLingFactory {
    public static final StringGroup[] QI_LING = new StringGroup[]{new StringGroup("契灵",
            QiLingHuoLing.QiLingName,
            QiLingZhenMuShou.QiLingName,
            QiLingTiHun.QiLingName
    )};

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
        }
    }

    public static void yuHunEffect(Character character, String name) {
        character.bp.interactive.addYuHunEffectLog(character, name);
    }
}