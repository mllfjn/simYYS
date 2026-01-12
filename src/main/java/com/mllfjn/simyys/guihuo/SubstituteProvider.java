package com.mllfjn.simyys.guihuo;

public interface SubstituteProvider {
    int getGuiHuo(int num, boolean isFromYuHun);

    boolean canUse(int num);

    void use(int num);

    String getSubstituteProviderName();
}
