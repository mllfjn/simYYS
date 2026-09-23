package com.mllfjn.simyys.interactive;

public enum AttackType {
    DAN_TI("单体"),
    QUN_TI("群体"),
    JIAN_JIE("间接"),
    ZHEN_SHI("真实"),
    CHUAN_DAO("传导"),
    GU_DING("固定"),
    ;
    private final String desc;

    AttackType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
