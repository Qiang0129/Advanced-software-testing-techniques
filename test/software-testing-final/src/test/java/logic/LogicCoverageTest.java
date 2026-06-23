package logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sut.CollectionsUnderTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 任务 3：基于谓词覆盖和 CACC 的逻辑覆盖测试用例。
 */
class LogicCoverageTest {
    @Test
    @DisplayName("L-RA-01 replaceAll CACC：size < 11 为真且 RandomAccess 为假")
    void replaceAllSmallLinkedListMakesSizeClauseDetermineTrue() {
        List<String> values = new LinkedList<>(Arrays.asList("a", "b", "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(new LinkedList<>(Arrays.asList("x", "b", "x")), values);
    }

    @Test
    @DisplayName("L-RA-02 replaceAll CACC：size < 11 为假且 RandomAccess 为假")
    void replaceAllLargeLinkedListMakesSizeClauseDetermineFalse() {
        List<String> values = new LinkedList<>(Collections.nCopies(12, "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(new LinkedList<>(Collections.nCopies(12, "x")), values);
    }

    @Test
    @DisplayName("L-RA-03 replaceAll CACC：RandomAccess 子句决定复合谓词为真")
    void replaceAllLargeArrayListMakesRandomAccessClauseDetermineTrue() {
        List<String> values = new ArrayList<>(Collections.nCopies(12, "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(new ArrayList<>(Collections.nCopies(12, "x")), values);
    }

    @Test
    @DisplayName("L-RA-04 replaceAll PC：oldVal 为 null 且索引路径匹配")
    void replaceAllIndexPathCoversNullOldValuePredicateTrue() {
        List<String> values = new ArrayList<>(Arrays.asList(null, "a", null));

        boolean changed = CollectionsUnderTest.replaceAll(values, null, "x");

        assertTrue(changed);
        assertEquals(Arrays.asList("x", "a", "x"), values);
    }

    @Test
    @DisplayName("L-RA-05 replaceAll PC：非空 oldVal 且索引路径无匹配")
    void replaceAllIndexPathCoversEqualsPredicateFalse() {
        List<String> values = new ArrayList<>(Arrays.asList("a", "b", "c"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "z", "x");

        assertFalse(changed);
        assertEquals(Arrays.asList("a", "b", "c"), values);
    }

    @Test
    @DisplayName("L-RA-06 replaceAll PC：空列表覆盖循环条件为假")
    void replaceAllCoversLoopPredicateFalseWithEmptyList() {
        List<String> values = new ArrayList<>();

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertFalse(changed);
        assertEquals(List.of(), values);
    }

    @Test
    @DisplayName("L-RA-07 replaceAll PC：迭代器路径 oldVal 为 null 但元素不匹配")
    void replaceAllIteratorPathCoversNullComparisonFalse() {
        List<String> values = new LinkedList<>(Collections.nCopies(12, "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, null, "x");

        assertFalse(changed);
        assertEquals(new LinkedList<>(Collections.nCopies(12, "a")), values);
    }

    @Test
    @DisplayName("L-RA-08 replaceAll PC：迭代器路径 oldVal 为 null 且元素匹配")
    void replaceAllIteratorPathCoversNullComparisonTrue() {
        List<String> values = new LinkedList<>(Collections.nCopies(12, "a"));
        values.set(5, null);

        boolean changed = CollectionsUnderTest.replaceAll(values, null, "x");

        List<String> expected = new LinkedList<>(Collections.nCopies(12, "a"));
        expected.set(5, "x");
        assertTrue(changed);
        assertEquals(expected, values);
    }

    @Test
    @DisplayName("L-IS-01 indexOfSubList CACC：sourceSize < 35 子句决定复合谓词为真")
    void indexOfSubListSmallLinkedListMakesSizeClauseDetermineTrue() {
        int index = CollectionsUnderTest.indexOfSubList(
                new LinkedList<>(Arrays.asList(1, 2, 3, 4)),
                new ArrayList<>(Arrays.asList(2, 3))
        );

        assertEquals(1, index);
    }

    @Test
    @DisplayName("L-IS-02 indexOfSubList CACC：sourceSize < 35 为假且 source 非 RandomAccess")
    void indexOfSubListLargeLinkedListMakesSizeClauseDetermineFalse() {
        int index = CollectionsUnderTest.indexOfSubList(
                linkedRange(0, 40),
                new ArrayList<>(Arrays.asList(20, 21))
        );

        assertEquals(20, index);
    }

    @Test
    @DisplayName("L-IS-03 indexOfSubList CACC：source RandomAccess 子句决定复合谓词为真")
    void indexOfSubListLargeArrayListMakesSourceRandomAccessClauseTrue() {
        int index = CollectionsUnderTest.indexOfSubList(
                new ArrayList<>(integerRange(0, 40)),
                new ArrayList<>(Arrays.asList(20, 21))
        );

        assertEquals(20, index);
    }

    @Test
    @DisplayName("L-IS-04 indexOfSubList CACC：target RandomAccess 子句决定复合谓词为假")
    void indexOfSubListLargeArrayListWithLinkedTargetMakesTargetRandomAccessClauseFalse() {
        int index = CollectionsUnderTest.indexOfSubList(
                new ArrayList<>(integerRange(0, 40)),
                new LinkedList<>(Arrays.asList(20, 21))
        );

        assertEquals(20, index);
    }

    @Test
    @DisplayName("L-IS-05 indexOfSubList PC：target 长于 source 覆盖候选循环为假")
    void indexOfSubListCoversCandidateLoopFalseWhenTargetLonger() {
        int index = CollectionsUnderTest.indexOfSubList(
                List.of(1),
                Arrays.asList(1, 2)
        );

        assertEquals(-1, index);
    }

    @Test
    @DisplayName("L-IS-06 indexOfSubList PC：空 target 覆盖内层循环为假")
    void indexOfSubListCoversInnerLoopFalseWithEmptyTarget() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3),
                List.of()
        );

        assertEquals(0, index);
    }

    @Test
    @DisplayName("L-IS-07 indexOfSubList PC：随机访问路径失配后继续并最终匹配")
    void indexOfSubListCoversMismatchPredicateInRandomAccessPath() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(0, 1, 2, 1, 2),
                Arrays.asList(1, 2)
        );

        assertEquals(1, index);
    }

    @Test
    @DisplayName("L-IS-08 indexOfSubList PC：迭代器路径部分匹配失败后回退")
    void indexOfSubListCoversIteratorBacktrackLoopTrue() {
        List<Integer> source = linkedRange(0, 36);
        source.set(0, 0);
        source.set(1, 1);
        source.set(2, 0);
        source.set(3, 99);

        int index = CollectionsUnderTest.indexOfSubList(
                source,
                new LinkedList<>(Arrays.asList(0, 99))
        );

        assertEquals(2, index);
    }

    @Test
    @DisplayName("L-IS-09 indexOfSubList PC：eq 覆盖 null 匹配")
    void indexOfSubListCoversEqNullMatch() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList("a", null, "c"),
                Arrays.asList(null, "c")
        );

        assertEquals(1, index);
    }

    @Test
    @DisplayName("L-IS-10 indexOfSubList PC：迭代器路径全部候选失败")
    void indexOfSubListCoversIteratorAllCandidatesFail() {
        int index = CollectionsUnderTest.indexOfSubList(
                linkedRange(0, 36),
                new ArrayList<>(List.of(1000))
        );

        assertEquals(-1, index);
    }

    @Test
    @DisplayName("L-IS-11 indexOfSubList PC：source 与 target 等长且完全匹配")
    void indexOfSubListCoversEqualLengthFullMatch() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(1, 2, 3)
        );

        assertEquals(0, index);
    }

    @Test
    @DisplayName("L-DJ-01 disjoint PC：c1 为 Set 且无交集")
    void disjointCoversFirstCollectionSetPredicateTrueNoOverlap() {
        boolean result = CollectionsUnderTest.disjoint(
                new HashSet<>(Arrays.asList(1, 2)),
                Arrays.asList(3, 4)
        );

        assertTrue(result);
    }

    @Test
    @DisplayName("L-DJ-02 disjoint PC：c1 为 Set 且有交集")
    void disjointCoversFirstCollectionSetPredicateTrueWithOverlap() {
        boolean result = CollectionsUnderTest.disjoint(
                new HashSet<>(Arrays.asList(1, 2)),
                Arrays.asList(2, 3)
        );

        assertFalse(result);
    }

    @Test
    @DisplayName("L-DJ-03 disjoint PC：c2 为 Set 覆盖 !(c2 instanceof Set) 为假")
    void disjointCoversSecondCollectionSetPredicateFalse() {
        boolean result = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                new LinkedHashSet<>(Arrays.asList(2, 3))
        );

        assertFalse(result);
    }

    @Test
    @DisplayName("L-DJ-04 disjoint PC：两个非 Set 且无交集")
    void disjointCoversNeitherCollectionSetNoOverlap() {
        boolean result = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4)
        );

        assertTrue(result);
    }

    @Test
    @DisplayName("L-DJ-05 disjoint CACC：c1size == 0 子句决定复合谓词为真")
    void disjointCoversFirstSizeZeroClauseTrue() {
        boolean result = CollectionsUnderTest.disjoint(
                List.of(),
                Arrays.asList(1, 2)
        );

        assertTrue(result);
    }

    @Test
    @DisplayName("L-DJ-06 disjoint CACC：c2size == 0 子句决定复合谓词为真")
    void disjointCoversSecondSizeZeroClauseTrue() {
        boolean result = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                List.of()
        );

        assertTrue(result);
    }

    @Test
    @DisplayName("L-DJ-07 disjoint PC：c1.size() > c2.size() 为真")
    void disjointCoversFirstCollectionLargerPredicateTrue() {
        boolean result = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2, 3),
                List.of(4)
        );

        assertTrue(result);
    }

    @Test
    @DisplayName("L-DJ-08 disjoint PC：c1.size() > c2.size() 为假")
    void disjointCoversFirstCollectionLargerPredicateFalse() {
        boolean result = CollectionsUnderTest.disjoint(
                List.of(1),
                Arrays.asList(2, 3)
        );

        assertTrue(result);
    }

    @Test
    @DisplayName("L-DJ-09 disjoint PC：contains.contains(e) 对 null 返回 true")
    void disjointCoversContainsPredicateTrueForNullElement() {
        boolean result = CollectionsUnderTest.disjoint(
                new ArrayList<>(Arrays.asList(null, "a")),
                new ArrayList<>(Arrays.asList("b", null))
        );

        assertFalse(result);
    }

    private static List<Integer> integerRange(int startInclusive, int endExclusive) {
        List<Integer> values = new ArrayList<>();
        for (int value = startInclusive; value < endExclusive; value++) {
            values.add(value);
        }
        return values;
    }

    private static List<Integer> linkedRange(int startInclusive, int endExclusive) {
        return new LinkedList<>(integerRange(startInclusive, endExclusive));
    }
}
