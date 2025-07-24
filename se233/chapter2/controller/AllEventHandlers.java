package se233.chapter2.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import se233.chapter2.Launcher;
import se233.chapter2.model.Currency;
import se233.chapter2.model.CurrencyEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AllEventHandlers {
    public static void onRefresh() {
        try {
            Launcher.refreshPane();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void onAdd() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Currency"); dialog.setContentText("Currency code:");
            dialog. setHeaderText (null); dialog. setGraphic(null);
            Optional<String> code = dialog.showAndWait();
            if (code.isPresent()){
                List<Currency> currencyList = Launcher.getCurrencyList();
                Currency c = new Currency(code.get().toUpperCase());
                List<CurrencyEntity> cList = FetchData.fetchRange(c.getShortCode(), 30);
                c.setHistorical(cList);
                c.setCurrent(cList.get(cList.size() - 1));
                currencyList.add(c);
                Launcher.setCurrencyList(currencyList);
                Launcher.refreshPane();
            }
        } catch (InterruptedException e) {
            e. printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        } catch(Exception e){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("The currency entered is invalid.");
            error.setHeaderText(null);
            error.showAndWait();
        }
    }
    public static void onChange(){
        try{
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Change Base Currency"); dialog.setContentText("Base Currency code:");
            dialog. setHeaderText (null); dialog. setGraphic(null);
            Optional<String> code = dialog.showAndWait();
            if(code.isPresent()){
                Launcher.setBaseCurrency(code.get().toUpperCase());
                List<Currency> currencyList = Launcher.getCurrencyList();
                for (Currency currency : currencyList) {
                    List<CurrencyEntity> cList = FetchData.fetchRange(currency.getShortCode(), 30);
                    currency.setHistorical(cList);
                    currency.setCurrent(cList.get(cList.size() - 1));
                }
                Launcher.refreshPane();
            }
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setContentText("The currency entered is invalid.");
            error.setHeaderText(null);
            error.showAndWait();
        }
    }
    public static void onDelete(String code) {
        try {
            List<Currency> currencyList = Launcher.getCurrencyList();
            int index = -1;
            for(int i=0 ; i<currencyList.size() ; i++) {
                if (currencyList.get(i).getShortCode().equals(code) ) {
                    index = i;
                    break; }
            }
            if (index != -1) {
                currencyList.remove(index);
                Launcher.setCurrencyList(currencyList);
                Launcher.refreshPane();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void onWatch(String code) {
        try {
            List<Currency> currencyList = Launcher.getCurrencyList();
            int index = -1;
            for(int i = 0 ; i < currencyList.size() ; i++) {
                if (currencyList.get(i).getShortCode().equals(code) ) {
                    index = i;
                    break; }
            }
            if (index != -1) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Add Watch");
                dialog.setContentText("Rate:");
                dialog.setHeaderText(null);
                dialog.setGraphic(null);
                Optional<String> retrievedRate = dialog.showAndWait();
                if (retrievedRate.isPresent()){
                    double rate = Double.parseDouble(retrievedRate.get());
                    currencyList.get(index).setWatch(true);
                    currencyList.get(index).setWatchRate(rate);
                    Launcher.setCurrencyList(currencyList);
                    Launcher.refreshPane();
                }
                Launcher.setCurrencyList(currencyList);
                Launcher.refreshPane();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void onUnwatch(String code){
        try{
            List<Currency> currencyList = Launcher.getCurrencyList();
            for (Currency currency : currencyList) {
                if (currency.getShortCode().equals(code)) {
                    currency.setWatch(false);
                    currency.setWatchRate(0.0);
                    break;
                }
            }
            Launcher.setCurrencyList(currencyList);
            Launcher.refreshPane();
        }catch(InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }
}