package com.mllfjn.simyys.guihuo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventUseGuiHuo;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.list.sp.yinfan.StatusYuanLi;
import com.mllfjn.simyys.character.yuhun.list.youchizi.StatusYCZ;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.io.Serializable;

public class GuiHuo implements Serializable {
    private int max = 8;
    private int now;
    private int increment = 3;
    private int progress;
    private transient Label guiHuoDisplay;

    private StatusYuanLi yuanLi;
    private StatusYCZ ycz;

    public GuiHuo(int startWith) {
        now = startWith;
    }

    public Node getGuiHuoDisplay() {
        guiHuoDisplay = new Label();
        guiHuoDisplay.setFont(new Font(20));
        repaint();
        StackPane stackPane = new StackPane(guiHuoDisplay);
        stackPane.setAlignment(Pos.BOTTOM_CENTER);
        return stackPane;
    }

    public boolean canUseGuiHuo(int num) {
        // 如果鬼火够直接返回true
        if (now >= num) {
            return true;
        }

        // 鬼火不够时检查替代鬼火
        int needUse = num - now;
        // 愿力
        if (yuanLi != null) {
            needUse -= yuanLi.maxUse();
            if (needUse <= 0) {
                return true;
            }
        }

        if (ycz != null) {
            return ycz.maxUse() >= needUse;
        }

        return false;
    }

    public void useGuiHuo(BattlePane bp, Character character, int num) {
        if (num <= now) {
            now -= num;
            bp.interactive.guiHuo(character, -num, "鬼火");
            bp.onTrigger(new EventUseGuiHuo(character.team, num));
        } else {
            int useGuiHuo = now;
            int needUse = num - now;
            now = 0;

            if (useGuiHuo > 0) {
                bp.interactive.guiHuo(character, -useGuiHuo, "鬼火");
                bp.onTrigger(new EventUseGuiHuo(character.team, useGuiHuo));
            }

            if (yuanLi != null) {
                int useYuanLi = Math.min(needUse, yuanLi.maxUse());
                needUse -= useYuanLi;
                yuanLi.use(useYuanLi);
                bp.interactive.guiHuo(character, -useYuanLi, StatusYuanLi.StatusName);
            }

            if (needUse > 0) {
                ycz.use(needUse);
            }

        }
        repaint();
    }

    public void addProgress() {
        progress++;
        if (progress == 5) {
            progress = 0;
            gainGuiHuo(increment, false);
            if (increment < 5) {
                increment++;
            }
        }
        repaint();
    }

    public void gainGuiHuo(int num, boolean isFromYuHun) {
        if (yuanLi != null) {
            num = yuanLi.getGuiHuo(num, isFromYuHun);
        }

        if (num != 0) {
            now = Math.min(now + num, max);
        }

        repaint();
    }

    public int getNow() {
        return now;
    }

    public void setMax(int num) {
        max = num;
        now = Math.min(now, max);
        repaint();
    }

    private void repaint() {
        // 当前鬼火：4/8  进度：2/5(+4)
        if (guiHuoDisplay != null) {
            guiHuoDisplay.setText("当前鬼火:" + now + "/" + max + "-进度:" + progress + "/5(+" + increment + ")");
        }
    }

    public void setYuanLi(StatusYuanLi yuanLi) {
        this.yuanLi = yuanLi;
    }

    public void setYCZ(StatusYCZ ycz) {
        this.ycz = ycz;
    }
}
