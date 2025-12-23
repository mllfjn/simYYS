package com.mllfjn.simyys.character;

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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.Iterator;
import java.util.Optional;
import java.util.StringJoiner;

public class CharacterIcon extends VBox {
    public static final double MAX_WIDTH = CharacterFactory.ImageSize.LARGE.size * 1.1;
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
                            sb.append("火:").append(skill.getRealCost(false));
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

    protected final Character character;
    // 状态栏
    private final Label statusLabel = new Label();
    // 生命条
    private final ProgressBar healthBar = new ProgressBar();
    // 护盾条
    private final ProgressBar shieldBar = new ProgressBar();
    // 技能选择栏
    private final ComboBox<Skill> skillBox;
    // 状态显示
    // TODO UI决定是否显示，难点在于头像对齐，上下贴在头像旁 可以让所有角色同时不显示
    // TODO 发生改变时标出来
    private final MemoryLabel[] info = new MemoryLabel[9];
    // 红绿标的那个标
    private final HBox autoTo;
    // 左键点击时的红绿标菜单
    private final ContextMenu autoToMenu;
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
        skillBox.setCellFactory(skillCellFactory);
        skillBox.setMaxWidth(MAX_WIDTH);
        // 生命
        healthBar.setMaxWidth(MAX_WIDTH);
        // 盾
        shieldBar.setStyle("-fx-accent: lightblue");
        shieldBar.setMaxWidth(MAX_WIDTH);
        // 状态栏
        statusLabel.setMaxWidth(MAX_WIDTH);
        statusLabel.setWrapText(true);

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

        // 设置队伍相关
        if (character.team == 0) {
            healthBar.setStyle("-fx-accent: orange");
        } else {
            healthBar.setStyle("-fx-accent: red");
        }

        this.getChildren().addAll(
                autoTo,
                statusLabel,
                healthBar,
                shieldBar,
                imagePane,
                skillBox
        );
        for (int i = 0; i < info.length; i++) {
            MemoryLabel memoryLabel = new MemoryLabel(character, Attribute.values()[i]);
            info[i] = memoryLabel;
            Label label = memoryLabel.label;
            label.setMaxWidth(MAX_WIDTH);
            this.getChildren().add(label);
        }
    }

    public void setEventHandler(EventHandler<MouseEvent> eventHandler) {
        this.eventHandler = eventHandler;
    }

    protected void onMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY || eventHandler == null) {
            autoToMenu.show(this, event.getScreenX(), event.getScreenY());
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
    }

    private void refreshSkillBox() {
        skillBox.setCellFactory(null);
        skillBox.setCellFactory(skillCellFactory);
    }

    private void refreshStatusLabel() {
        /*TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefRowCount(3);
        textArea.setEditable(false);*/

        StringJoiner sj = new StringJoiner(Displayable.DELIMITER);
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
        for (MemoryLabel memoryLabel : info) {
            memoryLabel.refresh();
        }
    }

    private void refreshShieldBar() {
        double shield = 0;
        double maxHp = character.getMaxHp();
        for (Status status : character.getStatuses()) {
            if (status instanceof StatusShield ss) {
                shield += ss.getShield();
                if (shield >= maxHp) {
                    break;
                }
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

    static class MemoryLabel {
        private final Label label = new Label();
        private final Character character;
        private final Attribute attribute;

        private double num;
        private boolean showingChanged = false;

        private MemoryLabel(Character character, Attribute attribute) {
            this.attribute = attribute;
            this.character = character;
            num = attribute.getGetter().apply(character);

            setLabelText();
        }

        public void refresh() {
            double newNumber = attribute.getGetter().apply(character);
            if (newNumber != num) {
                setLabelText(newNumber);
                num = newNumber;
                setChanged(true);
            } else {
                setChanged(false);
            }
        }

        private void setLabelText() {
            label.setText(attribute.getText() + ":" + DecimalFormatUtil.df_0_2.format(num));
        }

        private void setLabelText(double newNumber) {
            label.setText(attribute.getText() + ":" + DecimalFormatUtil.df_0_2.format(newNumber)
                    + "(" + DecimalFormatUtil.df_0_2.format(newNumber - num) + ")");
        }

        private void setChanged(boolean changed) {
            if (changed) {
                if (!showingChanged) {
                    showingChanged = true;
                    // 如果变更就换成红色
                    label.setStyle("-fx-text-fill: red");
                }
            } else {
                if (showingChanged) {
                    label.setStyle("-fx-text-fill: black");
                    setLabelText();
                    showingChanged = false;
                }
            }
        }
    }
}
