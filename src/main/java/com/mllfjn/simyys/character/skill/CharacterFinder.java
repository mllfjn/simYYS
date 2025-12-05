package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CharacterFinder {
    private final Character owner;
    private final BattlePane bp;

    private Stream<Character> stream;

    private TargetTeam targetTeam;

    public CharacterFinder(Character owner) {
        this.owner = owner;
        this.bp = owner.bp;
        List<Character> characters = bp.situation.characters;

        stream = characters.stream();
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

    public CharacterFinder filterMob(boolean include) {
        stream = include ?
                stream.filter(Character::isMob) :
                stream.filter(character -> !character.isMob());
        return this;
    }

    public CharacterFinder filterShiShen() {
        stream = stream.filter(character -> !character.isSummon() && !character.isYYS());
        return this;
    }

    public CharacterFinder setTargetTeam(TargetTeam targetTeam) {
        this.targetTeam = targetTeam;
        if (targetTeam == TargetTeam.TEAMMATE) {
            stream = stream.filter(character -> character.team == owner.team);
        } else {
            stream = stream.filter(character -> character.team != owner.team);
        }
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

    public Character getPriorAuto(Property property, Criteria criteria) {
        // 如果自动目标存在,且列表中有该目标,直接返回
        // 否则返回和get方法一样的结果
        List<Character> list = getList();
        Character auto = bp.situation.getAutoTo(targetTeam == TargetTeam.TEAMMATE ? owner.team : getEnemyTeam())
                .orElse(null);
        if (auto != null && list.contains(auto)) {
            return auto;
        } else {
            stream = list.stream();
            return get(property, criteria);
        }
    }

    public Character getAutoOrElseRandom() {
        List<Character> list = getList();
        Character auto = bp.situation.getAutoTo(targetTeam == TargetTeam.TEAMMATE ? owner.team : getEnemyTeam())
                .orElse(null);
        if (auto != null && list.contains(auto)) {
            return auto;
        } else {
            stream = list.stream();
            return getRandom();
        }
    }

    public Character get(Property property, Criteria criteria) {
        Comparator<Character> comparator = Comparator.comparing(property.getGetter());
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

    public int getEnemyTeam() {
        return 1 - owner.team;
    }

    public enum Property {
        HP(Character::getHp),
        ATTACK(Character::getAttack);

        private final Function<Character, Double> getter;

        Property(Function<Character, Double> getter) {
            this.getter = getter;
        }

        public Function<Character, Double> getGetter() {
            return getter;
        }
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
