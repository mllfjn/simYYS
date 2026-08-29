package com.mllfjn.simyys.character.list.sp.laotou;

import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.StatusType;

class StatusUse3Flag extends Status {
    public StatusUse3Flag(LaoTou laoTou) {
        super(laoTou, laoTou, StatusType.SPECIAL, StatusForm.SPECIAL);
        duration(StatusDurationType.WEI_CHI, 1);
    }
}
