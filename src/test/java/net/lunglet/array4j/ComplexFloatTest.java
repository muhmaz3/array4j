package net.lunglet.array4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public final class ComplexFloatTest {
    @Test
    public void testConj() {
        ComplexFloat x = ComplexFloat.valueOf(1.0f, 1.0f);
        assertFalse(x.equals(x.conj()));
        assertEquals(x, x.conj().conj());
    }

    @Test
    public void testTimes() {
        ComplexFloat x = ComplexFloat.valueOf(0, 1);
        ComplexFloat y = ComplexFloat.valueOf(-1, 0);
        assertEquals(y, x.times(x));
    }
}
