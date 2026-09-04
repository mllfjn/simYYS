package com.mllfjn.simyys.utils;

import com.mllfjn.simyys.character.yuhun.EquipFactory;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
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
import java.util.function.Consumer;
import java.util.function.Function;

public class YYXSnapshotLoader {
    private static boolean loaded = false;
    private static final ObjectMapper mapper = JsonMapper.builder().build();

    private static final Map<Integer, List<Hero>> heroes = new HashMap<>();
    private static final Map<String, Integer> hero_equips = new HashMap<>();

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

                navigateToProperty(parser, "hero_equips");
                if (parser.nextToken() != JsonToken.START_ARRAY) {
                    throw new IOException("hero_equips数组不存在");
                }

                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    HeroEquip equip = parser.readValueAs(HeroEquip.class);
                    hero_equips.put(equip.id, equip.suit_id);
                }

                navigateToProperty(parser, "heroes");
                if (parser.nextToken() != JsonToken.START_ARRAY) {
                    throw new IOException("heroes数组不存在");
                }

                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    Hero hero = parser.readValueAs(Hero.class);
                    if (hero.equips.length > 0) {
                        heroes.computeIfAbsent(hero.hero_id, _ -> new ArrayList<>()).add(hero);
                    }
                }
                loaded = true;

            } catch (Exception e) {
                Utils.throwException("读取导出的数据时发生错误", e);
            }
            return file.getName();
        }

        return null;
    }

    public static @Nullable Hero getHero(int hero_id) {
        if (!loaded) {
            Utils.information("未找到有效数据\n请先加载导出文件");
            return null;
        }

        List<Hero> heroes = YYXSnapshotLoader.heroes.get(hero_id);
        if (heroes == null) {
            Utils.information("未找到有效数据\n式神需要至少装备一个御魂");
            return null;
        }

        if (heroes.size() == 1) {
            return heroes.getFirst();
        }

        ChoiceDialog<Hero> dialog = new ChoiceDialog<>(heroes.getFirst(), heroes);
        Optional<Hero> result = dialog.showAndWait();
        return result.orElse(null);
//        return showSelectionDialog(heroes);
    }

    public static void getEquips(String[] equips, Consumer<EquipFactory.EquipMeta> action) {
        Map<EquipFactory.EquipMeta, Integer> counts = new HashMap<>();

        for (String equip : equips) {
            EquipFactory.EquipMeta equipMeta = EquipFactory.ID_MAP.get(hero_equips.get(equip));
            if (equipMeta != null) {
                counts.put(equipMeta, counts.getOrDefault(equipMeta, 0) + 1);
            }
        }

        for (Map.Entry<EquipFactory.EquipMeta, Integer> entry : counts.entrySet()) {
            if (entry.getValue() >= entry.getKey().setCount()) {
                action.accept(entry.getKey());
            }
        }
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

    private static Hero showSelectionDialog(List<Hero> heroes) {
        TableView<Hero> tableView = new TableView<>();
        tableView.getItems().addAll(heroes);

        tableView.getColumns().addAll(
                simpleColumn("昵称", hero -> hero.nick_name),
                simpleColumn("等级", hero -> String.valueOf(hero.level)),
                simpleColumn("觉醒", hero -> hero.awake ? "√" : "×"),
                simpleColumn("技能等级", Hero::getSkillLevel)
        );

        Dialog<Hero> dialog = new Dialog<>();
        ButtonType confirmButton = new ButtonType("确定");
        ButtonType cancelButton = new ButtonType("取消");

        dialog.setResultConverter(buttonType -> {
            if (buttonType == confirmButton) {
                return null;
            } else {
                return null;
            }
        });

        return dialog.showAndWait().orElse(null);
    }

    private static TableColumn<Hero, ?> simpleColumn(String title, Function<Hero, String> getter) {
        TableColumn<Hero, String> tableColumn = new TableColumn<>(title);
        tableColumn.setCellValueFactory(heroData ->
                new ReadOnlyStringWrapper(getter.apply(heroData.getValue()))
        );
        return tableColumn;
    }

    public static class Hero {
        public Attrs attrs;
        public boolean awake;
        public String[] equips;
        public int hero_id;
        public String nick_name;
        public Skill[] skills;
        public int level;

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

    public static class HeroEquip {
        public String id;
        public int suit_id;
    }
}
