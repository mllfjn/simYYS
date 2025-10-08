package com.mllfjn.simyys.character.propertygetter;

import java.io.Serializable;

public record StringGroup(String label, String[] values) implements Serializable {
    public final static StringGroup[] JI_FENG_MO_CI_TIAO = new StringGroup[]{new StringGroup("词条", new String[]{"巧劲", "易碎", "咒术", "猛火", "狂风", "斗魂", "疾行"})};
    public final static StringGroup[] YU_HUN = new StringGroup[]{
//            new StringGroup("攻击两件套", )
    };
}
