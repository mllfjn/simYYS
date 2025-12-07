package com.mllfjn.simyys.customnode;

import com.mllfjn.simyys.collections.SerializableObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;

import java.io.Serializable;

public class ListViewWithBasicController<T extends Serializable> extends NodeWithController {
    ListView<T> listView = new ListView<>();
    SerializableObservableList<T> items;

    public ListViewWithBasicController(SerializableObservableList<T> items) {
        super();
        this.items = items;
        listView.setItems(items.getObservableList());
        setNode(listView);
    }

    public ListView<T> getListView() {
        return listView;
    }

    public void setDefaultControlButtons(EventHandler<ActionEvent> e) {
        setAddButton(e);
        setDeleteButton();
        setUpButton();
        setDownButton();
        setClearButton();
    }

    public void setAddButton(EventHandler<ActionEvent> e) {
        addControlButton("添加", e);
    }

    public void setDeleteButton() {
        addControlButton("删除", e -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < items.size()) {
                items.remove(index);
                if (index < items.size()) {
                    listView.getSelectionModel().select(index);
                }
            }
        });
    }

    public void setUpButton() {
        addControlButton("上移", e -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index > 0 && index < items.size()) {
                items.add(index - 1, items.remove(index));
                listView.getSelectionModel().select(index - 1);
            }
        });
    }

    public void setDownButton() {
        addControlButton("下移", e -> {
            int index = listView.getSelectionModel().getSelectedIndex();
            if (index < items.size() - 1 && index >= 0) {
                items.add(index + 1, items.remove(index));
                listView.getSelectionModel().select(index + 1);
            }
        });
    }

    public void setClearButton() {
        addControlButton("清空", e -> items.clear());
    }
}
