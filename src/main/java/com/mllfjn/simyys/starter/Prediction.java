package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.collections.SerializableObservableList;
import com.mllfjn.simyys.customnode.ListViewWithBasicController;
import com.mllfjn.simyys.utils.Utils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Prediction implements Serializable {
    public static final String NOT_PREDICTION = "不限";
    @Serial
    private static final long serialVersionUID = -4891093640103493137L;

    public SerializableObservableList<CharacterNameAndTeam> predictionOrder = new SerializableObservableList<>();

    public void showPrediction(Scene ownerScene, SerializableObservableList<PropertiesHolder> items) {
        Stage stage = new Stage();
        ListViewWithBasicController<CharacterNameAndTeam> customListView
                = new ListViewWithBasicController<>(predictionOrder);


        customListView.getListView().setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(CharacterNameAndTeam item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText("\t" + (getIndex() + 1) + "\t" + item);
                }
            }
        });

        customListView.setDefaultControlButtons(
                e -> showAddStage(customListView, items, stage, new AtomicInteger(predictionOrder.size())));

        customListView.addControlButton("从当前位置开始添加", e -> showAddStage(customListView, items, stage
                , new AtomicInteger(customListView.getListView().getSelectionModel().getSelectedIndex())), 1);

        ownerScene.getRoot().setMouseTransparent(true);
        stage.setOnCloseRequest(event -> ownerScene.getRoot().setMouseTransparent(false));
        Initializer.installScale(stage, customListView, 900, 600);
        stage.showAndWait();
    }

    private void showAddStage(ListViewWithBasicController<CharacterNameAndTeam> customListView
            , SerializableObservableList<PropertiesHolder> items, Stage stage, AtomicInteger startIndex) {
        Set<CharacterNameAndTeam> set = getUsedCharacterName(items);
        if (set == null) return;

        FlowPane fp0 = new FlowPane();
        FlowPane fp1 = new FlowPane();
        for (CharacterNameAndTeam c : set) {
            String name = c.name();
            int team = c.team();

            Button button = new Button(name);
            button.setOnAction(e1 -> {
                startIndex.set(startIndex.get() + 1);
                predictionOrder.add(startIndex.get(), new CharacterNameAndTeam(name, team));
                customListView.getListView().scrollTo(startIndex.get());
                customListView.getListView().getSelectionModel().select(startIndex.get());
            });
            button.setPrefWidth(75);

            if (team == 0) {
                fp0.getChildren().add(button);
            } else {
                fp1.getChildren().add(button);
            }
        }

        Button btnNot = new Button(NOT_PREDICTION);
        btnNot.setOnAction(e1 -> {
            startIndex.set(startIndex.get() + 1);
            predictionOrder.add(startIndex.get(), new CharacterNameAndTeam(NOT_PREDICTION, 0));
            customListView.getListView().scrollTo(startIndex.get());
            customListView.getListView().getSelectionModel().select(startIndex.get());
        });
        btnNot.setPrefWidth(75);

        GridPane gp = new GridPane();
        gp.add(new Text("特殊"), 0, 0);
        gp.add(btnNot, 1, 0);

        gp.add(new Text("己方"), 0, 1);
        gp.add(fp0, 1, 1);

        gp.add(new Text("敌方"), 0, 2);
        gp.add(fp1, 1, 2);

        gp.setHgap(20);
        gp.setVgap(20);
        gp.setPadding(new Insets(20));

        Stage addStage = new Stage();
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(stage);

        Initializer.installScale(addStage, gp, 500, 600);

        addStage.showAndWait();
    }

    public void check(SerializableObservableList<PropertiesHolder> items, Stage stage, Runnable back) {
        if (items.isEmpty()) {
            Utils.information("请先添加角色");
            return;
        }
        BattlePane battlePane = new BattlePane(items);

        for (int i = 0; i < predictionOrder.size(); i++) {
            CharacterNameAndTeam pre = predictionOrder.get(i);
            String preName = pre.name();
            int preTeam = pre.team();
            // 不作预测
            if (preName.equals(NOT_PREDICTION)) {
                continue;
            }

            Character real = battlePane.getCharacterActing();
            // 预测错误
            if (!preName.equals(real.name) || real.team != preTeam) {
                Utils.error("不符合预期!\n" +
                                "第" + (i + 1) + "个角色\n预测为:队伍" + preTeam + "-" + preName
                                + "\n实际为:队伍" + real.team + "-" + real.name
                        , "跳转至不符合位置", () -> battlePane.predictionShow(stage, back));
                return;
            }
            battlePane.next(false);
        }
        Utils.information("符合预期");
    }

    private Set<CharacterNameAndTeam> getUsedCharacterName(SerializableObservableList<PropertiesHolder> items) {
        Set<CharacterNameAndTeam> set = new LinkedHashSet<>();
        for (PropertiesHolder item : items) {
            set.add(new CharacterNameAndTeam(item.name
                    , item.propertiesMap.get(PropertyKey.GENERAL_TEAM_KEY).getBoolean() ? 1 : 0));
        }
        if (set.isEmpty()) {
            Utils.error("请先添加角色");
            return null;
        }
        return set;
    }

}
