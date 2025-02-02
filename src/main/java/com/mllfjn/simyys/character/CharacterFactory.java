package com.mllfjn.simyys.character;

import com.mllfjn.simyys.character.SP.dayuan.daYuan;
import com.mllfjn.simyys.starter.info.CharacterInfo;

public class CharacterFactory {
    public static final String[] characterListYYS = {"晴明", "神乐", "八百比丘尼", "源博雅"};
    public static final String[] characterListSP = {"纺愿缘结神", "神堕八岐大蛇", "因幡辉夜姬", "浮世青行灯"};
    public static final String[] characterListSSR = {"伊邪那美", "天照", "千姬", "缘结神"};
    public static final String[] characterListSR = {"蝎女"};
    public static final String[] characterListR = {"丑时之女"};
    public static final String[] characterListN = {};
    public static final String[] characterListMob = {"鬼灵歌姬", "蜃气楼", "土蜘蛛", "荒骷髅", "地震鲶", "胧车", "达摩"};
    public static final String[][] characterList = new String[][]{characterListYYS, characterListSP, characterListSSR, characterListSR, characterListR, characterListN, characterListMob};
    public static final String[] characterType = new String[]{"阴阳师", "SP", "SSR", "SR", "R", "N", "怪物"};
    /*public static Character createCharacter(String name,
                                            double baseSpeed,
                                            double basicAttack,
                                            double yuHunAttack,
                                            int team,
                                            double baseHp,
                                            double baseDefense,
                                            double baseCriticalRate,
                                            double baseCriticalMultiplier,
                                            double mingZhong,
                                            double diKang){
        Character character = switch (name) {
            //case "纺愿缘结神" -> new SPyuan();
            default -> null;
        };
        if (character != null){
            //character.init(name,baseSpeed,basicAttack,yuHunAttack,team,baseHp,baseDefense,baseCriticalRate,baseCriticalMultiplier,mingZhong,diKang);
        }
        return character;
    }*/

    public static Character createCharacter(CharacterInfo info) {
        Character character = switch (info.name) {
            case "纺愿缘结神" -> new daYuan();
            default -> null;
        };

        if (character != null) {
            character.init(info);
        }

        return character;
    }
}
