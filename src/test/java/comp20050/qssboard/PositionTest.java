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
            Position pos = new Position("O_3_4");
            ArrayList<Integer> result = pos.extractPosition();
            assertEquals(2, result.size());
            assertEquals(3, result.get(0));
            assertEquals(4, result.get(1));
        }

        @Test
        @DisplayName("Extract from Rhombus string")
        void extractRhombus() {
            Position pos = new Position("R_1_2");
            ArrayList<Integer> result = pos.extractPosition();
            assertEquals(2, result.size());
            assertEquals(1, result.get(0));
            assertEquals(2, result.get(1));
        }

        @Test
        @DisplayName("Invalid format throws exception")
        void invalidFormat() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> new Position("X_1_2"));
            assertTrue(exception.getMessage().contains("invalid format"));
        }

    }

    @Nested
    @DisplayName("Tests for getters")
    class GetterTests {
        @Test
        @DisplayName("getPosition returns correct ArrayList after extract")
        void getPositionAfterExtract() {
            Position pos = new Position("R_4_5");
            pos.extractPosition();
            ArrayList<Integer> list = pos.getPosition();
            assertEquals(4, list.get(0));
            assertEquals(5, list.get(1));
        }
    }
}