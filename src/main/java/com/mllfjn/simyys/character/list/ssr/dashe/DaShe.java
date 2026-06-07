package com.mllfjn.simyys.character.list.ssr.dashe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterShiShenBase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DaShe extends CharacterShiShenBase {
    public static final String CharacterName = "八岐大蛇";

    private List<CharacterSheMo> sheMoList;

    private Skill2 skill2;
    private Skill3 skill3;

    void addMo(Character target) {
        target.addStatusOrChange(StatusMo.class, statusMo -> statusMo.addStack(skill2),
                () -> new StatusMo(this, target)
        );
    }

    void addSheMo(CharacterSheMo sheMo) {
        if (sheMoList == null) {
            sheMoList = new ArrayList<>();
        }
        sheMoList.add(sheMo);
    }

    void removeSheMo(CharacterSheMo sheMo) {
        sheMoList.remove(sheMo);
    }

    Optional<CharacterSheMo> getMinHpSheMo() {
        if (sheMoList == null || sheMoList.isEmpty()) {
            return Optional.empty();
        } else {
            return sheMoList.stream().min(Comparator.comparing(CharacterSheMo::getHp));
        }
    }

    int getSheMoCount() {
        if (sheMoList == null) {
            return 0;
        } else {
            return sheMoList.size();
        }
    }

    List<CharacterSheMo> getSheMoList() {
        return sheMoList;
    }

    @Override
    protected boolean useSkillAuto() {
        return skill3.tryUse(getBp()) || skill2.tryUse(getBp());
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return false;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "4074";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));

        skill3 = new Skill3(this, skill3Level);
        skill2 = new Skill2(this, skill2Level, skill3);

        if (skill2Level > 0) {
            addSkill(skill2);
        }

        addSkill(skill3);
    }
}
