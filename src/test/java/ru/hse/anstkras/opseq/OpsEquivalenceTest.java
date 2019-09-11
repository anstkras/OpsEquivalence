package ru.hse.anstkras.opseq;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpsEquivalenceTest {
    @Test
    void testSimple() {
        assertTrue(OpsEquivalence.opsEqual("+20", "+20"));
    }

    @Test
    void testDifficult() {
        assertTrue(OpsEquivalence.opsEqual("+20*10*2+10*20", "+20*20+10*2*10"));
    }

    @Test
    void testMultipleByOne() {
        assertTrue(OpsEquivalence.opsEqual("+20*1+10", "+20+10*1"));
    }

    @Test
    void testNotEqual() {
        assertFalse(OpsEquivalence.opsEqual("+10*2", "*2+10"));
    }
}
