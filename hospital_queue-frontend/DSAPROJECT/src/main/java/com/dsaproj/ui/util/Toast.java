package com.dsaproj.ui.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class Toast {

    public static void show(Window owner, String message, int seconds) {
        Popup popup = new Popup();

        Label label = new Label(message);
        label.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 8;");
        label.setMinWidth(200);
        label.setAlignment(Pos.CENTER);

        StackPane pane = new StackPane(label);
        pane.setStyle("-fx-background-color: transparent;");

        popup.getContent().add(pane);
        popup.setAutoHide(true);

        // Show popup relative to owner window
        popup.show(owner);

        // Auto close after given seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(seconds), e -> popup.hide()));
        timeline.play();
    }
}