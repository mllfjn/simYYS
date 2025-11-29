package com.mllfjn.simyys.status;

public interface CrowdControl {

    // 似乎驱散不需要单独的逻辑,只要是状态就可以驱散
    /*// 可被驱散
    boolean canDispel();*/


    // 可被解除,目前版本只有霜冻无法被解除
    default boolean canRemove() {
        return true;
    }

}
