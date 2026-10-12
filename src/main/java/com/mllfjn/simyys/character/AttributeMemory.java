package com.mllfjn.simyys.character;

import com.mllfjn.simyys.utils.DecimalFormatUtil;

import java.io.Serializable;
import java.util.List;

public class AttributeMemory implements Serializable {
    private final Character character;
    public final Attribute attribute;

    private double lastNum;
    private boolean alwaysShow;

    public AttributeMemory(Character character, Attribute attribute) {
        this.character = character;
        this.attribute = attribute;
        lastNum = attribute.getGetter().apply(character);
    }

    public void refresh(List<String> list) {
        double num = attribute.getGetter().apply(character);

        if (num != lastNum) {
            // 攻击:100->200
            list.add(attribute.getText() + ":\n"
                    + DecimalFormatUtil.df_0_2.format(lastNum)
                    + "->"
                    + DecimalFormatUtil.df_0_2.format(num)
            );
            lastNum = num;
        } else if (alwaysShow) {
            list.add(attribute.getText() + ":\n" + DecimalFormatUtil.df_0_2.format(num));
        }
    }

    public void setAlwaysShow(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
    }
}
