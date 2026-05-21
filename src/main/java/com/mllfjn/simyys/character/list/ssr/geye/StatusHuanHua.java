package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.instance.StatusCanNotChoose;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

class StatusHuanHua extends StatusCanNotChoose {

    StatusHuanHua(Character from, Character belongTo) {
        super(from, belongTo);
        StatusHHSpeed.addStack(from);

        belongTo.bp.removeFromList(belongTo);
        belongTo.doIfCharacterIconExist(characterIcon -> {
            // 隐去状态栏(top)和属性栏(bottom)
            characterIcon.setVisualEffectTop(node -> node.setVisible(false));
            characterIcon.setVisualEffectBottom(node -> node.setVisible(false));

            // 头像幻化
            characterIcon.setVisualEffectCenter(node -> {
                node.setOpacity(0.6);

                DropShadow glow = new DropShadow();
                glow.setColor(Color.PURPLE);
                glow.setRadius(20);
                glow.setSpread(0.3);
                glow.setBlurType(BlurType.GAUSSIAN);

                node.setEffect(glow);
            });
        });
    }

    @Override
    public void beforeDelete() {
        belongTo.bp.addToList(belongTo);
        belongTo.doIfCharacterIconExist(characterIcon -> {
            characterIcon.setVisualEffectTop(node -> node.setVisible(true));
            characterIcon.setVisualEffectBottom(node -> node.setVisible(true));

            characterIcon.setVisualEffectCenter(node -> {
                node.setEffect(null);
                node.setOpacity(1);
            });
        });
    }

    @Override
    public String getDisplayText() {
        return "幻化";
    }

    static class StatusHHSpeed extends Status implements AttributeModifier {
        private int stack = 1;

        private StatusHHSpeed(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        static void addStack(Character character) {
            character.getStatus(StatusHHSpeed.class)
                    .ifPresentOrElse(
                            status -> status.stack++,
                            () -> character.addStatus(new StatusHHSpeed(character))
                    );
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return belongTo.getInitSpeed() * 0.15 * stack;
        }
    }
}
