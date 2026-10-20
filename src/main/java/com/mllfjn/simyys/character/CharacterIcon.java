package com.mllfjn.simyys.character;

import com.mllfjn.simyys.character.list.mob.multiplayer.InfoDisplay;
import com.mllfjn.simyys.character.propertygetter.FlagChangeInfo;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.skill.SkillAuto;
import com.mllfjn.simyys.character.yuhun.Equip;
import com.mllfjn.simyys.character.yuhun.EquipFactory;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.instance.StatusShield;
import com.mllfjn.simyys.customnode.CustomInputMenuItem;
import com.mllfjn.simyys.utils.DecimalFormatUtil;
import com.mllfjn.simyys.utils.serializable.SerialConsumer;
import javafx.collections.ObservableList;
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
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

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
                            setOnMousePressed(_ -> {
                            });
                            setText("被动 " + skill.getName());
                            String skillDesc = skill.getSkillDesc();
                            if (skillDesc != null) {
                                setTooltip(new Tooltip(skillDesc));
                            }
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
                            String skillDesc = skill.getSkillDesc();
                            if (skillDesc != null) {
                                Tooltip.install(hBox, new Tooltip(skillDesc));
                            }
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
    // 头像以下，显示技能和当前属性
    private transient VBox bottom;

    // 视觉效果
    private SerialConsumer<Node> visualEffectTop;
    private SerialConsumer<Node> visualEffectCenter;
    private SerialConsumer<Node> visualEffectBottom;

    // 状态栏
    private transient TextFlow statuses;
    // 生命条
    private transient ProgressBar healthBar;
    // 护盾条
    private transient ProgressBar shieldBar;
    // 技能选择栏
    private transient ComboBox<Skill> skillBox;
    // 状态显示
    private transient VBox attributes;
    private final AttributeMemory[] attributeMemories;
    private final ArrayList<String> attributeShowList = new ArrayList<>();
    // 红绿标的那个标
    private transient HBox autoTo;
    // 左键点击时的红绿标菜单
    private transient ContextMenu autoToMenu;
    // 图片区域,包括头像和御魂
    private transient AnchorPane imagePane;
    // 御魂显示
    private transient ImageView[] yuHunIcon;
    // 可选,右键时触发其他事件,比如极逢魔的转阶段,须佐的天威
    private EventHandlerContainer eventHandlerContainer;
    // 其他信息显示，比如极逢魔的伤害显示
    private transient Label infoDisplayLabel;
    private InfoDisplay infoDisplay;

    public CharacterIcon(Character character) {
        this.character = character;

        Attribute[] attributes = Attribute.values();
        attributeMemories = new AttributeMemory[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            AttributeMemory memoryLabel = new AttributeMemory(character, attributes[i]);
            attributeMemories[i] = memoryLabel;
        }
    }

    public void reset() {
        setInfoDisplay(character.getInfoDisplay());
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

        autoToGreen.setOnAction(_ -> {
            character.bp.situation.setAuto(character, FlagChangeInfo.FlagType.GREEN);
            character.bp.characterSetFlag(FlagChangeInfo.FlagType.GREEN, character);
        });
        autoToRed.setOnAction(_ -> {
            character.bp.situation.setAuto(character, FlagChangeInfo.FlagType.RED);
            character.bp.characterSetFlag(FlagChangeInfo.FlagType.RED, character);
        });
        autoToMenu = new ContextMenu(autoToGreen, autoToRed);

        // 状态栏
        statuses = new TextFlow();
        statuses.setMaxWidth(MAX_WIDTH);
//        status.setFont(Font.font(10));
//        status.setWrapText(true);

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

        // 头像以上部分组装
        top = new VBox(autoTo, statuses, healthBar, shieldBar);
        top.setAlignment(Pos.BOTTOM_CENTER);
        if (visualEffectTop != null) {
            visualEffectTop.accept(top);
        }

        // 中间区域-----------------------------------


        // 头像
        imagePane = new AnchorPane();
        Node image = CharacterFactory.getImageWithStroke(character, CharacterFactory.ImageSize.CHARACTER_ICON_IMAGE, 5);
        image.setOnMouseClicked(this::onMouseClicked);
        StackPane icon = new StackPane(image);
        icon.setAlignment(Pos.CENTER);
        imagePane.getChildren().add(icon);


        yuHunIcon = new ImageView[4];

        // 中间区域组装
        center = new VBox(imagePane);
        if (visualEffectCenter != null) {
            visualEffectCenter.accept(center);
        }

        // 底部区域,显示属性
        // 技能选择
        ObservableList<Skill> skills = character.getReadOnlySkillList();
        skillBox = new ComboBox<>(skills);
        selectLockSkill();
        skillBox.valueProperty().addListener((_, _, val) -> {
            int skillID = val.getSkillID();
            character.setLockSkill(skillID);

            if (skillID == -1) {
                skillID = skills.indexOf(val);
            }

            character.bp.characterSetLockSkill(character, skillID);

        });
        skillBox.setCellFactory(skillCellFactory);
        skillBox.setMaxWidth(MAX_WIDTH);

        attributes = new VBox();
        bottom = new VBox(skillBox, attributes);
        bottom.setMaxWidth(MAX_WIDTH);
        Menu menuAlwaysShow = new Menu("总是显示属性");
        for (AttributeMemory attributeMemory : attributeMemories) {
            CheckMenuItem item = new CheckMenuItem(attributeMemory.attribute.getText());
            item.selectedProperty().addListener((_, _, val) -> {
                        attributeMemory.setAlwaysShow(val);
                        if (val) {
                            attributeMemory.refresh(attributeShowList);
                        }
                        showAttributes();
                    }
            );
            menuAlwaysShow.getItems().add(item);
        }

        Menu menuSetAttribute = new Menu("设置属性");
        menuSetAttribute.getItems().addAll(
                CustomInputMenuItem.of("最大生命值", number -> {
                    character.forceSetMaxHp(number, false);
                    update();
                }),
                CustomInputMenuItem.of("当前生命值", number -> {
                    character.setHpWithoutTrigger(number);
                    update();
                }),
                CustomInputMenuItem.of("生命百分比", number -> {
                    character.setHpWithoutTrigger(character.getMaxHp() * number / 100);
                    update();
                }),
                CustomInputMenuItem.of("基础攻击力", number -> {
                    character.setInitBaseAttack(number);
                    update();
                }),
                CustomInputMenuItem.of("额外攻击力", number -> {
                    character.setInitAdditionAttack(number);
                    update();
                }),
                CustomInputMenuItem.of("初始防御力", number -> {
                    character.setInitDefense(number);
                    update();
                }),
                CustomInputMenuItem.of("绝对行动条", number -> {
                    character.forceSetLocation(number);
                    update();
                }),
                CustomInputMenuItem.of("相对行动条", number -> {
                    character.forceSetLocation(character.getLocation() + number);
                    update();
                })
        );

        ContextMenu menu = new ContextMenu(menuAlwaysShow, menuSetAttribute);
        bottom.setOnContextMenuRequested(event ->
                menu.show(bottom, event.getScreenX(), event.getScreenY())
        );
        if (visualEffectBottom != null) {
            visualEffectBottom.accept(bottom);
        }
    }

    public VBox getTop() {
        if (top == null) {
            setupUI();
        }
        return top;
    }

    public VBox getCenter() {
        if (center == null) {
            setupUI();
        }
        return center;
    }

    public VBox getBottom() {
        if (bottom == null) {
            setupUI();
        }
        return bottom;
    }

    public void setEventHandlerContainer(EventHandlerContainer eventHandlerContainer) {
        this.eventHandlerContainer = eventHandlerContainer;
    }

    protected void onMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY || eventHandlerContainer == null) {
            autoToMenu.show(center, event.getScreenX(), event.getScreenY());
        } else {
            eventHandlerContainer.handle(event);
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

        Optional<Skill> os = character.getSkill(character.getLockSkill());
        os.ifPresentOrElse(
                s -> skillBox.getSelectionModel().select(s)
                , () -> skillBox.getSelectionModel().select(0)
        );
    }

    public void selectLockSkill(Skill skill) {
        if (skill == null) {
            return;
        }
        skillBox.getSelectionModel().select(skill);
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
        statuses.getChildren().clear();
        for (Status status : character.getStatuses()) {
            if (status.isDisplayText()) {
                String text = status.getDisplayText();
                if (text != null) {
                    Text t = new Text(text + Status.DELIMITER);
                    Color color = status.getDisplayColor();
                    if (color != null) {
                        t.setFill(color);
                    }
                    statuses.getChildren().add(t);
                }
            }
        }
    }

    private void refreshProperties() {
        attributeShowList.clear();
        for (AttributeMemory attributeMemory : attributeMemories) {
            attributeMemory.refresh(attributeShowList);
        }
        showAttributes();
    }

    private void showAttributes() {
        attributes.getChildren().clear();
        if (!attributeShowList.isEmpty()) {
            for (String s : attributeShowList) {
                attributes.getChildren().add(new Text(s));
            }
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
        Iterator<Equip> iterator = character.getYuHunSet().iterator();
        for (int i = 0; i < 4; i++) {
            if (iterator.hasNext()) {
                Equip equip = iterator.next();
                ImageView imageView = getImagePosition(i);
                imageView.setImage(EquipFactory.getImage(equip.getName()));
            } else {
                if (yuHunIcon[i] != null) {
                    yuHunIcon[i].setImage(null);
                }
            }
        }
    }

    public void setVisualEffectTop(SerialConsumer<Node> consumer) {
        consumer.accept(top);
        visualEffectTop = consumer;
    }

    public void setVisualEffectCenter(SerialConsumer<Node> consumer) {
        consumer.accept(center);
        visualEffectCenter = consumer;
    }

    public void setVisualEffectBottom(SerialConsumer<Node> consumer) {
        consumer.accept(bottom);
        visualEffectBottom = consumer;
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
            imageView.setClip(new Circle(EquipFactory.ICON_RADIUS, EquipFactory.ICON_RADIUS, EquipFactory.ICON_RADIUS));
            imageView.setFitHeight(EquipFactory.ICON_SIZE);
            imageView.setFitWidth(EquipFactory.ICON_SIZE);
            yuHunIcon[index] = imageView;
            imagePane.getChildren().add(imageView);
        }

        return yuHunIcon[index];
    }

}
