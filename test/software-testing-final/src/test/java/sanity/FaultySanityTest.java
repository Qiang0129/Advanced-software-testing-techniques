package sanity;

import org.junit.jupiter.api.Test;
import sut.faulty.FaultyDisjoint;
import sut.faulty.FaultyIndexOfSubList;
import sut.faulty.FaultyReplaceAll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 缺陷版本冒烟测试，用于确认三个植入缺陷都能被关键输入触发。
 */
class FaultySanityTest {
    @Test
    void faultyReplaceAllExposesNullOldValueDefect() {
        List<String> values = new ArrayList<>(Arrays.asList(null, "b", null));

        assertThrows(
                NullPointerException.class,
                () -> FaultyReplaceAll.replaceAll(values, null, "x")
        );
    }

    @Test
    void faultyIndexOfSubListExposesLastCandidateDefect() {
        int index = FaultyIndexOfSubList.indexOfSubList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(3, 4)
        );

        assertEquals(-1, index);
    }

    @Test
    void faultyDisjointExposesOverlapLogicDefect() {
        boolean result = FaultyDisjoint.disjoint(
                Arrays.asList(1, 2),
                Arrays.asList(2, 3)
        );

        assertTrue(result);
    }

    @Test
    void correctOracleForFaultyInputs() {
        List<String> values = new ArrayList<>(Arrays.asList(null, "b", null));
        assertTrue(java.util.Collections.replaceAll(values, null, "x"));
        assertEquals(Arrays.asList("x", "b", "x"), values);

        assertEquals(
                2,
                java.util.Collections.indexOfSubList(
                        Arrays.asList(1, 2, 3, 4),
                        Arrays.asList(3, 4)
                )
        );
        assertFalse(java.util.Collections.disjoint(
                Arrays.asList(1, 2),
                Arrays.asList(2, 3)
        ));
    }
}
