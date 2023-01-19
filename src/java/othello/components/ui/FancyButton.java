package othello.components.ui;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class FancyButton extends Button {
    public FancyButton(String text, Color color) {
        super(text);

        String colorString = String.format("rgba(%f, %f, %f, %f)", color.getRed() * 255, color.getGreen() * 255, color.getBlue() * 255, color.getOpacity());
        String hoverColorString = String.format("rgba(%f, %f, %f, %f)", color.getRed() * 255 + 20, color.getGreen() * 255 + 20, color.getBlue() * 255 + 20, color.getOpacity() - 0.2);

        // Determine if the color is dark or light
        double luminance = 0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue();
        String textColor = luminance > 0.4 ? "#000000" : "#ffffff";
        String styleString = "-fx-background-color: "+ colorString +"; -fx-border-color: transparent;-fx-font-size: 14px; -fx-text-fill:" + textColor + ";";

        this.setStyle(styleString);

        this.setOnMouseEntered(e -> {
            this.setStyle(styleString + "-fx-background-color: " + hoverColorString + ";" + "-fx-border-color:" + textColor +" ;");
        });

        this.setOnMouseExited(e -> {
            this.setStyle(styleString);
        });
    }
}
