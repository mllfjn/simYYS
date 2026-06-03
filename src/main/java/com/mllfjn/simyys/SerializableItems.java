package com.mllfjn.simyys;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.propertygetter.FlagChangeInfo;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.instance.StatusUnselectable;
import com.mllfjn.simyys.collections.SafeList;
import com.mllfjn.simyys.utils.SerialFunction;
import com.mllfjn.simyys.utils.SerializableRunnable;

import java.io.Serializable;
import java.util.*;

public class SerializableItems implements Serializable {
    // 角色列表
    public final List<Character> characters = new ArrayList<>();
    // 行动条改变参与单位
    private final List<Character> charactersChangeLocation = new ArrayList<>();
    // 目标选取参与单位
    private final List<Character> charactersSelectable = new ArrayList<>();
    // 死亡角色列表
    public final List<Character> deadCharacters = new ArrayList<>();
    // 当前行动角色
    public Character characterActing;
    // 记录获得新回合的单位
    // 这里的逻辑按照nga的帖子很乱,还要分是不是和怪物战斗,先简化随便取以后再改 https://bbs.nga.cn/read.php?tid=32486237
    private final Stack<Character> newRound = new Stack<>();
    // 获得时之隙新回合的单位
    private final Stack<Character> sZXNewRound = new Stack<>();
    public boolean disablePush = false;
    // 队伍面板，负责显示头像和管理鬼火条
    public final TeamPane[] teamPane = new TeamPane[2];
    // 全局监听器,用于监听角色死亡,"任意回合"等
    public final SafeList<BattleActionListener> listeners = new SafeList<>();
    // 状态添加器,用于结界,幻境等
    public final List<StatusAdder<?>> statusAdders = new ArrayList<>();
    // 保存的概率
    private double currentRate;
    // 先机
    private final List<PriorityMove> priorityMoves = new ArrayList<>();

    // 多回目战斗
    private int team0Wave = 1;
    private int team1Wave = 1;

    // 上一步中有多少个回合
    public int roundInLastStep = 0;

    public SerializableItems() {
        teamPane[0] = new TeamPane();
        teamPane[1] = new TeamPane();
    }

    public Optional<Character> getAutoTo(int team, FlagChangeInfo.FlagType flagType) {
        return teamPane[team].getAuto(flagType);
    }

    public double getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(double currentRate) {
        this.currentRate = currentRate;
    }

    public Optional<Character> sZXNewRoundCharacter() {
        // 获得时之隙的单位
        if (!sZXNewRound.isEmpty()) {
            Character pop = sZXNewRound.pop();
            pop.statusRun(Trigger.OUT_ROUND_ACTION, null);
            return Optional.of(pop);
        }

        return Optional.empty();
    }

    public Optional<Character> newRoundCharacter() {
        // 获得新回合的单位
        if (!newRound.isEmpty()) {
            disablePush = true;
            return Optional.of(newRound.pop());
        }

        return Optional.empty();
    }

    public void getSZXNewRound(Character character) {
        sZXNewRound.push(character);
    }

    public void getNewRound(Character character) {
        if (!newRound.contains(character)) {
            newRound.push(character);
        }
    }

    public void addProgress(int team) {
        if (!disablePush) {
            teamPane[team].addProgress();
        }
        disablePush = false;
    }

    public void reset(BattlePane bp) {
        teamPane[0].reset(bp);
        teamPane[1].reset(bp);
    }

    public void removeCharacter(Character character) {
        characters.remove(character);
        charactersChangeLocation.remove(character);
        charactersSelectable.remove(character);
        deadCharacters.add(character);

        for (TeamPane teamPane : teamPane) {
            teamPane.removeCharacter(character);
        }


        listeners.removeIf(listener -> listener.fromCharacter == character);
    }

    public void addCharacter(Character character) {
        characters.add(character);
        charactersChangeLocation.add(character);
        if (!character.isHaveStatus(StatusUnselectable.class)) {
            charactersSelectable.add(character);
        }
        teamPane[character.team].addCharacter(character);
    }

    public List<Character> getCharactersChangeLocation() {
        return charactersChangeLocation;
    }

    public void canNotChangeLocation(Character character) {
        charactersChangeLocation.remove(character);
    }

    public void canChangeLocation(Character character) {
        charactersChangeLocation.add(character);
    }

    public List<Character> getCharactersSelectable() {
        return charactersSelectable;
    }

    public void unSelectable(Character character) {
        charactersSelectable.remove(character);
    }

    public void selectable(Character character) {
        charactersSelectable.add(character);
    }

    public void setAuto(Character characterSelected, FlagChangeInfo.FlagType flagType) {
        // CS是需要设置红绿标的目标
        // 如果flagType是绿标,那么给CS所在的队伍设置绿标,CS本人设置绿标
        // 如果flagType是红标,那么给CS对面的队伍设置红标,CS本人设置红标

        if (flagType == FlagChangeInfo.FlagType.GREEN) {
            teamPane[characterSelected.team].setAuto(characterSelected, flagType);
        } else {
            teamPane[characterSelected.team == 0 ? 1 : 0].setAuto(characterSelected, flagType);
        }
    }

    public void priorityMove() {
        for (PriorityMove priorityMove : priorityMoves) {
            if (priorityMove.character.alive) {
                priorityMove.runnable.run();
            }
        }
    }

    public void addPriorityMove(Character character, SerializableRunnable runnable) {
        priorityMoves.add(new PriorityMove(character, runnable));
    }

    public int getWave(int team) {
        return team == 0 ? team0Wave : team1Wave;
    }

    public void addWave(int team) {
        if (team == 0) {
            team0Wave++;
        } else {
            team1Wave++;
        }
    }

    public <T extends Status> StatusAdder<T> addStatusAdder(SerialFunction<Character, T> statusProvider) {
        StatusAdder<T> adder = new StatusAdder<>(this, statusProvider);
        statusAdders.add(adder);
        return adder;
    }

    public void removeStatusAdder(StatusAdder<?> statusAdder) {
        statusAdders.remove(statusAdder);
    }

    private record PriorityMove(Character character, SerializableRunnable runnable) implements Serializable {
    }
}