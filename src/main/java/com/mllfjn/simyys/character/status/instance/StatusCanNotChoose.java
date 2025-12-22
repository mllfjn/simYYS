package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;


/**
 * 拥有该状态的单位不可被选中,但可以正常跑行动条
 */
public class StatusCanNotChoose extends Status {
    public StatusCanNotChoose(Character from, Character belongTo) {
        super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
    }
}
