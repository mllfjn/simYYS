package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.list.r.chounv.CaoRen;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

import java.util.List;


/**
 * 拥有该状态的单位不可被选中,但可以正常跑行动条
 */
public class StatusCanNotChoose extends Status implements Displayable {
    public StatusCanNotChoose(Character from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);

        List<Character> teammates = new CharacterFinder(belongTo)
                .filterTeammate()
                .getList();

        for (Character teammate : teammates) {
            if (teammate instanceof CaoRen cr && cr.getBind() == belongTo) {
                cr.die();
                break;
            }
        }


    }

    @Override
    public String getDisplayText() {
        return "无法选中";
    }
}
