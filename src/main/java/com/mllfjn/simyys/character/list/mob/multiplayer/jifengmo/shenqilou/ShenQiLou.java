package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.EventRoundDone;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.list.mob.multiplayer.DisplayDamageRecord;
import com.mllfjn.simyys.character.list.mob.multiplayer.MultiStageManager;
import com.mllfjn.simyys.character.list.mob.multiplayer.StatusRecordDamage;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.CharacterJiFengMoBase;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusDieHandler;
import com.mllfjn.simyys.character.status.instance.StatusUnselectable;
import com.mllfjn.simyys.guihuo.GuiHuo;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;

public class ShenQiLou extends CharacterJiFengMoBase {
    public static final String CharacterName = "蜃气楼";

    private boolean diving = false;

    private final BattleActionListener listener = event -> {
        if (event instanceof EventRoundDone) {
            GuiHuo guiHuoInstance = bp.getGuiHuoInstance(CharacterFinder.getEnemyTeam(team));
            if (guiHuoInstance != null) {
                guiHuoInstance.fullyCharge();
            }
        }
        return false;
    };

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
                bp.removeActionListener(this, listener);
                removeStatus(StatusSQLPF.class);
            }

            DisplayDamageRecord infoDisplay = getInfoDisplay();
            for (int i = 0; i < 5; i++) {
                CharacterSummonBase xiaBing = new CharacterSummonBase(bp, "虾兵", team) {
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

                    @Override
                    protected void dieHandle() {
                        summonList.remove(this);
                        if (summonList.isEmpty()) {
                            multiStageManager.changeStage();
                        }
                    }
                };
                summonList.add(xiaBing);
                bp.addCharacter(xiaBing);
            }
        });

        // 第二次转阶段,召唤5个纸人
        multiStageManager.addStage(() -> {
            clearSummon();
            for (int i = 0; i < 5; i++) {
                CharacterSummonBase zhiRen = new CharacterSummonBase(bp, "纸人", team) {
                    {
                        setInitDefense(400);
                        setMaxHp(9999, true);
                        setMob(0, 1);
                    }

                    @Override
                    protected void dieHandle() {
                        summonList.remove(this);
                    }
                };
                summonList.add(zhiRen);
                bp.addCharacter(zhiRen);
            }
        });

        // 第三次转阶段,自身无法选中,召唤镜像,一个一定假,其他2真2假.进入下潜状态,无法使用技能
        multiStageManager.addStage(() -> {
            clearSummon();
            diving = true;
            canChangeStage = false;
            addStatus(new StatusUnselectable(this, this));
            List<Character> list = new CharacterFinder(this, true)
                    .filterEnemy()
                    .filterYYS(false)
                    .filterSummon(false)
                    .getList();
            for (Character existCharacter : list) {
                Character newCharacter = CharacterFactory.getCharacter(existCharacter.name).orElseThrow();
                newCharacter.reset(bp);
                newCharacter.name = existCharacter.name;
                newCharacter.team = team;
                newCharacter.setMaxHp(99999999, true);
                newCharacter.setMob(3, 3);
                newCharacter.setInitDefense(704);
                newCharacter.setInitSpeed(existCharacter.getInitSpeed());
                newCharacter.setInitBaseAttack(existCharacter.getInitBaseAttack());
                newCharacter.setInitAdditionAttack(existCharacter.getInitAdditionAttack());
                newCharacter.setInitCritRate(existCharacter.getInitCritRate());
                newCharacter.setInitCritPower(existCharacter.getInitCritPower());
                newCharacter.fillSkills();
                bp.addCharacter(newCharacter);
                summonList.add(newCharacter);
            }

            // 想不通第一个不是真这种BUG是怎么出的
            Character first = summonList.remove(0);
            List<Character> choose = RateController.choose("镜像-真", summonList, Character::getName, bp.calc, 2);
            for (Character character : choose) {
                character.addStatus(new StatusTrueMirror(character, getInfoDisplay()));
                character.addStatus(new StatusDieHandler(character, () -> {
                    summonList.remove(character);
                    if (summonList.isEmpty()) {
                        canChangeStage = true;
                        multiStageManager.changeStage();
                    }
                }));
            }
            summonList.add(first);
        });

        // 第四次转阶段,该阶段每次行动前回满火
        // 下沉防御85.547435(?),按照破防88%写吧
        // 由于蜃气楼可以重复多次所有阶段,执行完后再重新添加所有阶段
        multiStageManager.addStage(() -> {
            removeStatus(StatusUnselectable.class);
            addStatus(new StatusSQLPF(this));
            bp.addActionListener(this, listener);


            addStage(multiStageManager);
        });
    }

    @Override
    public void round() {
        if (!diving) {
            super.round();
        }
    }

    private void clearSummon() {
        if (!summonList.isEmpty()) {
            Character[] array = summonList.toArray(new Character[]{});
            for (Character character : array) {
                character.die();
            }
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

    static class StatusTrueMirror extends StatusRecordDamage implements Displayable {
        private final DisplayDamageRecord displayDamageRecord;

        public StatusTrueMirror(Character character, DisplayDamageRecord displayDamageRecord) {
            super(character);
            this.displayDamageRecord = displayDamageRecord;
        }

        @Override
        protected void addDamage(double damage) {
            displayDamageRecord.addDamage(damage);
        }

        @Override
        public String getDisplayText() {
            return "真";
        }
    }

    static class StatusSQLPF extends Status implements AttributeModifier {

        public StatusSQLPF(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return -belongTo.getInitDefense() * 0.88;
        }
    }
}
