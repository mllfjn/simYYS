package com.mllfjn.simyys.starter;

import com.mllfjn.simyys.character.PropertyKey;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;
import com.mllfjn.simyys.character.propertygetter.PropertyCheck;
import com.mllfjn.simyys.character.propertygetter.PropertyInput;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

public class CustomTableView extends TableView<PropertiesHolder> {
    public CustomTableView() {
        super();
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.setEditable(true);

        TableColumn<PropertiesHolder, String> nameColumn = new TableColumn<>("名称");
        nameColumn.setCellValueFactory(param -> param.getValue().getNameProperty());

        this.getColumns().add(nameColumn);

        for (String key : PropertyKey.GENERAL_INPUT_KEYS) {
            TableColumn<PropertiesHolder, String> column = new TableColumn<>(key);
            column.setCellValueFactory(param -> {
                PropertyInput pi = (PropertyInput) param.getValue().map.get(key);
                return pi.getProperty();
            });
            column.setEditable(true);
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            this.getColumns().add(column);
        }

        for (String key : PropertyKey.GENERAL_CHECK_KEYS) {
            TableColumn<PropertiesHolder, Boolean> column = new TableColumn<>(key);
            column.setCellValueFactory(param -> {
                PropertyCheck pc = (PropertyCheck) param.getValue().map.get(key);
                return pc.getProperty();
            });
            column.setEditable(true);
            column.setCellFactory(CheckBoxTableCell.forTableColumn(column));
            this.getColumns().add(column);
        }

        TableColumn<PropertiesHolder, String> columnTotalAttack = new TableColumn<>("总攻击");
        columnTotalAttack.setCellValueFactory(param -> param.getValue().getTotalAttack());
        this.getColumns().add(2, columnTotalAttack);
    }
}
