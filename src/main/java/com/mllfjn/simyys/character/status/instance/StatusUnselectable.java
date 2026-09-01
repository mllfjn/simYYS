package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.list.r.chounv.CaoRen;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.Status;


/**
 * 拥有该状态的单位不可被选中,但可以正常跑行动条
 */
public class StatusUnselectable extends Status {
    public StatusUnselectable(Character from, Character belongTo) {
        super("无法选中", from, belongTo);
        belongTo.bp.situation.unSelectable(belongTo);

        Character caoRen = new CharacterFinder(belongTo)
                .filterTeammate()
                .filter(character -> character instanceof CaoRen cr && cr.getBind() == belongTo)
                .getFirst();

        if (caoRen != null) {
            caoRen.die();
        }
        displayName();
        beforeDelete(() -> belongTo.bp.situation.selectable(belongTo));
    }
}
