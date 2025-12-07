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

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Prediction implements Serializable {
    public static final String NOT_PREDICTION = "不限";

    public SerializableObservableList<CharacterNameAndTeam> predictionOrder = new SerializableObservableList<>();

    public void showPrediction(Stage owner, SerializableObservableList<PropertiesHolder> items) {
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

        customListView.setDefaultControlButtons(e -> {
            Set<CharacterNameAndTeam> set = getUsedCharacterName(items);
            if (set == null) return;

            FlowPane fp0 = new FlowPane();
            FlowPane fp1 = new FlowPane();
            for (CharacterNameAndTeam c : set) {
                String name = c.name();
                int team = c.team();

                Button button = new Button(name);
                button.setOnAction(e1 -> {
                    predictionOrder.add(new CharacterNameAndTeam(name, team));
                    customListView.getListView().scrollTo(predictionOrder.size() - 1);
                });
                button.setPrefWidth(100);

                if (team == 0) {
                    fp0.getChildren().add(button);
                } else {
                    fp1.getChildren().add(button);
                }
            }

            Button btnNot = new Button(NOT_PREDICTION);
            btnNot.setOnAction(e1 -> {
                predictionOrder.add(new CharacterNameAndTeam(NOT_PREDICTION, 0));
                customListView.getListView().scrollTo(predictionOrder.size() - 1);
            });
            btnNot.setPrefWidth(100);

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
            addStage.setScene(new Scene(gp));
            addStage.initModality(Modality.APPLICATION_MODAL);
            addStage.initOwner(stage);

            addStage.showAndWait();
        });

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setScene(new Scene(customListView));
        stage.showAndWait();
    }

    public void check(SerializableObservableList<PropertiesHolder> items) {
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
                Utils.information("不符合预期!\n" +
                        "第" + (i + 1) + "个角色\n预测为:队伍" + preTeam + "的" + preName
                        + "\n实际为:队伍" + real.team + "的" + real.name);
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
            Utils.information("请先添加角色");
            return null;
        }
        return set;
    }

}
