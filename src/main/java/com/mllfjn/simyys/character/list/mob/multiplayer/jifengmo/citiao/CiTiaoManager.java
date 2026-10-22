package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;

import java.util.List;
import java.util.Map;

public class CiTiaoManager {

    public static void addOptions(Map<String, Map<String, List<String>>> map) {
        map.put(PropertyKey.JI_FENG_MO_CI_TIAO_KEY, Map.of("词条", List.of(
                CiTiao1QiaoJin.CiTiaoName,
                CiTiao2YiSui.CiTiaoName,
                CiTiao3ZhouShu.CiTiaoName,
                CiTiao4MengHuo.CiTiaoName,
                CiTiao5KuangFeng.CiTiaoName,
                CiTiao6DouHun.CiTiaoName,
                CiTiao7JiXing.CiTiaoName))
        );
    }

    public static void installCiTiao(String ciTiao, Character character) {
        if (ciTiao == null || ciTiao.isEmpty()) {
            return;
        }
        switch (ciTiao) {
            case CiTiao1QiaoJin.CiTiaoName -> CiTiao1QiaoJin.install(character);
            case CiTiao2YiSui.CiTiaoName -> CiTiao2YiSui.install(character);
            case CiTiao3ZhouShu.CiTiaoName -> CiTiao3ZhouShu.install(character);
            case CiTiao4MengHuo.CiTiaoName -> CiTiao4MengHuo.install(character);
            case CiTiao5KuangFeng.CiTiaoName -> CiTiao5KuangFeng.install(character);
            case CiTiao6DouHun.CiTiaoName -> CiTiao6DouHun.install(character);
            case CiTiao7JiXing.CiTiaoName -> CiTiao7JiXing.install(character);
        }
    }
}
