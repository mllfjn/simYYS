package com.mllfjn.simyys.utils;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.SP.dayuan.DaYuan;
import com.mllfjn.simyys.character.SP.shenshe.ShenShe;
import com.mllfjn.simyys.character.SSR.namei.NaMei;
import com.mllfjn.simyys.character.SSR.qianji.QianJi;
import com.mllfjn.simyys.character.SSR.xiaoyuan.XiaoYuan;
import com.mllfjn.simyys.character.YYS.shenle.ShenLe;
import com.mllfjn.simyys.character.mob.shenqilou.ShenQiLou;

public class CharacterAdapter {
    public static RuntimeTypeAdapterFactory<Character> getCharacterAdapter() {
        return RuntimeTypeAdapterFactory.of(Character.class, "name")
                .registerSubtype(DaYuan.class, "纺愿缘结神")
                .registerSubtype(ShenLe.class, "神乐")
                .registerSubtype(QianJi.class, "千姬")
                .registerSubtype(NaMei.class, "伊邪那美")
                .registerSubtype(ShenShe.class, "神堕八岐大蛇")
                .registerSubtype(XiaoYuan.class, "缘结神")
                .registerSubtype(ShenQiLou.class, "蜃气楼");
    }
}
