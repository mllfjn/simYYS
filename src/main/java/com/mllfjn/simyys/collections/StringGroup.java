package com.mllfjn.simyys.collections;

import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.citiao.*;
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
            new StringGroup("攻击两件套",
                    KuangGu.YuHunName,
                    LunRuDao.YuHunName
            ),
            new StringGroup("暴击两件套",
                    QingNvFang.YuHunName,
                    ShangHunNiao.YuHunName,
                    HaiYueHuoYu.YuHunName,
                    ZhenMuShou.YuHunName,
                    YingShengChong.YuHunName
            ),
            new StringGroup("命中两件套",
                    HuoLing.YuHunName,
                    YuanXingSi.YuHunName,
                    YiNianHuo.YuHunName
            ),
            new StringGroup("抵抗两件套",
                    DiaoPingHuo.YuHunName
            ),
            new StringGroup("生命两件套",
                    TuFo.YuHunName
            ),
            new StringGroup("防御两件套",
                    HuoZhiChe.YuHunName,
                    ZhenZhu.YuHunName,
                    ZhaoCaiMao.YuHunName,
                    RiNvSiShi.YuHunName,
                    FengHaiTu.YuHunName
            ),
            new StringGroup("首领两件套",
                    TuZhiZhu.YuHunName,
                    DiZhenNian.YuHunName,
                    ShenQiLou.YuHunName,
                    HuangKuLou.YuHunName,
                    LongChe.YuHunName
            ),
    };
}
