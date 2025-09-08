package com.projeto.gestao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.projeto.gestao.util.HibernateUtil;
import com.projeto.gestao.util.DataInitializer;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DataInitializer dataInitializer = new DataInitializer();
        dataInitializer.inicializarDados();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        primaryStage.setTitle("Sistema de Gestão de Projetos");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() {
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
