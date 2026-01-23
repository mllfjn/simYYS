package com.mllfjn.simyys.character;

import com.mllfjn.simyys.character.list.mob.multiplayer.InfoDisplay;
import com.mllfjn.simyys.character.propertygetter.FlagChangeInfo;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.SkillAuto;
import com.mllfjn.simyys.character.yuhun.YuHun;
import com.mllfjn.simyys.character.yuhun.YuHunFactory;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusShield;
import com.mllfjn.simyys.utils.DecimalFormatUtil;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.StringJoiner;

public class CharacterIcon implements Serializable {
    public static final double MAX_WIDTH = CharacterFactory.ImageSize.CHARACTER_ICON_IMAGE.size * 1.1;
    private static final Callback<ListView<Skill>, ListCell<Skill>> skillCellFactory = new Callback<>() {
        @Override
        public ListCell<Skill> call(ListView<Skill> skillListView) {
            return new ListCell<>() {
                @Override
                protected void updateItem(Skill skill, boolean b) {
                    super.updateItem(skill, b);
                    if (b || skill == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        if (skill instanceof PassiveSkill) {
                            // 被动技能不可点击
                            setText("被动 " + skill.getName());
                            setDisable(true);
                        } else if (skill instanceof SkillAuto) {
                            // 妖术
                            setText("妖术");
                        } else {
                            Text text = new Text(skill.toString());
                            StringBuilder sb = new StringBuilder();
                            int cooling = skill.getCooling();
                            if (cooling != 0) {
                                sb.append("冷:").append(cooling);
                            }
                            sb.append("火:").append(skill.getRealCost());
                            Label additionalText = new Label(sb.toString());
                            additionalText.setAlignment(Pos.CENTER_RIGHT);
                            additionalText.setMaxWidth(Double.MAX_VALUE);
                            HBox hBox = new HBox(text, additionalText);
                            HBox.setHgrow(additionalText, Priority.ALWAYS);
                            setGraphic(hBox);
                        }
                    }
                }
            };
        }
    };

    private final Character character;

    // 头像以上的部分，包括红绿标和状态栏
    private transient VBox top;
    // 中间区域，包括生命护盾头像技能
    private transient VBox center;
    // 头像以下，显示当前属性
    private transient VBox bottom;

    // 状态栏
    private transient Label status;
    // 生命条
    private transient ProgressBar healthBar;
    // 护盾条
    private transient ProgressBar shieldBar;
    // 技能选择栏
    private transient ComboBox<Skill> skillBox;
    // 状态显示
    private final MemoryLabel[] memoryLabels;
    private final EnumSet<Attribute> shownAttributes = EnumSet.noneOf(Attribute.class);
    // 红绿标的那个标
    private transient HBox autoTo;
    // 左键点击时的红绿标菜单
    private transient ContextMenu autoToMenu;
    // 图片区域,包括头像和御魂
    private transient AnchorPane imagePane;
    // 御魂显示
    private transient ImageView[] yuHunIcon;
    // 修改技能时comboBox切换不生效
    private boolean isModifyingItems = false;
    // 可选,右键时触发其他事件,比如极逢魔的转阶段,须佐的天威
    private transient EventHandler<MouseEvent> eventHandler;
    // 其他信息显示，比如极逢魔的伤害显示
    private transient Label infoDisplayLabel;
    private InfoDisplay infoDisplay;

    public CharacterIcon(Character character) {
        this.character = character;

        Attribute[] attributes = Attribute.values();
        memoryLabels = new MemoryLabel[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            MemoryLabel memoryLabel = new MemoryLabel(character, attributes[i]);
            memoryLabels[i] = memoryLabel;
        }

        shownAttributes.add(Attribute.SPEED);
        shownAttributes.add(Attribute.LOCATION);
        memoryLabels[Attribute.SPEED.ordinal()].displaying = true;
        memoryLabels[Attribute.LOCATION.ordinal()].displaying = true;
    }

    public void reset() {
        setupUI();
        setEventHandler(character.getEventHandler());
        setInfoDisplay(character.getInfoDisplay());

        for (MemoryLabel memoryLabel : memoryLabels) {
            memoryLabel.reset();
        }
    }

    private void setupUI() {
        // 头像以上部分

        // 红绿标
        autoTo = new HBox();
        autoTo.setAlignment(Pos.CENTER);
        Node green = CharacterFactory.getImage(FlagChangeInfo.FlagType.GREEN.type, CharacterFactory.ImageSize.LABEL);
        Node red = CharacterFactory.getImage(FlagChangeInfo.FlagType.RED.type, CharacterFactory.ImageSize.LABEL);

        green.setVisible(false);
        red.setVisible(false);
        autoTo.getChildren().addAll(green, red);

        MenuItem autoToGreen = new MenuItem(FlagChangeInfo.FlagType.GREEN.type);
        MenuItem autoToRed = new MenuItem(FlagChangeInfo.FlagType.RED.type);

        autoToGreen.setOnAction(
                e -> character.bp.situation.setAuto(character, FlagChangeInfo.FlagType.GREEN));
        autoToRed.setOnAction(
                e -> character.bp.situation.setAuto(character, FlagChangeInfo.FlagType.RED));
        autoToMenu = new ContextMenu(autoToGreen, autoToRed);

        // 状态栏
        status = new Label();
        status.setMaxWidth(MAX_WIDTH);
        status.setFont(Font.font(10));
        status.setWrapText(true);

        // 头像以上部分组装
        top = new VBox(autoTo, status);
        top.setAlignment(Pos.BOTTOM_CENTER);

        // 中间区域-----------------------------------

        // 生命
        healthBar = new ProgressBar();
        healthBar.setMaxWidth(MAX_WIDTH);
        if (character.team == 0) {
            healthBar.setStyle("-fx-accent: orange");
        } else {
            healthBar.setStyle("-fx-accent: red");
        }

        // 盾
        shieldBar = new ProgressBar();
        shieldBar.setStyle("-fx-accent: lightblue");
        shieldBar.setMaxWidth(MAX_WIDTH);

        // 头像
        imagePane = new AnchorPane();
        Node image = CharacterFactory.getImageWithStroke(character.name, CharacterFactory.ImageSize.CHARACTER_ICON_IMAGE, Color.ORANGE, 5);
        image.setOnMouseClicked(this::onMouseClicked);
        StackPane icon = new StackPane(image);
        icon.setAlignment(Pos.CENTER);
        imagePane.getChildren().add(icon);


        yuHunIcon = new ImageView[4];

        // 技能选择
        ObservableList<Skill> skills = character.getReadOnlySkillList();
        skillBox = new ComboBox<>(skills);
        selectLockSkill();
        skillBox.valueProperty().addListener((obs, old, val) -> {
            if (!isModifyingItems) {
                character.setLockSkill(val.getSkillID());
            }
        });
        skillBox.setCellFactory(skillCellFactory);
        skillBox.setMaxWidth(MAX_WIDTH);

        // 中间区域组装
        center = new VBox(healthBar, shieldBar, imagePane, skillBox);

        bottom = new VBox();
    }

    public VBox getTop() {
        return top;
    }

    public VBox getCenter() {
        return center;
    }

    public VBox getBottom() {
        return bottom;
    }

    public void setEventHandler(EventHandler<MouseEvent> eventHandler) {
        this.eventHandler = eventHandler;
    }

    protected void onMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY || eventHandler == null) {
            autoToMenu.show(center, event.getScreenX(), event.getScreenY());
//            character.bp.situation.teamPane[character.team].setAuto(character);
        } else {
            eventHandler.handle(event);
        }
    }

    public void setIsAuto(FlagChangeInfo.FlagType flagType, boolean isAuto) {
        autoTo.getChildren().get(flagType.index).setVisible(isAuto);
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
        updateYuHunIcon();
        refreshShieldBar();
        refreshSkillBox();

        healthBar.setProgress(character.getHp() / character.getMaxHp());

        if (infoDisplay != null) {
            infoDisplayLabel.setText(infoDisplay.getInfo());
        }
    }

    public void setInfoDisplay(InfoDisplay infoDisplay) {
        if (infoDisplay == null) {
            return;
        }
        this.infoDisplay = infoDisplay;
        infoDisplayLabel = character.bp.requestInfoDisplayLabel();
        infoDisplayLabel.setText(infoDisplay.getInfo());
    }

    private void refreshSkillBox() {
        skillBox.setCellFactory(null);
        skillBox.setCellFactory(skillCellFactory);
    }

    private void refreshStatusLabel() {
        StringJoiner sj = new StringJoiner(Displayable.DELIMITER);
        for (Status status : character.getStatuses()) {
            if (status instanceof Displayable d) {
                String text = d.getDisplayText();
                if (text != null) {
                    sj.add(text);
                }
            }
        }
        this.status.setText(sj.toString());
    }

    private void refreshProperties() {
        for (MemoryLabel memoryLabel : memoryLabels) {
            memoryLabel.refresh();
        }
    }

    private void refreshShieldBar() {
        double shield = 0;
        for (Status status : character.getStatuses()) {
            if (status instanceof StatusShield ss) {
                shield += ss.getShield();
            }
        }
        if (shield > 0) {
            shieldBar.setTooltip(new Tooltip(DecimalFormatUtil.df_0_2.format(shield)));
            shieldBar.setProgress(shield / character.getMaxHp());
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

    static class MemoryLabel implements Serializable {
        private final Character character;
        private final Attribute attribute;

        private double num;
        private boolean displaying = false;
        private boolean showingChanged = false;

        private transient Label label;

        private MemoryLabel(Character character, Attribute attribute) {
            this.attribute = attribute;
            this.character = character;

            num = attribute.getGetter().apply(character);
        }

        public void reset() {
            if (displaying) {
                createLabel(false);
                setLabelText();
            }
        }

        public void refresh() {
            double newNumber = attribute.getGetter().apply(character);
            if (newNumber != num) {
                if (!displaying) {
                    start();
                }
                setLabelText(newNumber);
                num = newNumber;
                setChanged(true);
            } else if (showingChanged) {
                setChanged(false);
            }
        }

        private void start() {
            displaying = true;
            createLabel(true);
        }

        private void createLabel(boolean shouldOrder) {
            label = new Label();
            label.setFont(Font.font(10));

            if (!shouldOrder) {
                character.getCharacterIcon().bottom.getChildren().add(label);
            } else {
                int ordinal = attribute.ordinal();
                int i = 0;
                EnumSet<Attribute> currentAttribute = character.getCharacterIcon().shownAttributes;
                for (Attribute shownAttribute : currentAttribute) {
                    if (shownAttribute.ordinal() < ordinal) {
                        i++;
                    } else {
                        break;
                    }
                }
                character.getCharacterIcon().bottom.getChildren().add(i, label);
                currentAttribute.add(attribute);
            }
        }

        private void setLabelText() {
            label.setText(attribute.getText() + ":" + DecimalFormatUtil.df_0_2.format(num));
        }

        private void setLabelText(double newNumber) {
            // 生命:1234(+234)

            StringBuilder sb = new StringBuilder();
            sb.append(attribute.getText()).append(":").append(DecimalFormatUtil.df_0_2.format(newNumber));
            sb.append("(");
            double difference = newNumber - num;
            if (difference > 0) {
                sb.append("+");
            }
            sb.append(DecimalFormatUtil.df_0_2.format(difference)).append(")");
            label.setText(sb.toString());
        }

        private void setChanged(boolean changed) {
            if (changed) {
                if (!showingChanged) {
                    showingChanged = true;
                    // 如果变更就换成红色
                    label.setStyle("-fx-text-fill: red");
                }
            } else {
                label.setStyle("-fx-text-fill: black");
                setLabelText();
                showingChanged = false;
            }
        }
    }
}
