package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;

public class ELou extends YuHun implements YuHunSealResponse {
    public static final String YuHunName = "恶楼";

    private StatusELLock lock;

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        lock = new StatusELLock(character);

        character.bp.atBattleStart(() -> {
            character.addStatus(new StatusELZL(character));
            yuHunEffect();
        });
    }

    @Override
    public String getName() {
        return YuHunName;
    }

    @Override
    public void enable() {
        if (lock != null) {
            character.addStatus(lock);
        }
    }

    @Override
    public void disable() {
        if (lock != null) {
            character.removeStatus(lock);
        }
    }

    static class StatusELZL extends Status implements Displayable, AttributeModifier {
        private static final String StatusName = "恶楼之力";

        private StatusELZL(Character character) {
            super(character, character, StatusType.BUFF, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return (attribute == Attribute.ZENG_SHANG || attribute == Attribute.JIAN_SHANG)
                    && !belongTo.isHaveStatus(StatusELLock.class);
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 80;
        }

        @Override
        public String getDisplayText() {
            return StatusName;
        }
    }

    class StatusELLock extends Status implements Displayable {
        private static final String StatusName = "恶楼封印";

        private StatusELLock(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            setDurationType(StatusDurationType.CHI_XU, 8);
        }

        @Override
        public void beforeDelete() {
            ELou.this.lock = null;
            ELou.this.yuHunEffect();
        }

        @Override
        public String getDisplayText() {
            return StatusName + getDuration();
        }
    }
}
