package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.list.r.chounv.CaoRen;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;


/**
 * 拥有该状态的单位不可被选中,但可以正常跑行动条
 */
public class StatusUnselectable extends Status implements Displayable {
    public StatusUnselectable(Character from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        belongTo.bp.situation.unSelectable(belongTo);

        Character caoRen = new CharacterFinder(belongTo)
                .filterTeammate()
                .filter(character -> character instanceof CaoRen cr && cr.getBind() == belongTo)
                .getFirst();

        if (caoRen != null) {
            caoRen.die();
        }
    }

    @Override
    public String getDisplayText() {
        return "无法选中";
    }

    @Override
    public void beforeDelete() {
        belongTo.bp.situation.selectable(belongTo);
    }
}
