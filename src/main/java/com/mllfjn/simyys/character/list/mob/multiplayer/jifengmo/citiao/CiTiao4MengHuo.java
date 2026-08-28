package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;

public class CiTiao4MengHuo {
    public static final String CiTiaoName = "猛火";

    public static void install(Character character) {
        // 鬼火消耗降低2点
        character.bp.addStatusAdder(c -> new Status(CiTiaoName, character, c).forceChangeSkillCost(-2));
    }
}
