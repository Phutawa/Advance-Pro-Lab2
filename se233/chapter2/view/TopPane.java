package se233.chapter2.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import se233.chapter2.Launcher;
import se233.chapter2.controller.AllEventHandlers;
import se233.chapter2.controller.Initialize;
import se233.chapter2.model.Currency;

import java.time.LocalDateTime;


public class TopPane extends FlowPane {
    private Button refresh;
    private Button add;
    private Button change;
    private Label update;

    public TopPane() {
        this.setPadding (new Insets(10));
        this. setHgap(10);
        this.setPrefSize (640,20);
        add = new Button ("Add");
        refresh = new Button ("Refresh");
        change = new Button(Launcher.getBaseCurrency());
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onRefresh();
            }
        });
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onAdd();
            }
        });
        change.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {AllEventHandlers.onChange();}
        });
        update = new Label();
        refreshPane ();
        this.getChildren().addAll(refresh, add,update);
        this.getChildren().addAll(change);

    }
    public void refreshPane() {
        update.setText(String.format("Last update: %s" , LocalDateTime.now().toString()));
        change.setText(Launcher.getBaseCurrency());
    }
}