module com.mllfjn.simyys {
    requires javafx.controls;
    requires javafx.base;
    requires com.google.gson;
    requires org.jetbrains.annotations;
    requires javafx.graphics;
    requires com.mllfjn.simyys;

    opens com.mllfjn.simyys to
            javafx.fxml,
            com.google.gson,
            org.testfx,
            org.testfx.junit5;
    opens com.mllfjn.simyys.starter to com.google.gson;
    opens com.mllfjn.simyys.customnode to org.testfx.junit5;


    exports com.mllfjn.simyys;
    exports com.mllfjn.simyys.starter;
    exports com.mllfjn.simyys.character;
    exports com.mllfjn.simyys.state;
    exports com.mllfjn.simyys.trigger;
    exports com.mllfjn.simyys.interactive;
    exports com.mllfjn.simyys.character.skill;
    exports com.mllfjn.simyys.customnode;
    opens com.mllfjn.simyys.character to com.google.gson, javafx.fxml, org.testfx, org.testfx.junit5;
    exports com.mllfjn.simyys.guihuo;
    exports com.mllfjn.simyys.state.determinant;
    exports com.mllfjn.simyys.ratecontroller;
    exports com.mllfjn.simyys.character.propertygetter;
    opens com.mllfjn.simyys.character.propertygetter to com.google.gson, org.testfx.junit5;
    exports com.mllfjn.simyys.utils;
    opens com.mllfjn.simyys.utils to com.google.gson, javafx.fxml, org.testfx, org.testfx.junit5;
    exports com.mllfjn.simyys.collections;
    opens com.mllfjn.simyys.collections to com.google.gson, javafx.fxml, org.testfx, org.testfx.junit5;

}