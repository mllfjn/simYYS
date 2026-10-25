package com.mllfjn.simyys.utils.qrcodeloader;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class QiLingData {
    public int id; // 契灵id
    public int star; // 星级,无用
    public int level; // 等级,无用
    public int relevantYYS; // 对应阴阳师,有用但是为什么这里阴阳师和英杰中间没空一个,考虑到基本不装备不对应的契灵,没必要检测
    // 晴明1,神乐2,博雅4,藤原6
    public int[] shuYin; // 术印

    public QiLingData() {
    }

    public int getShuYin(int skillID) {
        int count = 0;
        for (int i : shuYin) {
            if (i == skillID) {
                count++;
            }
        }
        return count;
    }
}
