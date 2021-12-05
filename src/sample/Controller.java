package sample;

import games.EcologicalSimulation2;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ImageView imgView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AbstractGame game = new EcologicalSimulation2();
        GameContainer gc = new GameContainer(game,
                (int)imgView.getFitWidth(),
                (int)imgView.getFitHeight(),
                imgView);
        imgView.setImage(gc.getImg());
        gc.getTimer().setShowFPSOnConsole(true);
        gc.getTimer().start();
    }

}
