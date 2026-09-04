package com.mllfjn.simyys.character.yuhun.list;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.YuHunSealResponse;
import javafx.scene.paint.Color;

public class ELou extends Equip implements YuHunSealResponse {
    public static final String YuHunName = "恶楼";

    private StatusELou eLou;
    private Status lock;

    @Override
    public void init(Character character, boolean isInit) {
        super.init(character, isInit);
        character.bp.addPriorityMove(character, () -> {

            eLou = new StatusELou(character);

            lock = Status.of("恶楼封印", character);
            lock.duration(StatusDurationType.CHI_XU, 8)
                    .displayNameAndDuration()
                    .setColor(Color.RED)
                    .beforeDelete(() -> {
                        eLou.enable();
                        lock = null;
                        ELou.this.yuHunEffect();
                    });

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
            eLou.disable();
        }
    }

    @Override
    public void disable() {
        if (lock != null) {
            character.removeStatus(lock);
            eLou.enable();
        }
    }

    private static class StatusELou extends Status {
        public StatusELou(Character character) {
            super("恶楼之力", character);
            type(StatusType.BUFF, StatusForm.SPECIAL)
                    .displayName()
                    .setColor(Color.ORANGE)
                    .addTo();
        }

        void enable() {
            attribute(Attribute.ZENG_SHANG, 80);
            attribute(Attribute.JIAN_SHANG, 80);
        }

        void disable() {
            removeAttribute(Attribute.ZENG_SHANG);
            removeAttribute(Attribute.JIAN_SHANG);
        }
    }
}
