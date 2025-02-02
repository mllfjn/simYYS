package com.mllfjn.simyys.customnode;

import com.mllfjn.simyys.starter.singleLine.CharacterLine;
import com.mllfjn.simyys.character.CharacterType;


public class SpecialChooser extends LabelChooser{
    private final CharacterLine line;


    public SpecialChooser(CharacterLine line) {
        super("");
        this.line = line;
    }

    @Override
    public String[] getTypeText() {
        return switch (CharacterType.getType(line.name.getText())) {
            case SHI_SHEN -> new String[]{"攻击两件套", "暴击两件套", "生命两件套", "防御两件套", "命中两件套", "抵抗两件套", "首领两件套"};
            case YYS -> new String[] {"契灵"};
            case MOB -> new String[] {"词条"};
            default -> null;
        };
    }

    @Override
    public String[][] getList() {
        return switch (CharacterType.getType(line.name.getText())) {
            case SHI_SHEN -> new String[][] {
                    new String[]{"隐念", "贝吹坊", "兵主部", "狂骨", "阴摩罗", "心眼", "鸣屋", "狰", "轮入道", "蝠翼"},
                    new String[]{"应声虫", "海月火玉", "青女房", "针女", "镇墓兽", "破势", "伤魂鸟", "网切", "三味"},
                    new String[]{"叠叩", "恶楼", "涂佛", "树妖", "薙魂", "钟灵", "镜姬", "被服", "涅槃之火", "地藏像"},
                    new String[]{"火之车", "出世螺", "魅妖", "珍珠", "木魅", "日女巳时", "反枕", "招财猫", "雪幽魂"},
                    new String[]{"元兴寺", "遗念火", "飞缘魔", "蚌精", "火灵"},
                    new String[]{"钓瓶火", "共潜", "幽谷响", "返魂香", "骰子鬼", "魍魉之匣"},
                    new String[]{"鬼灵歌姬", "蜃气楼", "地震鲶", "荒骷髅", "胧车", "土蜘蛛"}
            };
            case YYS -> new String[][] {new String[]{"契灵-镇墓兽", "契灵-火灵"}};
//            case typeYYS -> new String[][] {new String[]{"契灵-镇墓兽", "契灵-火灵", "契灵-茨球", "契灵-小黑"}};
            case MOB -> new String[][] {new String[]{"巧劲", "易碎", "咒术", "猛火", "狂风", "斗魂", "疾行"}};
            default -> null;
        };
        /*String[][] rt;
        switch (getType()) {
            case typeShiShen : {

            }
            case typeYYS :
            case typeMob :
        }*/
    }

    @Override
    public String getChooseText() {
        return switch (CharacterType.getType(line.name.getText())) {
            case SHI_SHEN -> "添加御魂";
            case YYS -> "添加契灵";
            case MOB -> "选择词条 （全都没做）";
            default -> "";
        };
    }

    @Override
    public void onMouseClicked() {
        if (this.getText().equals("+")) {
            super.onMouseClicked();
        } else if (!this.getText().isEmpty()) {
            this.setText("");
        }

        line.resetSpecial();
    }
}
