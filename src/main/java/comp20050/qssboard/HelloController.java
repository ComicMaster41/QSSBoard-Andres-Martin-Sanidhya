/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package comp20050.qssboard;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;

import static javafx.scene.paint.Color.BLACK;

public class HelloController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="OctCell1"
    private Polygon OctCell1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell10"
    private Polygon OctCell10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell11"
    private Polygon OctCell11; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell12"
    private Polygon OctCell12; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell13"
    private Polygon OctCell13; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell14"
    private Polygon OctCell14; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell15"
    private Polygon OctCell15; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell2"
    private Polygon OctCell2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell3"
    private Polygon OctCell3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell4"
    private Polygon OctCell4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell42"
    private Polygon OctCell42; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell5"
    private Polygon OctCell5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell6"
    private Polygon OctCell6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell7"
    private Polygon OctCell7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell8"
    private Polygon OctCell8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell9"
    private Polygon OctCell9; // Value injected by FXMLLoader

    @FXML // fx:id="hex1"
    private Polygon hex1; // Value injected by FXMLLoader

    @FXML // fx:id="hex2"
    private Polygon hex2; // Value injected by FXMLLoader

    @FXML // fx:id="hex3"
    private Polygon hex3; // Value injected by FXMLLoader

    @FXML // fx:id="hex5"
    private Polygon hex5; // Value injected by FXMLLoader

    @FXML // fx:id="hex6"
    private Polygon hex6; // Value injected by FXMLLoader

    @FXML // fx:id="hex7"
    private Polygon hex7; // Value injected by FXMLLoader

    @FXML // fx:id="hex8"
    private Polygon hex8; // Value injected by FXMLLoader

    @FXML // fx:id="hex9"
    private Polygon hex9; // Value injected by FXMLLoader

    @FXML
    void getCellID(MouseEvent event) {
        Polygon cell = (Polygon) event.getSource();
        cell.setFill(BLACK);
    }

    @FXML
    void getHexID(MouseEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert OctCell1 != null : "fx:id=\"OctCell1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell10 != null : "fx:id=\"OctCell10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell11 != null : "fx:id=\"OctCell11\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell12 != null : "fx:id=\"OctCell12\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell13 != null : "fx:id=\"OctCell13\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell14 != null : "fx:id=\"OctCell14\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell15 != null : "fx:id=\"OctCell15\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell2 != null : "fx:id=\"OctCell2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell3 != null : "fx:id=\"OctCell3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell4 != null : "fx:id=\"OctCell4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell42 != null : "fx:id=\"OctCell42\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell5 != null : "fx:id=\"OctCell5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell6 != null : "fx:id=\"OctCell6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell7 != null : "fx:id=\"OctCell7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell8 != null : "fx:id=\"OctCell8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell9 != null : "fx:id=\"OctCell9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex1 != null : "fx:id=\"hex1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex2 != null : "fx:id=\"hex2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex3 != null : "fx:id=\"hex3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex5 != null : "fx:id=\"hex5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex6 != null : "fx:id=\"hex6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex7 != null : "fx:id=\"hex7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex8 != null : "fx:id=\"hex8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert hex9 != null : "fx:id=\"hex9\" was not injected: check your FXML file 'hello-view.fxml'.";

    }

}
