module com.mllfjn.simyys {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.graphics;
    requires static org.jetbrains.annotations;

    opens com.mllfjn.simyys to javafx.fxml;


    exports com.mllfjn.simyys;
    exports com.mllfjn.simyys.starter;
    exports com.mllfjn.simyys.character;
    exports com.mllfjn.simyys.character.status;
    exports com.mllfjn.simyys.interactive;
    exports com.mllfjn.simyys.character.skill;
    exports com.mllfjn.simyys.customnode;
    opens com.mllfjn.simyys.character to javafx.fxml;
    exports com.mllfjn.simyys.guihuo;
    exports com.mllfjn.simyys.character.status.determinant;
    exports com.mllfjn.simyys.ratecontroller;
    exports com.mllfjn.simyys.character.propertygetter;
    exports com.mllfjn.simyys.utils;
    opens com.mllfjn.simyys.utils to javafx.fxml;
    exports com.mllfjn.simyys.collections;
    opens com.mllfjn.simyys.collections to javafx.fxml;
    exports com.mllfjn.simyys.character.yuhun;
    opens com.mllfjn.simyys.character.yuhun to javafx.fxml;
    exports com.mllfjn.simyys.battleevent;
    exports com.mllfjn.simyys.character.status.triggerParam;
    exports com.mllfjn.simyys.character.yuhun.list;
    opens com.mllfjn.simyys.character.yuhun.list to javafx.fxml;

}