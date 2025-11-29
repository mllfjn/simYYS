package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.status.Status;
import com.mllfjn.simyys.status.StatusForm;
import com.mllfjn.simyys.status.StatusDurationType;
import com.mllfjn.simyys.status.StatusType;

class StatusUse3Flag extends Status {
    public StatusUse3Flag(LaoTou laoTou) {
        super(laoTou, laoTou, StatusType.SPECIAL, StatusForm.SPECIAL);
        setSettleType(StatusDurationType.WEI_CHI, 1);
    }
}
