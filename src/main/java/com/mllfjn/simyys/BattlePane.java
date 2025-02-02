package com.mllfjn.simyys;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.starter.info.CharacterInfo;
import com.mllfjn.simyys.starter.info.FlagChangeInfo;
import com.mllfjn.simyys.starter.info.SkillChangeInfo;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BattlePane {
    CharacterInfo[] characterInfo;
    SkillChangeInfo[] skillChangeInfo;
    FlagChangeInfo[] flagChangeInfo;
    List<Character> characters;
    public BattlePane(Stage stage, CharacterInfo[] characterInfo, SkillChangeInfo[] skillChangeInfo, FlagChangeInfo[] flagChangeInfo) {
        this.characterInfo = characterInfo;
        this.skillChangeInfo = skillChangeInfo;
        this.flagChangeInfo = flagChangeInfo;
        this.characters = new ArrayList<>();

        BorderPane root = new BorderPane();
        stage.setScene(new Scene(root));
        init();

        List<Character> list = getCharactersByActionSort();
        System.out.println(list);
    }

    private void init() {
        for (CharacterInfo info : characterInfo) {
            if (Integer.parseInt(info.team) == 0 || Integer.parseInt(info.team) == 1) {
                characters.add(CharacterFactory.createCharacter(info));
            }
        }

        double maxSpeed = 0;
        for (Character character : characters) {
            if (character.speed > maxSpeed) {
                maxSpeed = character.speed;
            }
        }

        for (Character character : characters) {
            character.setLocation(character.speed / maxSpeed);
        }


    }

    private List<Character> getCharactersByLocation() {
        return getCharactersAlive().stream().sorted((o1, o2) -> {
            if (o1.getLocation() > o2.getLocation()) {
                return 1;
            } else if (o1.getLocation() < o2.getLocation()) {
                return -1;
            } else return Double.compare(o1.getSpeed(), o2.getSpeed());
        }).toList();
    }

    private List<Character> getCharactersByActionSort() {
        List<Character> rt = new ArrayList<>();
        List<Character> list = getCharactersAlive();
        int size = list.size();
        double[] locations = new double[size];
        double[] speeds = new double[size];
        for (int i = 0; i < size; i++) {
            locations[i] = list.get(i).getLocation();
            speeds[i] = list.get(i).getSpeed();
        }

        for (int i = 0; i < 8; i++) {
            int min = 0;
            for (int j = 1; j < size; j++) {
                if ( (100 - locations[j]) / speeds[j] < (100 - locations[min]) / speeds[min]) {
                    min = j;
                }
            }
            rt.add(list.get(min));
            for (int j = 0; i < size; i++) {
                locations[j] += (100 - locations[min]) / speeds[min] * speeds[j];
            }
            locations[min] = 0;
        }

        return rt;
    }

    private List<Character> getCharactersAlive() {
        return characters.stream().filter(character -> character.alive).toList();
    }
}
