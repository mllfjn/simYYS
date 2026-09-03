package com.mllfjn.simyys.utils;

import javafx.scene.control.ChoiceDialog;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.jetbrains.annotations.Nullable;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YYXSnapshotLoader {
    private static final Map<Integer, List<Hero>> index = new HashMap<>();
    private static final ObjectMapper mapper = JsonMapper.builder().build();

    public static String loadJson(Window ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("json", "*.json"));
        File file = fileChooser.showOpenDialog(ownerWindow);
        if (file != null) {
            try (JsonParser parser = mapper.createParser(file)) {
                if (parser.nextToken() != JsonToken.START_OBJECT) {
                    throw new IOException("不是json对象");
                }

                // {"data": {"heroes"}}
                navigateToProperty(parser, "data");
                if (parser.nextToken() != JsonToken.START_OBJECT) {
                    throw new IOException("data对象不存在");
                }

                navigateToProperty(parser, "heroes");
                if (parser.nextToken() != JsonToken.START_ARRAY) {
                    throw new IOException("heroes数组不存在");
                }

                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    Hero hero = parser.readValueAs(Hero.class);
                    if (hero.equips.length > 0) {
                        index.computeIfAbsent(hero.hero_id, _ -> new ArrayList<>()).add(hero);
                    }
                }

            } catch (Exception e) {
                Utils.throwException("读取导出的数据时发生错误", e);
            }
            return file.getName();
        }

        return null;
    }

    public static @Nullable Hero getHero(int hero_id) {
        List<Hero> heroes = index.get(hero_id);
        if (heroes == null) {
            return null;
        }

        if (heroes.size() == 1) {
            return heroes.getFirst();
        }

        ChoiceDialog<Hero> dialog = new ChoiceDialog<>(heroes.getFirst(), heroes);
        Optional<Hero> result = dialog.showAndWait();
        return result.orElse(null);

        /*Stage stage = new Stage();

        ListView<Hero> listView = new ListView<>();
        StackPane stackPane = new StackPane(listView);
        stackPane.setPadding(new Insets(20));

        stage.setScene(new Scene(stackPane));
        stage.showAndWait();*/
    }

    private static void navigateToProperty(JsonParser parser, String propertyName) throws IOException {
        while (true) {
            String name = parser.nextName();
            if (name == null) {
                throw new IOException("未找到property:" + propertyName);
            }
            if (propertyName.equals(name)) {
                return;
            } else {
                parser.nextToken();
                parser.skipChildren();
            }
        }
    }

    public static class Hero {
        public Attrs attrs;
        public boolean awake;
        public String[] equips;
        public int hero_id;
        public String nick_name;
        public Skill[] skills;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (nick_name != null) {
                sb.append(nick_name);
            }
            if (!awake) sb.append("未觉醒");
            sb.append("技能等级").append(getSkillLevel());
            // TODO 更多显示

            return sb.toString();
        }

        public String getSkillLevel() {
            return String.valueOf(100 * skills[0].level + 10 * skills[1].level + skills[2].level);
        }
    }

    public static class Attrs {
        public Attr attack;
        public Attr crit_power;
        public Attr crit_rate;
        public Attr defense;
        public double effect_hit_rate;
        public double effect_resist_rate;
        public Attr max_hp;
        public Attr speed;
    }

    public static class Attr {
        public double base;
        public double value;
    }

    public static class Skill {
        public int level;
    }
}
