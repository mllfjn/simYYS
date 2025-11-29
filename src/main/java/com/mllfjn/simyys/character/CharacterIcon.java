package com.mllfjn.simyys.character;

import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;
import com.mllfjn.simyys.status.Displayable;
import com.mllfjn.simyys.status.Status;
import com.mllfjn.simyys.status.StatusShield;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Iterator;
import java.util.Optional;
import java.util.StringJoiner;

public class CharacterIcon extends VBox {
    public static final double MAX_WIDTH = CharacterFactory.ImageSize.LARGE.size * 1.1;


    protected final Character character;
    // 状态栏
    private final Label statusLabel = new Label();
    // 生命条
    private final ProgressBar healthBar = new ProgressBar();
    // 护盾条
    private final ProgressBar shieldBar = new ProgressBar();
    // 技能选择栏
    private final ComboBox<Skill> skillBox;
    // 状态显示 TODO UI决定是否显示
    private final Label[] info = new Label[8];
    // 红绿标的那个标
    private final Node autoTo;
    // 图片区域,包括头像和御魂
    private final AnchorPane imagePane = new AnchorPane();
    // 御魂显示
    private final ImageView[] yuHunIcon = new ImageView[4];
    // 修改技能时comboBox切换不生效
    private boolean isModifyingItems = false;
    // 可选,右键时触发其他事件,比如极逢魔的转阶段,须佐的天威
    private EventHandler<MouseEvent> eventHandler;

    public CharacterIcon(Character character) {
        super();
        this.character = character;
        this.setPadding(new Insets(0, 0, 10, 0));
        this.setAlignment(Pos.BOTTOM_CENTER);

        // 头像
        Node image = CharacterFactory.getImageWithStroke(character.name, CharacterFactory.ImageSize.LARGE, Color.ORANGE, 5);
        image.setOnMouseClicked(this::onMouseClicked);
        StackPane icon = new StackPane(image);
        icon.setAlignment(Pos.CENTER);
        imagePane.getChildren().add(icon);
        // 技能选择
        ObservableList<Skill> skills = character.getReadOnlySkillList();
        skillBox = new ComboBox<>(skills);
        selectLockSkill();
        skillBox.valueProperty().addListener((obs, old, val) -> {
            if (!isModifyingItems) {
                character.setLockSkill(val.getSkillID());
            }
        });
        skillBox.setMaxWidth(MAX_WIDTH);
        // 生命
        healthBar.setMaxWidth(MAX_WIDTH);
        // 盾
        shieldBar.setStyle("-fx-accent: lightblue");
        shieldBar.setMaxWidth(MAX_WIDTH);
        // 状态栏
        statusLabel.setMaxWidth(MAX_WIDTH);
        statusLabel.setWrapText(true);

        // 设置队伍相关
        if (character.team == 0) {
            healthBar.setStyle("-fx-accent: orange");
            this.autoTo = CharacterFactory.getImage("绿标", CharacterFactory.ImageSize.LABEL);
        } else {
            healthBar.setStyle("-fx-accent: red");
            this.autoTo = CharacterFactory.getImage("红标", CharacterFactory.ImageSize.LABEL);
        }

        setIsAuto(false);

        this.getChildren().addAll(
                autoTo,
                statusLabel,
                healthBar,
                shieldBar,
                imagePane,
                skillBox
        );
        for (int i = 0; i < info.length; i++) {
            info[i] = new Label();
            info[i].setMaxWidth(MAX_WIDTH);
            this.getChildren().add(info[i]);
        }
    }

    public void setEventHandler(EventHandler<MouseEvent> eventHandler) {
        this.eventHandler = eventHandler;
    }

    protected void onMouseClicked(MouseEvent event) {
        if (eventHandler == null || event.getButton() == MouseButton.PRIMARY) {
            character.bp.situation.teamPane[character.team].setAuto(character);
        } else {
            eventHandler.handle(event);
        }
    }

    public void selectLockSkill() {
        // 如果当前选中的技能存在，并且SkillID等于lockSkill，返回
        if (skillBox.getValue() != null && skillBox.getValue().getSkillID() == character.getLockSkill()) {
            return;
        }

        isModifyingItems = true;
        Optional<Skill> os = character.getSkill(character.getLockSkill());
        os.ifPresentOrElse(
                s -> skillBox.getSelectionModel().select(s)
                , () -> skillBox.getSelectionModel().select(0)
        );
        isModifyingItems = false;
    }

    public void startChangeSkill() {
        isModifyingItems = true;
    }

    public void endChangeSkill() {
        selectLockSkill();
    }

    public void update() {
        refreshProperties();
        refreshStatusLabel();
        refreshHealthBar();
        updateYuHunIcon();
        refreshShieldBar();
    }

    private void refreshStatusLabel() {
        StringJoiner sj = new StringJoiner(Displayable.DELIMITER);
        // 这里海原贝戟和堕落之剑会显示没必要的无法动作,所以移除了   而且该方法需要遍历,和下面应该可以合在一起
        /*if (!character.controllable()) {
            sj.add("无法动作");
        }*/
        for (Status status : character.getStatuses()) {
            if (status instanceof Displayable d) {
                String text = d.getText();
                if (text != null) {
                    sj.add(text);
                }
            }
        }
        this.statusLabel.setText(sj.toString());
    }

    private void refreshProperties() {
        info[0].setText("攻击:" + String.format("%.2f", character.getAttack()));
        info[2].setText("防御:" + String.format("%.2f", character.getDefense()));
        info[3].setText("速度:" + String.format("%.2f", character.getSpeed()));
        info[4].setText("暴击:" + String.format("%.2f", character.getCritRate()));
        info[5].setText("爆伤:" + String.format("%.2f", character.getCritPower()));
        info[6].setText("命中:" + String.format("%.2f", character.getEffectHitRate()));
        info[7].setText("抵抗:" + String.format("%.2f", character.getEffectResistRate()));
    }

    private void refreshHealthBar() {
        info[1].setText("生命:" + String.format("%.2f(%.2f%%)", character.getHp(), character.getHp() / character.getMaxHp() * 100));
        this.healthBar.setProgress(character.getHp() / character.getMaxHp());
    }

    private void refreshShieldBar() {
        double shield = 0;
        double maxHp = character.getMaxHp();
        Iterator<Status> iterator = character.getStatuses().iterator();
        while (shield < maxHp && iterator.hasNext()) {
            if (iterator.next() instanceof StatusShield ss) {
                shield += ss.getShield();
            }
        }
        if (shield > 0) {
            shieldBar.setProgress(shield / maxHp);
            shieldBar.setVisible(true);
        } else {
            shieldBar.setVisible(false);
        }
    }

    private void updateYuHunIcon() {
        Iterator<YuHun> iterator = character.getYuHunSet().iterator();
        for (int i = 0; i < 4; i++) {
            if (iterator.hasNext()) {
                YuHun yuHun = iterator.next();
                ImageView imageView = getImagePosition(i);
                imageView.setImage(YuHunFactory.getImage(yuHun.getName()));
            } else {
                if (yuHunIcon[i] != null) {
                    yuHunIcon[i].setImage(null);
                }
            }
        }
    }

    public void setIsAuto(boolean visible) {
        autoTo.setVisible(visible);
    }

    private ImageView getImagePosition(int index) {
        if (index >= yuHunIcon.length) {
            throw new IndexOutOfBoundsException();
        }

        if (yuHunIcon[index] == null) {
            ImageView imageView = new ImageView();
            // 按顺序在imagePane的 左下 右下 左上 右上
            switch (index) {
                case 0 -> {
                    AnchorPane.setLeftAnchor(imageView, 0.0);
                    AnchorPane.setBottomAnchor(imageView, 0.0);
                }
                case 1 -> {
                    AnchorPane.setRightAnchor(imageView, 0.0);
                    AnchorPane.setBottomAnchor(imageView, 0.0);
                }
                case 2 -> {
                    AnchorPane.setLeftAnchor(imageView, 0.0);
                    AnchorPane.setTopAnchor(imageView, 0.0);
                }
                case 3 -> {
                    AnchorPane.setRightAnchor(imageView, 0.0);
                    AnchorPane.setTopAnchor(imageView, 0.0);
                }
            }
            imageView.setClip(new Circle(YuHunFactory.ICON_RADIUS, YuHunFactory.ICON_RADIUS, YuHunFactory.ICON_RADIUS));
            imageView.setFitHeight(YuHunFactory.ICON_SIZE);
            imageView.setFitWidth(YuHunFactory.ICON_SIZE);
            yuHunIcon[index] = imageView;
            imagePane.getChildren().add(imageView);
        }

        return yuHunIcon[index];
    }
}
