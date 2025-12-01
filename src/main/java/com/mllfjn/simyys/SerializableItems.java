package com.mllfjn.simyys;

import com.mllfjn.simyys.trigger.battleevent.BattleActionListener;
import com.mllfjn.simyys.character.Character;

import java.io.Serializable;
import java.util.*;

public class SerializableItems implements Serializable {
    // 角色列表
    public final List<Character> characters = new ArrayList<>();
    // 当前行动角色
    public Character characterActing;
    // 记录获得新回合的单位
    // 这里的逻辑按照nga的帖子很乱,还要分是不是和怪物战斗,先简化随便取以后再改 https://bbs.nga.cn/read.php?tid=32486237
    private final Stack<Character> newRound = new Stack<>();
    public boolean disablePush = false;
    // 队伍面板，负责显示头像和管理鬼火条
    public final TeamPane[] teamPane = new TeamPane[2];
    // 全局监听器,用于幻境,结界
    public final Map<Character, List<BattleActionListener>> listenerMap = new HashMap<>();
    // 保存的概率
    private double currentRate;

    public SerializableItems() {
        teamPane[0] = new TeamPane();
        teamPane[1] = new TeamPane();
    }

    public Optional<Character> getAutoTo(int team) {
        return teamPane[team].getAuto();
    }

    public double getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(double currentRate) {
        this.currentRate = currentRate;
    }

    public Optional<Character> newRoundCharacter() {
        if (newRound.isEmpty()) {
            return Optional.empty();
        }

        disablePush = true;
        return Optional.of(newRound.pop());
    }

    public void getNewRound(Character character) {
        newRound.push(character);
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
        teamPane[character.team].removeCharacter(character);
        listenerMap.remove(character);
    }

    public void addCharacter(Character character) {
        characters.add(character);
        teamPane[character.team].addCharacter(character);
    }
}