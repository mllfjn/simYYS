package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CharacterFinder {
    public static Character find(List<Character> characters, int team, Property property, Criteria criteria) {
        Stream<Character> stream = characters.stream().filter(character -> character.team == team);
        Comparator<Character> comparator = Comparator.comparing(property.getGetter());
        return criteria == Criteria.MAX ? stream.max(comparator).orElse(null) : stream.min(comparator).orElse(null);
    }

    public static Character findPriorAuto(BattlePane bp, int team, Property property, Criteria criteria) {
        return bp.situation.getAutoTo(team).orElseGet(() -> find(bp.situation.characters, team, property, criteria));
    }

    public static Character findPriorAuto(List<Character> list, BattlePane bp, int team, Property property, Criteria criteria) {
        return bp.situation.getAutoTo(team).filter(list::contains).orElseGet(() -> find(list, team, property, criteria));
    }

    public static List<Character> findTeammate(Character character, List<Character> characters) {
        return characters.stream()
                .filter(character1 -> character1.team == character.team)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Character> findTeammateExceptSummon(Character character, List<Character> characters) {
        return characters.stream()
                .filter(character1 -> character1.team == character.team && !character1.isSummon())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<Character> findTeammateShiShen(Character character, List<Character> characters) {
        return characters.stream().filter(character1 ->
                        character1.team == character.team
                                && !character1.isYYS()
                                && !character1.isSummon())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static int getEnemyTeam(Character character) {
        return 1 - character.team;
    }

    public static List<Character> findEnemy(Character character, List<Character> characters) {
        return characters.stream().filter(character1 -> character1.team != character.team).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Character findRandom(List<Character> characters) {

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
}
