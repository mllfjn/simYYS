package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.propertygetter.FlagChangeInfo;
import com.mllfjn.simyys.character.status.instance.StatusCanNotChoose;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CharacterFinder {
    private final Character owner;
    private final BattlePane bp;

    private Stream<Character> stream;

    private TargetTeam targetTeam;

    public CharacterFinder(Character owner) {
        this(owner, false);
    }

    public CharacterFinder(Character owner, boolean forceGetList) {
        this.owner = owner;
        this.bp = owner.bp;

        stream = bp.situation.characters.stream();

        if (!forceGetList) {
            stream = stream.filter(character -> !character.isHaveStatus(StatusCanNotChoose.class));
        }
    }

    public CharacterFinder filterYYS(boolean include) {
        stream = include ?
                stream.filter(Character::isYYS) :
                stream.filter(character -> !character.isYYS());
        return this;
    }

    public CharacterFinder filterSummon(boolean include) {
        stream = include ?
                stream.filter(Character::isSummon) :
                stream.filter(character -> !character.isSummon());
        return this;
    }

    public CharacterFinder filterShiShen() {
        stream = stream.filter(character -> !character.isSummon() && !character.isYYS());
        return this;
    }

    public CharacterFinder filterTeammate() {
        this.targetTeam = TargetTeam.TEAMMATE;
        stream = stream.filter(character -> character.team == owner.team);
        return this;
    }

    public CharacterFinder filterEnemy() {
        this.targetTeam = TargetTeam.ENEMY;
        stream = stream.filter(character -> character.team != owner.team);
        return this;
    }

    public CharacterFinder filterSelf() {
        stream = stream.filter(character -> character != owner);
        return this;
    }

    public CharacterFinder filter(Predicate<Character> predicate) {
        stream = stream.filter(predicate);
        return this;
    }

    public Character getPriorAuto(Attribute attribute, Criteria criteria) {
        // 如果自动目标存在,且列表中有该目标,直接返回
        // 否则返回和get方法一样的结果
        Character auto = getAuto();

        List<Character> list = getList();
        if (auto != null && list.contains(auto)) {
            return auto;
        } else {
            stream = list.stream();
            return get(attribute, criteria);
        }
    }

    public Character getAutoOrElseRandom() {
        List<Character> list = getList();
        Character auto = getAuto();
        if (auto != null && list.contains(auto)) {
            return auto;
        } else {
            stream = list.stream();
            return getRandom();
        }
    }

    public Character getAuto() {
        FlagChangeInfo.FlagType flagType;

        if (targetTeam == TargetTeam.TEAMMATE) {
            flagType = FlagChangeInfo.FlagType.GREEN;
        } else {
            flagType = FlagChangeInfo.FlagType.RED;
        }

        return bp.situation.getAutoTo(owner.team, flagType).orElse(null);
    }

    public Character get(Attribute attribute, Criteria criteria) {
        Comparator<Character> comparator = Comparator.comparing(attribute.getGetter());
        return (criteria == Criteria.MAX ? stream.max(comparator) : stream.min(comparator)).orElse(null);
    }

    public Character getRandom() {
        List<Character> list = getList();
        if (list.isEmpty()) {
            return null;
        }
        return RateController.choose("请选择攻击对象", list, Character::getName, bp.calc);
    }

    public Character getFirst() {
        return stream.findFirst().orElse(null);
    }

    public List<Character> getList() {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }

    public int getCount() {
        return Math.toIntExact(stream.count());
    }

    public static int getEnemyTeam(int team) {
        return 1 - team;
    }

    public enum Criteria {
        MAX,
        MIN
    }

    public enum TargetTeam {
        ENEMY,
        TEAMMATE
    }
}
