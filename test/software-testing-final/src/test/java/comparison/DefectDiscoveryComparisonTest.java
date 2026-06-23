package comparison;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sut.CollectionsUnderTest;
import sut.faulty.FaultyDisjoint;
import sut.faulty.FaultyIndexOfSubList;
import sut.faulty.FaultyReplaceAll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 不同测试方法缺陷发现情况的证据测试。
 */
class DefectDiscoveryComparisonTest {
    @Test
    @DisplayName("Domain-F1 D-RA-04 发现 replaceAll oldVal 为 null 缺陷")
    void domainTestDetectsReplaceAllNullOldValueDefect() {
        List<String> correctValues = new ArrayList<>(Arrays.asList(null, "b", null));
        assertTrue(CollectionsUnderTest.replaceAll(correctValues, null, "x"));
        assertEquals(Arrays.asList("x", "b", "x"), correctValues);

        List<String> faultyValues = new ArrayList<>(Arrays.asList(null, "b", null));
        assertThrows(NullPointerException.class, () -> FaultyReplaceAll.replaceAll(faultyValues, null, "x"));
    }

    @Test
    @DisplayName("Domain-F2 D-IS-05 发现 indexOfSubList 末尾候选缺陷")
    void domainTestDetectsIndexOfSubListLastCandidateDefect() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4);
        List<Integer> target = Arrays.asList(3, 4);

        int correct = CollectionsUnderTest.indexOfSubList(source, target);
        int faulty = FaultyIndexOfSubList.indexOfSubList(source, target);

        assertEquals(2, correct);
        assertEquals(-1, faulty);
        assertNotEquals(correct, faulty);
    }

    @Test
    @DisplayName("Domain-F3 D-DJ-02 发现 disjoint 交集判断缺陷")
    void domainTestDetectsDisjointOverlapDefect() {
        List<Integer> left = Arrays.asList(1, 2);
        List<Integer> right = Arrays.asList(2, 3);

        boolean correct = CollectionsUnderTest.disjoint(left, right);
        boolean faulty = FaultyDisjoint.disjoint(left, right);

        assertFalse(correct);
        assertTrue(faulty);
    }

    @Test
    @DisplayName("Graph-F1 G-RA-01 发现 replaceAll oldVal 为 null 缺陷")
    void graphTestDetectsReplaceAllNullOldValueDefect() {
        List<String> correctValues = new ArrayList<>(Arrays.asList(null, "a", null));
        assertTrue(CollectionsUnderTest.replaceAll(correctValues, null, "x"));
        assertEquals(Arrays.asList("x", "a", "x"), correctValues);

        List<String> faultyValues = new ArrayList<>(Arrays.asList(null, "a", null));
        assertThrows(NullPointerException.class, () -> FaultyReplaceAll.replaceAll(faultyValues, null, "x"));
    }

    @Test
    @DisplayName("Graph-F2 G-IS-06 发现 indexOfSubList 末尾候选缺陷")
    void graphTestDetectsIndexOfSubListLastCandidateDefect() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4);
        List<Integer> target = Arrays.asList(3, 4);

        int correct = CollectionsUnderTest.indexOfSubList(source, target);
        int faulty = FaultyIndexOfSubList.indexOfSubList(source, target);

        assertEquals(2, correct);
        assertEquals(-1, faulty);
        assertNotEquals(correct, faulty);
    }

    @Test
    @DisplayName("Graph-F3 G-DJ-02 发现 disjoint 交集判断缺陷")
    void graphTestDetectsDisjointOverlapDefect() {
        HashSet<Integer> left = new HashSet<>(Arrays.asList(1, 2));
        List<Integer> right = Arrays.asList(2, 3);

        boolean correct = CollectionsUnderTest.disjoint(left, right);
        boolean faulty = FaultyDisjoint.disjoint(left, right);

        assertFalse(correct);
        assertTrue(faulty);
    }

    @Test
    @DisplayName("Logic-F1 L-RA-08 发现 replaceAll oldVal 为 null 缺陷")
    void logicTestDetectsReplaceAllNullOldValueDefect() {
        List<String> correctValues = new LinkedList<>(Arrays.asList("a", "b", null, "c", "d", "e",
                "f", "g", "h", "i", "j", "k"));
        assertTrue(CollectionsUnderTest.replaceAll(correctValues, null, "x"));

        List<String> faultyValues = new LinkedList<>(Arrays.asList("a", "b", null, "c", "d", "e",
                "f", "g", "h", "i", "j", "k"));
        assertThrows(NullPointerException.class, () -> FaultyReplaceAll.replaceAll(faultyValues, null, "x"));
    }

    @Test
    @DisplayName("Logic-F2 L-IS-11 发现 indexOfSubList 末尾候选缺陷")
    void logicTestDetectsIndexOfSubListLastCandidateDefect() {
        List<Integer> source = Arrays.asList(1, 2, 3);
        List<Integer> target = Arrays.asList(1, 2, 3);

        int correct = CollectionsUnderTest.indexOfSubList(source, target);
        int faulty = FaultyIndexOfSubList.indexOfSubList(source, target);

        assertEquals(0, correct);
        assertEquals(-1, faulty);
        assertNotEquals(correct, faulty);
    }

    @Test
    @DisplayName("Logic-F3 L-DJ-03 发现 disjoint 交集判断缺陷")
    void logicTestDetectsDisjointOverlapDefect() {
        List<Integer> left = Arrays.asList(1, 2);
        LinkedHashSet<Integer> right = new LinkedHashSet<>(Arrays.asList(2, 3));

        boolean correct = CollectionsUnderTest.disjoint(left, right);
        boolean faulty = FaultyDisjoint.disjoint(left, right);

        assertFalse(correct);
        assertTrue(faulty);
    }

    @Test
    @DisplayName("AI-F1 AI-RA-03 发现 replaceAll oldVal 为 null 缺陷")
    void aiTestDetectsReplaceAllNullOldValueDefect() {
        List<String> correctValues = new ArrayList<>(Arrays.asList(null, "b", null));
        assertTrue(CollectionsUnderTest.replaceAll(correctValues, null, "x"));
        assertEquals(Arrays.asList("x", "b", "x"), correctValues);

        List<String> faultyValues = new ArrayList<>(Arrays.asList(null, "b", null));
        assertThrows(NullPointerException.class, () -> FaultyReplaceAll.replaceAll(faultyValues, null, "x"));
    }

    @Test
    @DisplayName("AI-F2 AI-IS-03 发现 indexOfSubList 末尾候选缺陷")
    void aiTestDetectsIndexOfSubListLastCandidateDefect() {
        List<Integer> source = Arrays.asList(1, 2, 3, 4);
        List<Integer> target = Arrays.asList(3, 4);

        int correct = CollectionsUnderTest.indexOfSubList(source, target);
        int faulty = FaultyIndexOfSubList.indexOfSubList(source, target);

        assertEquals(2, correct);
        assertEquals(-1, faulty);
        assertNotEquals(correct, faulty);
    }

    @Test
    @DisplayName("AI-F3 AI-DJ-02 发现 disjoint 交集判断缺陷")
    void aiTestDetectsDisjointOverlapDefect() {
        List<Integer> left = Arrays.asList(1, 2);
        List<Integer> right = Arrays.asList(2, 3);

        boolean correct = CollectionsUnderTest.disjoint(left, right);
        boolean faulty = FaultyDisjoint.disjoint(left, right);

        assertFalse(correct);
        assertTrue(faulty);
    }
}
