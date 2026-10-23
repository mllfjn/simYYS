package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Status;

// 这是神蛇被吸攻击的队友身上的状态
class StatusStoreAttack extends Status {
    public StatusStoreAttack(Character from, Character belongTo) {
        super("攻击被吸取", from, belongTo);
        enableStackAndAddStack(Integer.MAX_VALUE);
        // 每被吸一次减少6%
        attribute(Attribute.ATTACK, _ -> -0.06 * getStack() * belongTo.getInitAttack());
    }

    public static void addStack(Character from, Character character) {
        character.getStatus(StatusStoreAttack.class)
                .ifPresentOrElse(
                        StatusStoreAttack::addStack,
                        () -> character.addStatus(new StatusStoreAttack(from, character))
                );
    }
}
