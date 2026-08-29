package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.list.mob.multiplayer.DisplayDamageRecord;
import com.mllfjn.simyys.character.list.mob.multiplayer.MultiStageManager;
import com.mllfjn.simyys.character.list.mob.multiplayer.StatusRecordDamage;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.CharacterJiFengMoBase;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusUnselectable;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.ArrayList;
import java.util.List;

public class ShenQiLou extends CharacterJiFengMoBase {
    public static final String CharacterName = "蜃气楼";

    private boolean diving = false;

    private final Status status;

    private final List<CharacterMirror> mirrors = new ArrayList<>();

    public ShenQiLou() {
        status = Status.of("蜃气楼减防", this)
                .attribute(Attribute.DEFENCE, _ -> -0.88 * getInitDefense());
    }

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);
        this.bp.addPriorityMove(this, () -> {
            List<Character> list = new CharacterFinder(this, true)
                    .filterEnemy()
                    .filterYYS(false)
                    .filterSummon(false)
                    .getList();

            for (Character character : list) {
                mirrors.add(new CharacterMirror(this, character));
            }
        });
    }

    @Override
    protected void addStage(MultiStageManager multiStageManager) {
        // 第一次转阶段
        // 如果处于下潜状态,上浮
        // 使用"极·虾兵蟹将"召唤五只虾兵,但技能列表没有这个技能
        // 游戏内信息显示是叫"蜃气楼大将",防御704
        // 虾兵死后自动进入二阶段
        multiStageManager.addStage(() -> {
            if (diving) {
                diving = false;
                List<Character> list = new CharacterFinder(this, true)
                        .filterEnemy()
                        .getList();
                list.forEach(c -> c.removeStatus(StatusFullyChargeGuiHuo.class));
                removeStatus(status);
            }

            DisplayDamageRecord infoDisplay = getInfoDisplay();
            multiStageManager.setAutoChangeStage(true);
            for (int i = 0; i < 5; i++) {
                multiStageManager.addSummon(new CharacterSummonBase(bp, "虾兵", team) {
                    {
                        setInitDefense(704);
                        setInitBaseAttack(999);
                        setInitSpeed(100);
                        setMaxHp(99999999, true);

                        setMob(0, 1);

                        addStatus(new StatusRecordDamage(this) {
                            @Override
                            protected void addDamage(double damage) {
                                infoDisplay.addDamage(damage);
                            }
                        });
                    }
                });
            }
        });

        // 第二次转阶段,召唤5个纸人
        multiStageManager.addStage(() -> {
            multiStageManager.clearSummon();
            for (int i = 0; i < 5; i++) {
                multiStageManager.addSummon(new CharacterSummonBase(bp, "纸人", team) {
                    {
                        setInitDefense(400);
                        setMaxHp(9999, true);
                        setMob(0, 1);
                    }
                });
            }
        });

        // 第三次转阶段,自身无法选中,召唤镜像,一个一定假,其他2真2假.进入下潜状态,无法使用技能
        multiStageManager.addStage(() -> {
            multiStageManager.clearSummon();
            diving = true;
            multiStageManager.setCanChangeStage(false);
            addStatus(new StatusUnselectable(this, this));

            for (CharacterMirror mirror : mirrors) {
                multiStageManager.addSummon(mirror.getInstance());
            }

            // 想不通第一个不是真这种BUG是怎么出的
            List<Character> summonList = multiStageManager.getSummonList();
            Character first = summonList.removeFirst();
            List<Character> choose = RateController.choose("镜像-真", summonList, Character::getName, bp.calc, 2);
            for (Character character : choose) {
                StatusRecordDamage statusRecordDamage = new StatusRecordDamage(character) {
                    @Override
                    protected void addDamage(double damage) {
                        getInfoDisplay().addDamage(damage);
                    }
                };
                statusRecordDamage.display(() -> "真").addTo();
            }
            summonList.add(first);
            multiStageManager.setSummonDieCallback(c -> {
                if (choose.size() == 1) {
                    multiStageManager.clearSummon();
                    multiStageManager.changeStage();
                } else {
                    choose.remove(c);
                }
            });
        });

        // 第四次转阶段,该阶段每次行动前回满火
        // 下沉防御85.547435(?),按照破防88%写吧
        // 由于蜃气楼可以重复多次所有阶段,执行完后再重新添加所有阶段
        multiStageManager.addStage(() -> {
            removeStatus(StatusUnselectable.class);
            addStatus(status);
            List<Character> list = new CharacterFinder(this)
                    .filterEnemy()
                    .getList();
            for (Character character : list) {
                character.addStatus(new StatusFullyChargeGuiHuo(this, character));
            }

            addStage(multiStageManager);
        });
    }

    @Override
    public void round() {
        if (!diving) {
            super.round();
        }
    }

    @Override
    public void addOwnSkills() {
        addSkill(new Skill1(this));
        addSkill(new Skill2(this));
        addSkill(new Skill3(this));
        addSkill(new Skill4(this));
        Skill8 skill8 = new Skill8(this);
        addSkill(new Skill5(this, skill8));
        addSkill(new Skill6(this));
        addSkill(new Skill7(this));
        addSkill(skill8);
    }

    @Override
    protected boolean useSkillAuto() {
        // 只要带盾，直接放4.钳鳌重击
        // 如果没盾有3火,优先放3.绀连击,在冷却放2.蜃气爆弹
        return tryUseSkill(4) || tryUseSkill(3) || tryUseSkill(2);
    }

    @Override
    protected String getJiFengMoSpeed() {
        return "200";
    }

    static class StatusFullyChargeGuiHuo extends Status {
        public StatusFullyChargeGuiHuo(Character from, Character belongTo) {
            super("回火", from, belongTo);
            runOn(Trigger.BEFORE_ROUND, _ ->
                    belongTo.bp().getGuiHuoInstance(belongTo.team).fullyCharge()
            );
        }
    }
}
