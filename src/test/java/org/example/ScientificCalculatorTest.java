package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class ScientificCalculatorTest {

    @Test
    public void testSquareRoot() {

        assertEquals(3.0, Main.squareRoot(9.0), 0.001);
        assertThrows(IllegalArgumentException.class, () -> Main.squareRoot(-4.0));
    }

    @Test
    public void testFactorial() {

        assertEquals(1, Main.factorial(0));


        assertEquals(120, Main.factorial(5));


        assertThrows(IllegalArgumentException.class, () -> Main.factorial(-1));
    }

    @Test
    public void testNaturalLog() {

        assertEquals(Math.log(10), Main.naturalLog(10), 0.001);
        assertThrows(IllegalArgumentException.class, () -> Main.naturalLog(0));
        assertThrows(IllegalArgumentException.class, () -> Main.naturalLog(-5));
    }

    @Test
    public void testPower() {
        assertEquals(8.0, Main.power(2.0, 3.0), 0.001);

        assertEquals(1.0, Main.power(2.0, 0.0), 0.001);
    }
}