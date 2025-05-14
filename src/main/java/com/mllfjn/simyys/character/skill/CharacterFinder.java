package com.mllfjn.simyys.character.skill;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class CharacterFinder {
    public static Character find(List<Character> characters, int team, Property property, Criteria criteria) {
        Stream<Character> stream = characters.stream().filter(character -> character.team == team);
        Comparator<Character> comparator = Comparator.comparing(property.getGetter());
        return criteria == Criteria.MAX ? stream.max(comparator).orElse(null) : stream.min(comparator).orElse(null);
    }

    public static Character findPriorAuto(BattlePane bp, int team, Property property, Criteria criteria) {
        return bp.autoTo[team] == null ? find(bp.characters, team, property, criteria) : bp.autoTo[team];
    }
    public static int findEnemy(Character character) {
        return character.team == 0 ? 1 : 0;
    }
    public static int findTeammate(Character character) {
        return character.team == 0 ? 0 : 1;
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
