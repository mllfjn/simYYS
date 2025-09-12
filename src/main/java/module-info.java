module com.mllfjn.simyys {
    requires javafx.controls;
    requires javafx.base;
    requires com.google.gson;

    opens com.mllfjn.simyys to
            javafx.fxml,
            com.google.gson,
            org.testfx,
            org.testfx.junit5;
    opens com.mllfjn.simyys.starter to com.google.gson;
    //opens com.mllfjn.simyys.character to com.google.gson;
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
    exports com.mllfjn.simyys.determinant;
    exports com.mllfjn.simyys.ratecontroller;
    exports com.mllfjn.simyys.character.propertygetter;
    opens com.mllfjn.simyys.character.propertygetter to com.google.gson;
    //opens com.mllfjn.simyys.character.skill to com.google.gson;
    //opens com.mllfjn.simyys.trigger to com.google.gson, javafx.fxml, org.testfx, org.testfx.junit5;
    //opens com.mllfjn.simyys.hit to com.google.gson, javafx.fxml, org.testfx, org.testfx.junit5;

}