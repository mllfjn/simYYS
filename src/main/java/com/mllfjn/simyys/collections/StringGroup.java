package com.mllfjn.simyys.collections;

import com.mllfjn.simyys.character.list.mob.jifengmo.*;
import com.mllfjn.simyys.character.list.yys.QiLingFactory;
import com.mllfjn.simyys.character.yuhun.list.*;

import java.io.Serializable;

public record StringGroup(String label, String... values) implements Serializable {
    public final static StringGroup[] JI_FENG_MO_CI_TIAO
            = new StringGroup[]{new StringGroup("词条"
            , CiTiao1QiaoJin.CiTiaoName
            , CiTiao2YiSui.CiTiaoName
            , CiTiao3ZhouShu.CiTiaoName
            , CiTiao4MengHuo.CiTiaoName
            , CiTiao5KuangFeng.CiTiaoName
            , CiTiao6DouHun.CiTiaoName
            , CiTiao7JiXing.CiTiaoName
    )};

    public final static StringGroup[] YU_HUN = new StringGroup[]{
            new StringGroup("攻击两件套", KuangGu.YuHunName),
            new StringGroup("暴击两件套", QingNvFang.YuHunName, ShangHunNiao.YuHunName),
            new StringGroup("命中两件套", HuoLing.YuHunName),
            new StringGroup("防御两件套", HuoZhiChe.YuHunName),
            new StringGroup("首领两件套", TuZhiZhu.YuHunName, DiZhenNian.YuHunName, ShenQiLou.YuHunName),
    };
    public final static StringGroup[] QI_LING
            = new StringGroup[]{new StringGroup("契灵", QiLingFactory.ZhenMuShou, QiLingFactory.HuoLing)};
}
