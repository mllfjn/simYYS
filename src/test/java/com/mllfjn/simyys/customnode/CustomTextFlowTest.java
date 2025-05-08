package com.mllfjn.simyys.customnode;

//import com.sun.javafx.tk.Toolkit;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

// 使用 TestFX 的扩展来处理 JavaFX 线程
@ExtendWith(ApplicationExtension.class)
public class CustomTextFlowTest {

    // 测试构造函数是否正确设置内容
    @Test
    public void testConstructor() {
        CustomTextFlow ct = new CustomTextFlow();
        assertSame(ct.textFlow, ct.getContent(), "内容应为 textFlow");
    }

    // 测试默认颜色添加文本
    @Test
    public void testAddTextDefaultColor() {
        CustomTextFlow ct = new CustomTextFlow();
        ct.addText("测试默认颜色");
        Platform.runLater(() -> {
            Text addedText = (Text) ct.textFlow.getChildren().get(0);
            assertEquals("测试默认颜色", addedText.getText(), "文本内容不匹配");
            assertEquals(Color.BLACK, addedText.getFill(), "默认颜色应为黑色");
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    // 测试指定颜色添加文本
    @Test
    public void testAddTextWithColor() {
        CustomTextFlow ct = new CustomTextFlow();
        ct.addText("攻击", CustomTextFlow.NumberType.ATTACK, 30);
        Platform.runLater(() -> {
            Text addedText = (Text) ct.textFlow.getChildren().get(0);
            assertEquals("攻击", addedText.getText(), "文本内容不匹配");
            assertEquals(Color.RED, addedText.getFill(), "攻击颜色应为红色");
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    // 测试枚举颜色值是否正确
    @Test
    public void testTextColorEnumValues() {
        assertEquals(Color.BLACK, CustomTextFlow.NumberType.NORMAL.color, "NORMAL 颜色应为黑色");
        assertEquals(Color.RED, CustomTextFlow.NumberType.ATTACK.color, "ATTACK 颜色应为红色");
        assertEquals(Color.ORANGE, CustomTextFlow.NumberType.CRITICAL.color, "CRITICAL 颜色应为橙色");
        assertEquals(Color.GREEN, CustomTextFlow.NumberType.HEAL.color, "Heal 颜色应为绿色");
    }

    // 测试多次添加文本
    @Test
    public void testMultipleAdditions() {
        CustomTextFlow ct = new CustomTextFlow();
        ct.addText("第一段");
        ct.addText("第二段", CustomTextFlow.NumberType.HEAL, 30);
        Platform.runLater(() -> {
            assertEquals(2, ct.textFlow.getChildren().size(), "应有两个文本节点");
            Text first = (Text) ct.textFlow.getChildren().get(0);
            assertEquals("第一段", first.getText());
            assertEquals(Color.BLACK, first.getFill());

            Text second = (Text) ct.textFlow.getChildren().get(1);
            assertEquals("第二段", second.getText());
            assertEquals(Color.GREEN, second.getFill());
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

}