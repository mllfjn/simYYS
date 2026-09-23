package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

class Skill3Special_or_Skill5 extends Skill {
    private final Skill3 skill3;

    public Skill3Special_or_Skill5(Character belongTo, int level, Skill3 skill3) {
        super(belongTo, level, 0, 0, 5);
        this.skill3 = skill3;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        boolean isHaveJiuWei = false;
        for (Status status : getBelongTo().getStatuses()) {
            if (status instanceof StatusUsedSkill3Mark) {
                return false;
            } else if (status instanceof StatusJiuWei) {
                isHaveJiuWei = true;
            }
        }
        return isHaveJiuWei && super.canUse(bp);
    }

    @Override
    public String getName() {
        return Skill3.SkillName + "(手动)";
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        GeYe belongTo = ((GeYe) getBelongTo());
        int maxTarget = belongTo.getStatus(StatusJiuWei.class).orElseThrow().getStack();
        StatusDaYao statusDaYao = belongTo.getStatus(StatusDaYao.class).orElseGet(() -> {
            StatusDaYao status = new StatusDaYao(belongTo, skill3.getInitTeammateCount(), getLevel());
            belongTo.addStatus(status);
            return status;
        });

        List<Character> huanHuaList = statusDaYao.getHuanHuaList();
        ArrayList<Character> adds = new ArrayList<>(3);

        HBox hBox = new HBox();

        ArrayList<Character> removes;
        if (!huanHuaList.isEmpty()) {
            removes = new ArrayList<>(3);
            for (Character character : huanHuaList) {
                FXImageSelector fxImageSelector = new FXImageSelector(removes, character, true,
                        () -> huanHuaList.size() + adds.size() - removes.size() < 3
                );
                hBox.getChildren().add(fxImageSelector);
            }
        } else {
            removes = null;
        }

        List<Character> list = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSelf()
                .filterYYS(false)
                .getList();
        for (Character character : list) {
            hBox.getChildren().add(new FXImageSelector(adds, character, false, () -> {
                if (removes != null) {
                    return huanHuaList.size() + adds.size() - removes.size() < maxTarget;
                } else {
                    return huanHuaList.size() + adds.size() < maxTarget;
                }
            }));
        }

        Stage stage = new Stage();

        VBox vBox = new VBox();
        Button button = new Button("确定");
        button.setPrefWidth(100);
        button.setOnAction(event -> stage.close());
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(hBox, new Text("至多选择" + maxTarget + "名式神"), button);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));

        stage.setTitle("幻化");
        stage.setScene(new Scene(vBox));
        stage.showAndWait();

        for (Character character : adds) {
            statusDaYao.addHuanHua(character);
        }

        if (removes != null && !removes.isEmpty()) {
            for (Character character : removes) {
                statusDaYao.removeHuanHua(character);
            }
        }
        statusDaYao.changeDone();

        // 获得新回合
        belongTo.getInteractive().getNewRound(belongTo);
        // 刚用过不能再次用,考虑到变身会清除所有印记,应该不用考虑时之晖
        belongTo.addStatus(new StatusUsedSkill3Mark(belongTo));

        return Optional.empty();
    }
}
