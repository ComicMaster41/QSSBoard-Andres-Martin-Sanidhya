/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package comp20050.qssboard;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Polygon;
import kotlin.NotImplementedError;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;



public class HelloController {

    // Functions from backend - logic for handling the player
    GameState state = new GameState();

    // colours of different players
    Color colorP1 = Color.WHITE;
    Color colorP2 = Color.BLACK;

    @FXML // fx:id="OctCell_r0_c0"
    private Polygon OctCell_turn;

    @FXML // fx:id="OctCell_r0_c0"
    private Polygon Rhombus_turn;

    @FXML
    private Label turnLabel;

    private boolean isWhiteTurn = true;  // White starts


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="BlackBorders"
    private Group BlackBorders; // Value injected by FXMLLoader

    @FXML // fx:id="BottomLetters"
    private Group BottomLetters; // Value injected by FXMLLoader

    @FXML // fx:id="LeftNumbers"
    private Group LeftNumbers; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r0_c0"
    private Polygon OctCell_r0_c0; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r0_c1"
    private Polygon OctCell_r0_c1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r0_c10"
    private Polygon OctCell_r0_c10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r0_c2"
    private Polygon OctCell_r0_c2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r0_c3"
    private Polygon OctCell_r0_c3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r0_c4"
    private Polygon OctCell_r0_c4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r0_c5"
    private Polygon OctCell_r0_c5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r0_c6"
    private Polygon OctCell_r0_c6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r0_c7"
    private Polygon OctCell_r0_c7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r0_c8"
    private Polygon OctCell_r0_c8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r0_c9"
    private Polygon OctCell_r0_c9; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r10_c0"
    private Polygon OctCell_r10_c0; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r10_c1"
    private Polygon OctCell_r10_c1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r10_c10"
    private Polygon OctCell_r10_c10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r10_c2"
    private Polygon OctCell_r10_c2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r10_c3"
    private Polygon OctCell_r10_c3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r10_c4"
    private Polygon OctCell_r10_c4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r10_c5"
    private Polygon OctCell_r10_c5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r10_c6"
    private Polygon OctCell_r10_c6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r10_c7"
    private Polygon OctCell_r10_c7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r10_c8"
    private Polygon OctCell_r10_c8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r10_c9"
    private Polygon OctCell_r10_c9; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r1_c0"
    private Polygon OctCell_r1_c0; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r1_c1"
    private Polygon OctCell_r1_c1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r1_c10"
    private Polygon OctCell_r1_c10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r1_c2"
    private Polygon OctCell_r1_c2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r1_c3"
    private Polygon OctCell_r1_c3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r1_c4"
    private Polygon OctCell_r1_c4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r1_c5"
    private Polygon OctCell_r1_c5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r1_c6"
    private Polygon OctCell_r1_c6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r1_c7"
    private Polygon OctCell_r1_c7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r1_c8"
    private Polygon OctCell_r1_c8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r1_c9"
    private Polygon OctCell_r1_c9; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r2_c0"
    private Polygon OctCell_r2_c0; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r2_c1"
    private Polygon OctCell_r2_c1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r2_c10"
    private Polygon OctCell_r2_c10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r2_c2"
    private Polygon OctCell_r2_c2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r2_c3"
    private Polygon OctCell_r2_c3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r2_c4"
    private Polygon OctCell_r2_c4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r2_c5"
    private Polygon OctCell_r2_c5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r2_c6"
    private Polygon OctCell_r2_c6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r2_c7"
    private Polygon OctCell_r2_c7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r2_c8"
    private Polygon OctCell_r2_c8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r2_c9"
    private Polygon OctCell_r2_c9; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r3_c0"
    private Polygon OctCell_r3_c0; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r3_c1"
    private Polygon OctCell_r3_c1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r3_c10"
    private Polygon OctCell_r3_c10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r3_c2"
    private Polygon OctCell_r3_c2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r3_c3"
    private Polygon OctCell_r3_c3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r3_c4"
    private Polygon OctCell_r3_c4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r3_c5"
    private Polygon OctCell_r3_c5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r3_c6"
    private Polygon OctCell_r3_c6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r3_c7"
    private Polygon OctCell_r3_c7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r3_c8"
    private Polygon OctCell_r3_c8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r3_c9"
    private Polygon OctCell_r3_c9; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r4_c0"
    private Polygon OctCell_r4_c0; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r4_c1"
    private Polygon OctCell_r4_c1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r4_c10"
    private Polygon OctCell_r4_c10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r4_c2"
    private Polygon OctCell_r4_c2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r4_c3"
    private Polygon OctCell_r4_c3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r4_c4"
    private Polygon OctCell_r4_c4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r4_c5"
    private Polygon OctCell_r4_c5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r4_c6"
    private Polygon OctCell_r4_c6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r4_c7"
    private Polygon OctCell_r4_c7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r4_c8"
    private Polygon OctCell_r4_c8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r4_c9"
    private Polygon OctCell_r4_c9; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r5_c0"
    private Polygon OctCell_r5_c0; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r5_c1"
    private Polygon OctCell_r5_c1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r5_c10"
    private Polygon OctCell_r5_c10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r5_c2"
    private Polygon OctCell_r5_c2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r5_c3"
    private Polygon OctCell_r5_c3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r5_c4"
    private Polygon OctCell_r5_c4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r5_c5"
    private Polygon OctCell_r5_c5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r5_c6"
    private Polygon OctCell_r5_c6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r5_c7"
    private Polygon OctCell_r5_c7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r5_c8"
    private Polygon OctCell_r5_c8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r5_c9"
    private Polygon OctCell_r5_c9; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r6_c0"
    private Polygon OctCell_r6_c0; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r6_c1"
    private Polygon OctCell_r6_c1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r6_c10"
    private Polygon OctCell_r6_c10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r6_c2"
    private Polygon OctCell_r6_c2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r6_c3"
    private Polygon OctCell_r6_c3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r6_c4"
    private Polygon OctCell_r6_c4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r6_c5"
    private Polygon OctCell_r6_c5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r6_c6"
    private Polygon OctCell_r6_c6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r6_c7"
    private Polygon OctCell_r6_c7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r6_c8"
    private Polygon OctCell_r6_c8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r6_c9"
    private Polygon OctCell_r6_c9; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r7_c0"
    private Polygon OctCell_r7_c0; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r7_c1"
    private Polygon OctCell_r7_c1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r7_c10"
    private Polygon OctCell_r7_c10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r7_c2"
    private Polygon OctCell_r7_c2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r7_c3"
    private Polygon OctCell_r7_c3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r7_c4"
    private Polygon OctCell_r7_c4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r7_c5"
    private Polygon OctCell_r7_c5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r7_c6"
    private Polygon OctCell_r7_c6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r7_c7"
    private Polygon OctCell_r7_c7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r7_c8"
    private Polygon OctCell_r7_c8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r7_c9"
    private Polygon OctCell_r7_c9; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r8_c0"
    private Polygon OctCell_r8_c0; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r8_c1"
    private Polygon OctCell_r8_c1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r8_c10"
    private Polygon OctCell_r8_c10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r8_c2"
    private Polygon OctCell_r8_c2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r8_c3"
    private Polygon OctCell_r8_c3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r8_c4"
    private Polygon OctCell_r8_c4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r8_c5"
    private Polygon OctCell_r8_c5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r8_c6"
    private Polygon OctCell_r8_c6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r8_c7"
    private Polygon OctCell_r8_c7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r8_c8"
    private Polygon OctCell_r8_c8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r8_c9"
    private Polygon OctCell_r8_c9; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r9_c0"
    private Polygon OctCell_r9_c0; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r9_c1"
    private Polygon OctCell_r9_c1; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r9_c10"
    private Polygon OctCell_r9_c10; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r9_c2"
    private Polygon OctCell_r9_c2; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r9_c3"
    private Polygon OctCell_r9_c3; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r9_c4"
    private Polygon OctCell_r9_c4; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r9_c5"
    private Polygon OctCell_r9_c5; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r9_c6"
    private Polygon OctCell_r9_c6; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r9_c7"
    private Polygon OctCell_r9_c7; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r9_c8"
    private Polygon OctCell_r9_c8; // Value injected by FXMLLoader

    @FXML // fx:id="OctCell_r9_c9"
    private Polygon OctCell_r9_c9; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r0_c0"
    private Polygon Rhombus_r0_c0; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r0_c1"
    private Polygon Rhombus_r0_c1; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r0_c2"
    private Polygon Rhombus_r0_c2; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r0_c3"
    private Polygon Rhombus_r0_c3; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r0_c4"
    private Polygon Rhombus_r0_c4; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r0_c5"
    private Polygon Rhombus_r0_c5; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r0_c6"
    private Polygon Rhombus_r0_c6; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r0_c7"
    private Polygon Rhombus_r0_c7; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r0_c8"
    private Polygon Rhombus_r0_c8; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r0_c9"
    private Polygon Rhombus_r0_c9; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r1_c0"
    private Polygon Rhombus_r1_c0; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r1_c1"
    private Polygon Rhombus_r1_c1; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r1_c2"
    private Polygon Rhombus_r1_c2; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r1_c3"
    private Polygon Rhombus_r1_c3; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r1_c4"
    private Polygon Rhombus_r1_c4; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r1_c5"
    private Polygon Rhombus_r1_c5; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r1_c6"
    private Polygon Rhombus_r1_c6; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r1_c7"
    private Polygon Rhombus_r1_c7; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r1_c8"
    private Polygon Rhombus_r1_c8; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r1_c9"
    private Polygon Rhombus_r1_c9; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r2_c0"
    private Polygon Rhombus_r2_c0; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r2_c1"
    private Polygon Rhombus_r2_c1; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r2_c2"
    private Polygon Rhombus_r2_c2; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r2_c3"
    private Polygon Rhombus_r2_c3; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r2_c4"
    private Polygon Rhombus_r2_c4; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r2_c5"
    private Polygon Rhombus_r2_c5; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r2_c6"
    private Polygon Rhombus_r2_c6; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r2_c7"
    private Polygon Rhombus_r2_c7; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r2_c8"
    private Polygon Rhombus_r2_c8; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r2_c9"
    private Polygon Rhombus_r2_c9; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r3_c0"
    private Polygon Rhombus_r3_c0; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r3_c1"
    private Polygon Rhombus_r3_c1; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r3_c2"
    private Polygon Rhombus_r3_c2; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r3_c3"
    private Polygon Rhombus_r3_c3; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r3_c4"
    private Polygon Rhombus_r3_c4; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r3_c5"
    private Polygon Rhombus_r3_c5; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r3_c6"
    private Polygon Rhombus_r3_c6; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r3_c7"
    private Polygon Rhombus_r3_c7; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r3_c8"
    private Polygon Rhombus_r3_c8; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r3_c9"
    private Polygon Rhombus_r3_c9; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r4_c0"
    private Polygon Rhombus_r4_c0; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r4_c1"
    private Polygon Rhombus_r4_c1; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r4_c2"
    private Polygon Rhombus_r4_c2; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r4_c3"
    private Polygon Rhombus_r4_c3; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r4_c4"
    private Polygon Rhombus_r4_c4; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r4_c5"
    private Polygon Rhombus_r4_c5; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r4_c6"
    private Polygon Rhombus_r4_c6; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r4_c7"
    private Polygon Rhombus_r4_c7; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r4_c8"
    private Polygon Rhombus_r4_c8; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r4_c9"
    private Polygon Rhombus_r4_c9; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r5_c0"
    private Polygon Rhombus_r5_c0; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r5_c1"
    private Polygon Rhombus_r5_c1; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r5_c2"
    private Polygon Rhombus_r5_c2; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r5_c3"
    private Polygon Rhombus_r5_c3; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r5_c4"
    private Polygon Rhombus_r5_c4; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r5_c5"
    private Polygon Rhombus_r5_c5; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r5_c6"
    private Polygon Rhombus_r5_c6; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r5_c7"
    private Polygon Rhombus_r5_c7; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r5_c8"
    private Polygon Rhombus_r5_c8; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r5_c9"
    private Polygon Rhombus_r5_c9; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r6_c0"
    private Polygon Rhombus_r6_c0; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r6_c1"
    private Polygon Rhombus_r6_c1; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r6_c2"
    private Polygon Rhombus_r6_c2; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r6_c3"
    private Polygon Rhombus_r6_c3; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r6_c4"
    private Polygon Rhombus_r6_c4; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r6_c5"
    private Polygon Rhombus_r6_c5; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r6_c6"
    private Polygon Rhombus_r6_c6; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r6_c7"
    private Polygon Rhombus_r6_c7; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r6_c8"
    private Polygon Rhombus_r6_c8; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r6_c9"
    private Polygon Rhombus_r6_c9; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r7_c0"
    private Polygon Rhombus_r7_c0; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r7_c1"
    private Polygon Rhombus_r7_c1; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r7_c2"
    private Polygon Rhombus_r7_c2; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r7_c3"
    private Polygon Rhombus_r7_c3; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r7_c4"
    private Polygon Rhombus_r7_c4; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r7_c5"
    private Polygon Rhombus_r7_c5; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r7_c6"
    private Polygon Rhombus_r7_c6; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r7_c7"
    private Polygon Rhombus_r7_c7; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r7_c8"
    private Polygon Rhombus_r7_c8; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r7_c9"
    private Polygon Rhombus_r7_c9; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r8_c0"
    private Polygon Rhombus_r8_c0; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r8_c1"
    private Polygon Rhombus_r8_c1; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r8_c2"
    private Polygon Rhombus_r8_c2; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r8_c3"
    private Polygon Rhombus_r8_c3; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r8_c4"
    private Polygon Rhombus_r8_c4; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r8_c5"
    private Polygon Rhombus_r8_c5; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r8_c6"
    private Polygon Rhombus_r8_c6; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r8_c7"
    private Polygon Rhombus_r8_c7; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r8_c8"
    private Polygon Rhombus_r8_c8; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r8_c9"
    private Polygon Rhombus_r8_c9; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r9_c0"
    private Polygon Rhombus_r9_c0; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r9_c1"
    private Polygon Rhombus_r9_c1; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r9_c2"
    private Polygon Rhombus_r9_c2; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r9_c3"
    private Polygon Rhombus_r9_c3; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r9_c4"
    private Polygon Rhombus_r9_c4; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r9_c5"
    private Polygon Rhombus_r9_c5; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r9_c6"
    private Polygon Rhombus_r9_c6; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r9_c7"
    private Polygon Rhombus_r9_c7; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r9_c8"
    private Polygon Rhombus_r9_c8; // Value injected by FXMLLoader

    @FXML // fx:id="Rhombus_r9_c9"
    private Polygon Rhombus_r9_c9; // Value injected by FXMLLoader

    @FXML // fx:id="RightNumbers"
    private Group RightNumbers; // Value injected by FXMLLoader

    @FXML // fx:id="ShapeLayout"
    private Group ShapeLayout; // Value injected by FXMLLoader

    @FXML // fx:id="Title"
    private Group Title; // Value injected by FXMLLoader

    @FXML // fx:id="TopLetters"
    private Group TopLetters; // Value injected by FXMLLoader

    @FXML // fx:id="boardWrapper"
    private Group boardWrapper; // Value injected by FXMLLoader

    @FXML // fx:id="root"
    private StackPane root; // Value injected by FXMLLoader

    @FXML
    void getCellID(MouseEvent event) {
        updateTurnDisplay();

        Polygon cell = (Polygon) event.getSource();

        // get row and col
        Position pos = new Position(cell.getId());
        QuaxBoard.TileType tile_type;

        // get tile type:
        if (cell.getId().charAt(0) == 'R'){
            tile_type = QuaxBoard.TileType.RHOMBUS;
        }
        else {
            tile_type = QuaxBoard.TileType.OCTAGON;
        }

        // make the move:
        GameState.Player playerBeforeMove = state.getCurrentPlayer();
        boolean success = state.makeMove(pos, tile_type);
        if (!success) {
            System.out.println("print");
            // add some text here on teh screen
            // TO-DO
            throw new NotImplementedError();
        }

        if (playerBeforeMove == GameState.Player.P1) {
            cell.setFill(colorP1);
        }
        else if (playerBeforeMove == GameState.Player.P2) {
            cell.setFill(colorP2);
        }
        else { // player must be either P1 or P2. Otherwise, we have an error
            throw new IllegalArgumentException("Error from Controller in getCellID - NO player option given");
        }

        isWhiteTurn = !isWhiteTurn;
    }

    @FXML
    void getHexID(MouseEvent event) {
        getCellID(event);
    }

    private void updateTurnDisplay() {
        if (isWhiteTurn) {
            OctCell_turn.setFill(colorP1);
            Rhombus_turn.setFill(colorP1);
            turnLabel.setText("White to play");
        } else {
            OctCell_turn.setFill(colorP2);
            Rhombus_turn.setFill(colorP2);
            turnLabel.setText("Black to play");
        }
    }
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        // just to give initla white colour
        OctCell_turn.setFill(colorP1);
        Rhombus_turn.setFill(colorP1);
        isWhiteTurn = !isWhiteTurn;
        assert BlackBorders != null : "fx:id=\"BlackBorders\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert BottomLetters != null : "fx:id=\"BottomLetters\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert LeftNumbers != null : "fx:id=\"LeftNumbers\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r0_c0 != null : "fx:id=\"OctCell_r0_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r0_c1 != null : "fx:id=\"OctCell_r0_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r0_c10 != null : "fx:id=\"OctCell_r0_c10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r0_c2 != null : "fx:id=\"OctCell_r0_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r0_c3 != null : "fx:id=\"OctCell_r0_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r0_c4 != null : "fx:id=\"OctCell_r0_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r0_c5 != null : "fx:id=\"OctCell_r0_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r0_c6 != null : "fx:id=\"OctCell_r0_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r0_c7 != null : "fx:id=\"OctCell_r0_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r0_c8 != null : "fx:id=\"OctCell_r0_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r0_c9 != null : "fx:id=\"OctCell_r0_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r10_c0 != null : "fx:id=\"OctCell_r10_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r10_c1 != null : "fx:id=\"OctCell_r10_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r10_c10 != null : "fx:id=\"OctCell_r10_c10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r10_c2 != null : "fx:id=\"OctCell_r10_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r10_c3 != null : "fx:id=\"OctCell_r10_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r10_c4 != null : "fx:id=\"OctCell_r10_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r10_c5 != null : "fx:id=\"OctCell_r10_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r10_c6 != null : "fx:id=\"OctCell_r10_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r10_c7 != null : "fx:id=\"OctCell_r10_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r10_c8 != null : "fx:id=\"OctCell_r10_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r10_c9 != null : "fx:id=\"OctCell_r10_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r1_c0 != null : "fx:id=\"OctCell_r1_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r1_c1 != null : "fx:id=\"OctCell_r1_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r1_c10 != null : "fx:id=\"OctCell_r1_c10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r1_c2 != null : "fx:id=\"OctCell_r1_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r1_c3 != null : "fx:id=\"OctCell_r1_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r1_c4 != null : "fx:id=\"OctCell_r1_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r1_c5 != null : "fx:id=\"OctCell_r1_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r1_c6 != null : "fx:id=\"OctCell_r1_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r1_c7 != null : "fx:id=\"OctCell_r1_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r1_c8 != null : "fx:id=\"OctCell_r1_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r1_c9 != null : "fx:id=\"OctCell_r1_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r2_c0 != null : "fx:id=\"OctCell_r2_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r2_c1 != null : "fx:id=\"OctCell_r2_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r2_c10 != null : "fx:id=\"OctCell_r2_c10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r2_c2 != null : "fx:id=\"OctCell_r2_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r2_c3 != null : "fx:id=\"OctCell_r2_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r2_c4 != null : "fx:id=\"OctCell_r2_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r2_c5 != null : "fx:id=\"OctCell_r2_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r2_c6 != null : "fx:id=\"OctCell_r2_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r2_c7 != null : "fx:id=\"OctCell_r2_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r2_c8 != null : "fx:id=\"OctCell_r2_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r2_c9 != null : "fx:id=\"OctCell_r2_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r3_c0 != null : "fx:id=\"OctCell_r3_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r3_c1 != null : "fx:id=\"OctCell_r3_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r3_c10 != null : "fx:id=\"OctCell_r3_c10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r3_c2 != null : "fx:id=\"OctCell_r3_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r3_c3 != null : "fx:id=\"OctCell_r3_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r3_c4 != null : "fx:id=\"OctCell_r3_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r3_c5 != null : "fx:id=\"OctCell_r3_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r3_c6 != null : "fx:id=\"OctCell_r3_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r3_c7 != null : "fx:id=\"OctCell_r3_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r3_c8 != null : "fx:id=\"OctCell_r3_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r3_c9 != null : "fx:id=\"OctCell_r3_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r4_c0 != null : "fx:id=\"OctCell_r4_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r4_c1 != null : "fx:id=\"OctCell_r4_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r4_c10 != null : "fx:id=\"OctCell_r4_c10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r4_c2 != null : "fx:id=\"OctCell_r4_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r4_c3 != null : "fx:id=\"OctCell_r4_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r4_c4 != null : "fx:id=\"OctCell_r4_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r4_c5 != null : "fx:id=\"OctCell_r4_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r4_c6 != null : "fx:id=\"OctCell_r4_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r4_c7 != null : "fx:id=\"OctCell_r4_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r4_c8 != null : "fx:id=\"OctCell_r4_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r4_c9 != null : "fx:id=\"OctCell_r4_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r5_c0 != null : "fx:id=\"OctCell_r5_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r5_c1 != null : "fx:id=\"OctCell_r5_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r5_c10 != null : "fx:id=\"OctCell_r5_c10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r5_c2 != null : "fx:id=\"OctCell_r5_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r5_c3 != null : "fx:id=\"OctCell_r5_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r5_c4 != null : "fx:id=\"OctCell_r5_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r5_c5 != null : "fx:id=\"OctCell_r5_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r5_c6 != null : "fx:id=\"OctCell_r5_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r5_c7 != null : "fx:id=\"OctCell_r5_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r5_c8 != null : "fx:id=\"OctCell_r5_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r5_c9 != null : "fx:id=\"OctCell_r5_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r6_c0 != null : "fx:id=\"OctCell_r6_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r6_c1 != null : "fx:id=\"OctCell_r6_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r6_c10 != null : "fx:id=\"OctCell_r6_c10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r6_c2 != null : "fx:id=\"OctCell_r6_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r6_c3 != null : "fx:id=\"OctCell_r6_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r6_c4 != null : "fx:id=\"OctCell_r6_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r6_c5 != null : "fx:id=\"OctCell_r6_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r6_c6 != null : "fx:id=\"OctCell_r6_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r6_c7 != null : "fx:id=\"OctCell_r6_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r6_c8 != null : "fx:id=\"OctCell_r6_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r6_c9 != null : "fx:id=\"OctCell_r6_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r7_c0 != null : "fx:id=\"OctCell_r7_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r7_c1 != null : "fx:id=\"OctCell_r7_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r7_c10 != null : "fx:id=\"OctCell_r7_c10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r7_c2 != null : "fx:id=\"OctCell_r7_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r7_c3 != null : "fx:id=\"OctCell_r7_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r7_c4 != null : "fx:id=\"OctCell_r7_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r7_c5 != null : "fx:id=\"OctCell_r7_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r7_c6 != null : "fx:id=\"OctCell_r7_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r7_c7 != null : "fx:id=\"OctCell_r7_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r7_c8 != null : "fx:id=\"OctCell_r7_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r7_c9 != null : "fx:id=\"OctCell_r7_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r8_c0 != null : "fx:id=\"OctCell_r8_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r8_c1 != null : "fx:id=\"OctCell_r8_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r8_c10 != null : "fx:id=\"OctCell_r8_c10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r8_c2 != null : "fx:id=\"OctCell_r8_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r8_c3 != null : "fx:id=\"OctCell_r8_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r8_c4 != null : "fx:id=\"OctCell_r8_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r8_c5 != null : "fx:id=\"OctCell_r8_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r8_c6 != null : "fx:id=\"OctCell_r8_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r8_c7 != null : "fx:id=\"OctCell_r8_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r8_c8 != null : "fx:id=\"OctCell_r8_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r8_c9 != null : "fx:id=\"OctCell_r8_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r9_c0 != null : "fx:id=\"OctCell_r9_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r9_c1 != null : "fx:id=\"OctCell_r9_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r9_c10 != null : "fx:id=\"OctCell_r9_c10\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r9_c2 != null : "fx:id=\"OctCell_r9_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r9_c3 != null : "fx:id=\"OctCell_r9_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r9_c4 != null : "fx:id=\"OctCell_r9_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r9_c5 != null : "fx:id=\"OctCell_r9_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r9_c6 != null : "fx:id=\"OctCell_r9_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r9_c7 != null : "fx:id=\"OctCell_r9_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r9_c8 != null : "fx:id=\"OctCell_r9_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert OctCell_r9_c9 != null : "fx:id=\"OctCell_r9_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r0_c0 != null : "fx:id=\"Rhombus_r0_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r0_c1 != null : "fx:id=\"Rhombus_r0_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r0_c2 != null : "fx:id=\"Rhombus_r0_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r0_c3 != null : "fx:id=\"Rhombus_r0_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r0_c4 != null : "fx:id=\"Rhombus_r0_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r0_c5 != null : "fx:id=\"Rhombus_r0_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r0_c6 != null : "fx:id=\"Rhombus_r0_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r0_c7 != null : "fx:id=\"Rhombus_r0_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r0_c8 != null : "fx:id=\"Rhombus_r0_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r0_c9 != null : "fx:id=\"Rhombus_r0_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r1_c0 != null : "fx:id=\"Rhombus_r1_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r1_c1 != null : "fx:id=\"Rhombus_r1_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r1_c2 != null : "fx:id=\"Rhombus_r1_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r1_c3 != null : "fx:id=\"Rhombus_r1_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r1_c4 != null : "fx:id=\"Rhombus_r1_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r1_c5 != null : "fx:id=\"Rhombus_r1_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r1_c6 != null : "fx:id=\"Rhombus_r1_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r1_c7 != null : "fx:id=\"Rhombus_r1_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r1_c8 != null : "fx:id=\"Rhombus_r1_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r1_c9 != null : "fx:id=\"Rhombus_r1_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r2_c0 != null : "fx:id=\"Rhombus_r2_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r2_c1 != null : "fx:id=\"Rhombus_r2_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r2_c2 != null : "fx:id=\"Rhombus_r2_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r2_c3 != null : "fx:id=\"Rhombus_r2_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r2_c4 != null : "fx:id=\"Rhombus_r2_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r2_c5 != null : "fx:id=\"Rhombus_r2_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r2_c6 != null : "fx:id=\"Rhombus_r2_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r2_c7 != null : "fx:id=\"Rhombus_r2_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r2_c8 != null : "fx:id=\"Rhombus_r2_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r2_c9 != null : "fx:id=\"Rhombus_r2_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r3_c0 != null : "fx:id=\"Rhombus_r3_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r3_c1 != null : "fx:id=\"Rhombus_r3_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r3_c2 != null : "fx:id=\"Rhombus_r3_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r3_c3 != null : "fx:id=\"Rhombus_r3_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r3_c4 != null : "fx:id=\"Rhombus_r3_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r3_c5 != null : "fx:id=\"Rhombus_r3_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r3_c6 != null : "fx:id=\"Rhombus_r3_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r3_c7 != null : "fx:id=\"Rhombus_r3_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r3_c8 != null : "fx:id=\"Rhombus_r3_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r3_c9 != null : "fx:id=\"Rhombus_r3_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r4_c0 != null : "fx:id=\"Rhombus_r4_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r4_c1 != null : "fx:id=\"Rhombus_r4_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r4_c2 != null : "fx:id=\"Rhombus_r4_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r4_c3 != null : "fx:id=\"Rhombus_r4_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r4_c4 != null : "fx:id=\"Rhombus_r4_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r4_c5 != null : "fx:id=\"Rhombus_r4_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r4_c6 != null : "fx:id=\"Rhombus_r4_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r4_c7 != null : "fx:id=\"Rhombus_r4_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r4_c8 != null : "fx:id=\"Rhombus_r4_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r4_c9 != null : "fx:id=\"Rhombus_r4_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r5_c0 != null : "fx:id=\"Rhombus_r5_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r5_c1 != null : "fx:id=\"Rhombus_r5_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r5_c2 != null : "fx:id=\"Rhombus_r5_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r5_c3 != null : "fx:id=\"Rhombus_r5_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r5_c4 != null : "fx:id=\"Rhombus_r5_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r5_c5 != null : "fx:id=\"Rhombus_r5_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r5_c6 != null : "fx:id=\"Rhombus_r5_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r5_c7 != null : "fx:id=\"Rhombus_r5_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r5_c8 != null : "fx:id=\"Rhombus_r5_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r5_c9 != null : "fx:id=\"Rhombus_r5_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r6_c0 != null : "fx:id=\"Rhombus_r6_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r6_c1 != null : "fx:id=\"Rhombus_r6_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r6_c2 != null : "fx:id=\"Rhombus_r6_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r6_c3 != null : "fx:id=\"Rhombus_r6_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r6_c4 != null : "fx:id=\"Rhombus_r6_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r6_c5 != null : "fx:id=\"Rhombus_r6_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r6_c6 != null : "fx:id=\"Rhombus_r6_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r6_c7 != null : "fx:id=\"Rhombus_r6_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r6_c8 != null : "fx:id=\"Rhombus_r6_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r6_c9 != null : "fx:id=\"Rhombus_r6_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r7_c0 != null : "fx:id=\"Rhombus_r7_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r7_c1 != null : "fx:id=\"Rhombus_r7_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r7_c2 != null : "fx:id=\"Rhombus_r7_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r7_c3 != null : "fx:id=\"Rhombus_r7_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r7_c4 != null : "fx:id=\"Rhombus_r7_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r7_c5 != null : "fx:id=\"Rhombus_r7_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r7_c6 != null : "fx:id=\"Rhombus_r7_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r7_c7 != null : "fx:id=\"Rhombus_r7_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r7_c8 != null : "fx:id=\"Rhombus_r7_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r7_c9 != null : "fx:id=\"Rhombus_r7_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r8_c0 != null : "fx:id=\"Rhombus_r8_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r8_c1 != null : "fx:id=\"Rhombus_r8_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r8_c2 != null : "fx:id=\"Rhombus_r8_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r8_c3 != null : "fx:id=\"Rhombus_r8_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r8_c4 != null : "fx:id=\"Rhombus_r8_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r8_c5 != null : "fx:id=\"Rhombus_r8_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r8_c6 != null : "fx:id=\"Rhombus_r8_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r8_c7 != null : "fx:id=\"Rhombus_r8_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r8_c8 != null : "fx:id=\"Rhombus_r8_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r8_c9 != null : "fx:id=\"Rhombus_r8_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r9_c0 != null : "fx:id=\"Rhombus_r9_c0\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r9_c1 != null : "fx:id=\"Rhombus_r9_c1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r9_c2 != null : "fx:id=\"Rhombus_r9_c2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r9_c3 != null : "fx:id=\"Rhombus_r9_c3\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r9_c4 != null : "fx:id=\"Rhombus_r9_c4\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r9_c5 != null : "fx:id=\"Rhombus_r9_c5\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r9_c6 != null : "fx:id=\"Rhombus_r9_c6\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r9_c7 != null : "fx:id=\"Rhombus_r9_c7\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r9_c8 != null : "fx:id=\"Rhombus_r9_c8\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Rhombus_r9_c9 != null : "fx:id=\"Rhombus_r9_c9\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert RightNumbers != null : "fx:id=\"RightNumbers\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert ShapeLayout != null : "fx:id=\"ShapeLayout\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert Title != null : "fx:id=\"Title\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert TopLetters != null : "fx:id=\"TopLetters\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert boardWrapper != null : "fx:id=\"boardWrapper\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert root != null : "fx:id=\"root\" was not injected: check your FXML file 'hello-view.fxml'.";

    }

}
