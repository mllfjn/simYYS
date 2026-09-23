package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

class StatusHuanHua extends Status {

    StatusHuanHua(Character from, Character belongTo) {
        super("幻化", from, belongTo);
        StatusHHSpeed.addStack(from);

        belongTo.bp.situation.canNotChangeLocation(belongTo);
        belongTo.bp.situation.unSelectable(belongTo);

        belongTo.doIfCharacterIconExist(characterIcon -> {
            // 隐去状态栏(top)和属性栏(bottom)
            characterIcon.setVisualEffectTop(node -> node.setVisible(false));
            characterIcon.setVisualEffectBottom(node -> node.setVisible(false));

            // 头像幻化
            characterIcon.setVisualEffectCenter(node -> {
                node.setOpacity(0.6);

                DropShadow glow = new DropShadow();
                glow.setColor(Color.BLUE);
                glow.setRadius(20);
                glow.setSpread(0.3);
                glow.setBlurType(BlurType.GAUSSIAN);

                node.setEffect(glow);
            });
        });

        beforeDelete(() -> {
            belongTo.bp.situation.canChangeLocation(belongTo);
            belongTo.bp.situation.selectable(belongTo);

            StatusHHSpeed.removeStack(from);

            belongTo.doIfCharacterIconExist(characterIcon -> {
                characterIcon.setVisualEffectTop(node -> node.setVisible(true));
                characterIcon.setVisualEffectBottom(node -> node.setVisible(true));

                characterIcon.setVisualEffectCenter(node -> {
                    node.setEffect(null);
                    node.setOpacity(1);
                });
            });
        });
    }

    static class StatusHHSpeed extends Status {
        private int stack = 1;

        private StatusHHSpeed(Character character) {
            super("幻化速度", character);
            attribute(Attribute.SPEED, _ -> belongTo.getInitSpeed() * 0.15 * stack);
        }

        static void addStack(Character character) {
            character.getStatus(StatusHHSpeed.class)
                    .ifPresentOrElse(
                            status -> status.stack++,
                            () -> character.addStatus(new StatusHHSpeed(character))
                    );
        }

        static void removeStack(Character character) {
            character.getStatus(StatusHHSpeed.class)
                    .ifPresent(status -> status.stack--);
        }
    }
}
