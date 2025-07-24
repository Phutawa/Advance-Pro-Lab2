package se233.chapter2.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import se233.chapter2.controller.AllEventHandlers;
import se233.chapter2.controller.draw.DrawGraphTask;
import se233.chapter2.model.Currency;


import java.util.concurrent.*;

public class CurrencyPane extends BorderPane {
    private Currency currency;
    private Button delete;
    private Button watch;
    private Button unwatch;

    public CurrencyPane(Currency currency) {
        this.watch = new Button("Watch");
        this.delete = new Button("Delete");
        this.unwatch = new Button("Unwatch");
        this.watch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onWatch(currency.getShortCode());
            }
        });
        this.delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AllEventHandlers.onDelete(currency.getShortCode());
            }
        });
        this.unwatch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {AllEventHandlers.onUnwatch(currency.getShortCode());}
        });
        this.setPadding(new Insets(0));
        this.setPrefSize(640, 300);
        this.setStyle("-fx-border-color: black");
        try {
            this.refreshPane(currency);
        }catch(ExecutionException e){
            System.out.println("Encountered an execution exception.");
        }catch (InterruptedException e){
            System.out.println("Encountered an interupted exception.");
        }
    }

    //    public void refreshPane(Currency currency) throws ExecutionException,  InterruptedException {
//    this.currency =currency;
//    Pane currencyInfo = genInfoPane();
//    FutureTask futureTask = new FutureTask<VBox>(new DrawGraphTask(currency));
//    ExecutorService executor = Executors.newSingleThreadExecutor();
//    executor.execute(futureTask);
//    VBox currencyGraph = (VBox) futureTask.get();
//    Pane topArea = genTopArea();
//        this.setTop(topArea);
//        this.setLeft(currencyInfo);
//        this.setCenter(currencyGraph);
//    }
    public void refreshPane(Currency currency) throws ExecutionException, InterruptedException {
        this.currency = currency;

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Future<VBox> infoFuture = executor.submit(new InfoPaneTask(currency));
        Future<HBox> topAreaFuture = executor.submit(new TopAreaTask(watch, delete, unwatch));
        Future<VBox> graphFuture = executor.submit(new DrawGraphTask(currency));

        VBox infoPane = infoFuture.get();
        HBox topArea = topAreaFuture.get();
        VBox graphPane = graphFuture.get();


        executor.shutdown();

        javafx.application.Platform.runLater(() -> {
            this.setLeft(infoPane);
            this.setTop(topArea);
            this.setCenter(graphPane);
        });
    }
//        private Pane genInfoPane(){
//            VBox currencyInfoPane = new VBox (10);
//            currencyInfoPane.setPadding(new Insets(5, 25, 5, 25)); currencyInfoPane.setAlignment (Pos.CENTER) ;
//            Label exchangeString = new Label("");
//            Label watchString = new Label("");
//            exchangeString.setStyle("-fx-font-size: 20;"); watchString.setStyle("-fx-font-size: 14;");
//            if (this.currency != null) {
//                exchangeString.setText(String.format("%s: %.4f", this.currency.getShortCode(), this.currency.getCurrent().getRate()));
//                if(this.currency.getWatch() == true) {
//                    watchString.setText(String.format("(Watch @%.4f)",this.currency. getWatchRate())) ;
//                }
//            }
//            currencyInfoPane.getChildren().addAll(exchangeString,watchString) ;
//            return currencyInfoPane;
//        }
//        private HBox genTopArea() {
//            HBox topArea = new HBox (10) ;
//            topArea.setPadding(new Insets(5));
//            topArea.getChildren() .addAll(unwatch, watch, delete);
//            ((HBox) topArea).setAlignment(Pos.CENTER_RIGHT) ;
//            return topArea;
//        }


    private class InfoPaneTask implements Callable<VBox> {
        private Currency currency;

        public InfoPaneTask(Currency currency) {
            this.currency = currency;
        }

        @Override
        public VBox call() {
            VBox currencyInfoPane = new VBox(10);
            currencyInfoPane.setPadding(new Insets(5, 25, 5, 25));
            currencyInfoPane.setAlignment(Pos.CENTER);
            Label exchangeString = new Label("");
            Label watchString = new Label("");
            exchangeString.setStyle("-fx-font-size: 20;");
            watchString.setStyle("-fx-font-size: 14;");
            if (currency != null) {
                exchangeString.setText(String.format("%s: %.4f", currency.getShortCode(), currency.getCurrent().getRate()));
                if (currency.getWatch()) {
                    watchString.setText(String.format("(Watch @%.4f)", currency.getWatchRate()));
                }
            }
            currencyInfoPane.getChildren().addAll(exchangeString, watchString);
            return currencyInfoPane;
        }
    }
    private class TopAreaTask implements Callable<HBox> {
        private final Button watch;
        private final Button delete;
        private final Button unwatch;

        public TopAreaTask(Button watch, Button delete, Button unwatch) {
            this.watch = watch;
            this.delete = delete;
            this.unwatch = unwatch;
        }

        @Override
        public HBox call() {
            HBox topArea = new HBox(10);
            topArea.setPadding(new Insets(5));
            topArea.getChildren().addAll(unwatch, watch, delete);
            topArea.setAlignment(Pos.CENTER_RIGHT);
            return topArea;
        }
    }


}

