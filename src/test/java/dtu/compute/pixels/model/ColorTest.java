package dtu.compute.pixels.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @Test
    @DisplayName("all constructors are equal")
    void allConstructorsAreEqual() {
        assertEquals(Color.fromARGB(0x89abcdef), Color.fromInts(0x89, 0xab, 0xcd, 0xef));
        assertEquals(Color.fromARGB(0x89abcdef), new Color((byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef));
        assertEquals(Color.fromARGB(0x89abcdef).toARGB(), 0x89abcdef);
    }

    @Test
    @DisplayName("can convert to and from fractions")
    void canConvertToAndFromFactions() {
        Color color = Color.fromARGB(0x00abcdff);
        double[] fractions = color.toFractions();
        assertEquals(color, Color.fromFractions(fractions[0], fractions[1], fractions[2], fractions[3]));

        Color color2 = Color.fromFractions(0, 0, 0, 1);
        double[] fractions2 = color2.toFractions();
        assertArrayEquals(fractions2, new double[] {0, 0, 0, 1} );
    }
}