package com.mllfjn.simyys.character.status;

public enum StatusDurationType {
    CHI_XU, // 持续，在目标回合结束后减少一回合
    WEI_CHI, // 维持,指此状态或印记,存在回合数以来源的回合计算,而非目标的回合
    NONE // 无,此状态不会减少回合数
}
