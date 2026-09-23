package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;

class StatusUse3Flag extends Status {
    public StatusUse3Flag(LaoTou laoTou) {
        super("SP老头使用三技能标记", laoTou);
        duration(StatusDurationType.WEI_CHI, 1);
    }
}
