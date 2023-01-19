package othello.components.ui;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class FancyButton extends Button {
    public FancyButton(String text, Color color) {
        super(text);

        String colorString = String.format("rgb(%f, %f, %f, %f)", color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
        String hoverColorString = String.format("rgb(%f, %f, %f, %f)", color.getRed() + 20, color.getGreen() + 20, color.getBlue() + 20, color.getOpacity() - 0.2);

        // Determine if the color is dark or light
        double luminance = 0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue();
        String textColor = luminance > 0.4 ? "#000000" : "#ffffff";
        String styleString = "-fx-background-color: "+ colorString +"; -fx-border-color: transparent;-fx-font-size: 14px; -fx-text-fill:" + textColor + ";";


        System.out.println(text + " (" + color + ")");
        System.out.println(colorString);
        System.out.println("luminance = " + luminance);
        System.out.println(textColor);
        System.out.println(styleString);


        this.setStyle(styleString);

        this.setOnMouseEntered(e -> {
            this.setStyle(styleString + "-fx-background-color: " + hoverColorString + ";" + "-fx-border-color:" + textColor +" ;");
        });

        this.setOnMouseExited(e -> {
            this.setStyle(styleString);
        });
    }
}
