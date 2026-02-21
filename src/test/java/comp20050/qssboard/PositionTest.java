package comp20050.qssboard;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class PositionTest {

    @Nested
    @DisplayName("Tests for extractPosition()")
    class ExtractPositionTests {

        @Test
        @DisplayName("Extract from OctCell string")
        void extractOctCell() {
            Position pos = new Position("OctCell_r3_c4");
            ArrayList<Integer> result = pos.extractPosition();
            assertEquals(2, result.size());
            assertEquals(3, result.get(0));
            assertEquals(4, result.get(1));

            // getters
            assertEquals(3, pos.getRow());
            assertEquals(4, pos.getCol());
            assertEquals(result, pos.getPosition());
        }

        @Test
        @DisplayName("Extract from Rhombus string")
        void extractRhombus() {
            Position pos = new Position("Rhombus_r1_c2");
            ArrayList<Integer> result = pos.extractPosition();
            assertEquals(2, result.size());
            assertEquals(1, result.get(0));
            assertEquals(2, result.get(1));

            // getters
            assertEquals(1, pos.getRow());
            assertEquals(2, pos.getCol());
        }

        @Test
        @DisplayName("Invalid format throws exception")
        void invalidFormat() {
            Position pos = new Position("Random_r1_c2");
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                pos.extractPosition();
            });
        }

        @Test
        @DisplayName("Extract multiple times appends to position list")
        void multipleExtracts() {
            Position pos = new Position("OctCell_r2_c3");
            pos.extractPosition();
            ArrayList<Integer> result2 = pos.extractPosition();
            // Original implementation keeps adding to the same ArrayList
            assertEquals(4, result2.size()); // 2 elements added again
        }
    }

    @Nested
    @DisplayName("Tests for getters")
    class GetterTests {

        @Test
        @DisplayName("getPosition returns correct ArrayList after extract")
        void getPositionAfterExtract() {
            Position pos = new Position("Rhombus_r4_c5");
            pos.extractPosition();
            ArrayList<Integer> list = pos.getPosition();
            assertEquals(4, list.get(0));
            assertEquals(5, list.get(1));
        }
    }
}