package com.mllfjn.simyys.customnode;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Popup;

import java.util.function.Supplier;

public class CustomText extends Text {
    private final TextFlowLog.TextType type;
    private Supplier<String> tooltipSupplier;
    private Popup popup;

    public CustomText(String text, TextFlowLog.TextType type, TextFlowLog.TextColor textColor, TextFlowLog.FontSize fontSize) {
        super(text);
        this.type = type;
        this.setFont(new Font(fontSize.size));
        this.setFill(textColor.color);
        addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            if (popup == null && tooltipSupplier != null) {
                TextArea ta = new TextArea(tooltipSupplier.get());
                ta.setEditable(false);
                ta.setWrapText(false);
                popup = new Popup();
                popup.addEventHandler(MouseEvent.MOUSE_EXITED, e -> popup.hide());
                popup.getContent().add(ta);

                EventHandler<MouseEvent> handler = e -> {
                    Node intersectedNode = e.getPickResult().getIntersectedNode();
                    if (intersectedNode != this && intersectedNode != ta) {
                        popup.hide();
                    }
                };
                this.addEventHandler(MouseEvent.MOUSE_EXITED, handler);
                ta.addEventHandler(MouseEvent.MOUSE_EXITED, handler);
            }

            if (popup != null) {
                popup.show(this,
                        localToScreen(getBoundsInLocal()).getMinX(),
                        localToScreen(getBoundsInLocal()).getMinY()
                                + getBoundsInLocal().getHeight() - 3
                );
            }

        });
    }

    public TextFlowLog.TextType getType() {
        return type;
    }

    public void setTooltipSupplier(Supplier<String> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
    }
}
