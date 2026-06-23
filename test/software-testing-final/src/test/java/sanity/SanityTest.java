package sanity;

import org.junit.jupiter.api.Test;
import sut.CollectionsUnderTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 基线冒烟测试，用于确认正确版本的三个方法具备基本可用行为。
 */
class SanityTest {
    @Test
    void replaceAllChangesAllMatchingElements() {
        List<String> values = new ArrayList<>(Arrays.asList("a", "b", "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(Arrays.asList("x", "b", "x"), values);
    }

    @Test
    void indexOfSubListFindsMiddleMatch() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(2, 3)
        );

        assertEquals(1, index);
    }

    @Test
    void disjointReturnsFalseWhenCollectionsOverlap() {
        boolean result = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                Arrays.asList(2, 3)
        );

        assertFalse(result);
    }
}
