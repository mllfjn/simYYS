package com.mllfjn.simyys.customnode;

public record StringGroup(String label, String[] values) {
    public final static StringGroup JI_FENG_MO_CI_TIAO = new StringGroup("词条", new String[]{"巧劲", "易碎", "咒术", "猛火", "狂风", "斗魂", "疾行"});
}
