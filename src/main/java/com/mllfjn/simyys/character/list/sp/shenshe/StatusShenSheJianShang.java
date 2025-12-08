package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageBeingAttack;

public class StatusShenSheJianShang extends Status implements InfluenceDamageBeingAttack {
    private int count;

    public StatusShenSheJianShang(Character character) {
        super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
    }

    public static void add(Character character) {
        character.getStatus(StatusShenSheJianShang.class)
                .or(() -> character.addStatus(new StatusShenSheJianShang(character)))
                .ifPresent(StatusShenSheJianShang::add);
    }

    public static void reduce(Character character) {
        character.getStatus(StatusShenSheJianShang.class).ifPresent(StatusShenSheJianShang::reduce);
    }

    private void add() {
        count++;
    }

    private void reduce() {
        count--;
        if (count == 0) {
            delete();
        }
    }

    @Override
    public void doInfluenceBeingAttack(AttackType attackType, AttackInfo attackInfo) {
        // 每存在1把(count),神堕八岐大蛇受到的伤害减少20%
        attackInfo.getTraceableNumber().mul(Math.max(0, 1 - count * 0.2), "神蛇堕落之剑减伤");
    }
}
