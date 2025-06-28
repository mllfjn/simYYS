package com.mllfjn.simyys.character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum CharacterType {
    SHI_SHEN,
    YYS,
    MOB,
    SUMMON,
    UNDEFINED;

    public static CharacterType getType(String name) {
        for (int i = 1; i < CharacterFactory.characterList.length - 1; i++) {
            for (String s : CharacterFactory.characterList[i]) {
                if (name.equals(s)) {
                    return SHI_SHEN;
                }
            }
        }

        String[] arrayMob = {"鬼灵歌姬", "蜃气楼", "土蜘蛛", "荒骷髅", "地震鲶", "胧车"};
        List<String> listMob = new ArrayList<>(Arrays.asList(arrayMob));
        if (listMob.contains(name)) {
            return MOB;
        }

        List<String> listYYS = new ArrayList<>(Arrays.asList(CharacterFactory.characterListYYS));
        if (listYYS.contains(name)) {
            return YYS;
        }

        return UNDEFINED;
    }
}
