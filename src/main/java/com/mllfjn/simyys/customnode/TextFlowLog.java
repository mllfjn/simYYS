package com.mllfjn.simyys.customnode;

import com.mllfjn.simyys.character.Character;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

public class TextFlowLog extends ScrollPane {
    private final TextFlow textFlow = new TextFlow();
    private final boolean[] shownTypes = new boolean[TextType.values().length];
    private List<CustomText> currentList = new ArrayList<>();
    private final List<List<CustomText>> totalList = new ArrayList<>();
    private static final String DIVIDER = "------------------------------\n";

    public TextFlowLog() {
        super();
        this.setContent(textFlow);
        this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.widthProperty().addListener((obs, old, val) -> textFlow.setMaxWidth(val.doubleValue()));
        textFlow.heightProperty().addListener((obs, old, val) -> setVvalue(1));

        totalList.add(currentList);

        ContextMenu menu = new ContextMenu();
        TextType[] values = TextType.values();
        for (int i = 0; i < values.length; i++) {
            CheckMenuItem item = new CheckMenuItem(values[i].description);
            shownTypes[i] = true;
            item.setSelected(true);

            int index = i;
            item.selectedProperty().addListener((obs, old, val) -> {
                shownTypes[index] = val;
                reFresh();
            });

            menu.getItems().add(item);
        }
        this.setContextMenu(menu);

    }

    public void addSkill(String s) {
        this.addText("\t" + s + "\n", TextType.SKILL, TextColor.NORMAL, FontSize.NORMAL);
    }

    public void addText(String s, TextType type, TextColor textColor, FontSize fontSize) {
        CustomText text = new CustomText(s, type, textColor, fontSize);

        addText(text);
    }

    public void addText(CustomText text) {
        currentList.add(text);
        if (shownTypes[text.getType().ordinal()]) {
            textFlow.getChildren().add(text);
        }
    }

    public void addLocationChange(String text) {
        addText("\t" + text + "\n", TextType.INCREASE, TextFlowLog.TextColor.NORMAL, TextFlowLog.FontSize.NORMAL);
    }

    private void reFresh() {
        textFlow.getChildren().clear();
        for (List<CustomText> list : totalList) {
            for (CustomText text : list) {
                if (shownTypes[text.getType().ordinal()]) {
                    textFlow.getChildren().add(text);
                }
            }
        }
    }

    public void characterAct(Character characterActing) {
        this.addText(DIVIDER, TextType.ROUND, TextColor.NORMAL, FontSize.NORMAL);
        this.addText(characterActing.name + "行动" + characterActing.timesToAct + "\n", TextType.ROUND, TextColor.NORMAL, FontSize.BIG);
    }

    public void next() {
        currentList = new ArrayList<>();
        totalList.add(currentList);
    }

    public void prev() {
        List<CustomText> prev = totalList.get(totalList.size() - 2);
        textFlow.getChildren().removeAll(prev);
        totalList.remove(prev);
    }

    public enum TextType {
        ROUND("回合"),
        SKILL("技能"),
        INCREASE("行动条"),
        NUMBER("伤害数字"),
        YU_HUN("御魂触发");
        final String description;
        TextType(String description) {
            this.description = description;
        }
    }

    public enum TextColor {
        NORMAL(Color.BLACK),
        ATTACK(Color.RED),
        CRITICAL(Color.ORANGE),
        HEAL(Color.GREEN);
        final Color color;
        TextColor(Color color) {
            this.color = color;
        }
    }

    public enum FontSize {
        BIG(20),
        NORMAL(15),
        SMALL(10);

        final double size;
        FontSize(double size) {
            this.size = size;
        }
    }
}
