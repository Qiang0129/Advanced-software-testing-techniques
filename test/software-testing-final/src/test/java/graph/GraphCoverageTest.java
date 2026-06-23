package graph;

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
 * 基于 CFG 和主路径覆盖需求设计的图覆盖测试用例。
 */
class GraphCoverageTest {
    @Test
    @DisplayName("G-RA-01 replaceAll 小 ArrayList：oldVal 为 null 且命中")
    void replaceAllSmallRandomAccessNullOldValueMatch() {
        List<String> values = new ArrayList<>(Arrays.asList(null, "a", null));

        boolean changed = CollectionsUnderTest.replaceAll(values, null, "x");

        assertTrue(changed);
        assertEquals(Arrays.asList("x", "a", "x"), values);
    }

    @Test
    @DisplayName("G-RA-02 replaceAll 小 ArrayList：非空 oldVal 多次命中")
    void replaceAllSmallRandomAccessNonNullMultipleMatches() {
        List<String> values = new ArrayList<>(Arrays.asList("a", "b", "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(Arrays.asList("x", "b", "x"), values);
    }

    @Test
    @DisplayName("G-RA-03 replaceAll 小 ArrayList：非空 oldVal 无命中")
    void replaceAllSmallRandomAccessNoMatch() {
        List<String> values = new ArrayList<>(Arrays.asList("a", "b", "c"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "z", "x");

        assertFalse(changed);
        assertEquals(Arrays.asList("a", "b", "c"), values);
    }

    @Test
    @DisplayName("G-RA-04 replaceAll 空列表：循环 0 次")
    void replaceAllEmptyListCoversZeroIteration() {
        List<String> values = new ArrayList<>();

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertFalse(changed);
        assertEquals(List.of(), values);
    }

    @Test
    @DisplayName("G-RA-05 replaceAll 大 LinkedList：oldVal 为 null 的迭代器路径")
    void replaceAllLargeLinkedListNullOldValueIteratorPath() {
        List<String> values = new LinkedList<>(Collections.nCopies(12, "a"));
        values.set(3, null);
        values.set(7, null);

        boolean changed = CollectionsUnderTest.replaceAll(values, null, "x");

        List<String> expected = new LinkedList<>(Collections.nCopies(12, "a"));
        expected.set(3, "x");
        expected.set(7, "x");
        assertTrue(changed);
        assertEquals(expected, values);
    }

    @Test
    @DisplayName("G-RA-06 replaceAll 大 LinkedList：非空 oldVal 的迭代器路径")
    void replaceAllLargeLinkedListNonNullIteratorPath() {
        List<String> values = new LinkedList<>(Collections.nCopies(12, "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(new LinkedList<>(Collections.nCopies(12, "x")), values);
    }

    @Test
    @DisplayName("G-RA-07 replaceAll 大 ArrayList：RandomAccess 仍走索引路径")
    void replaceAllLargeArrayListStillUsesRandomAccessPath() {
        List<String> values = new ArrayList<>(Collections.nCopies(12, "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(new ArrayList<>(Collections.nCopies(12, "x")), values);
    }

    @Test
    @DisplayName("G-IS-01 indexOfSubList 空 target：内层循环 0 次返回 0")
    void indexOfSubListEmptyTargetReturnsZero() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3),
                List.of()
        );

        assertEquals(0, index);
    }

    @Test
    @DisplayName("G-IS-02 indexOfSubList 随机访问路径：首候选命中")
    void indexOfSubListRandomAccessFirstCandidateMatches() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(1, 2)
        );

        assertEquals(0, index);
    }

    @Test
    @DisplayName("G-IS-03 indexOfSubList 随机访问路径：先失败后命中")
    void indexOfSubListRandomAccessContinuesAfterMismatch() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(0, 1, 2, 1, 2),
                Arrays.asList(1, 2)
        );

        assertEquals(1, index);
    }

    @Test
    @DisplayName("G-IS-04 indexOfSubList target 长于 source：候选循环 0 次")
    void indexOfSubListTargetLongerThanSourceReturnsMinusOne() {
        int index = CollectionsUnderTest.indexOfSubList(
                List.of(1),
                Arrays.asList(1, 2)
        );

        assertEquals(-1, index);
    }

    @Test
    @DisplayName("G-IS-05 indexOfSubList 随机访问路径：全部候选失败")
    void indexOfSubListRandomAccessAllCandidatesFail() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3),
                List.of(4)
        );

        assertEquals(-1, index);
    }

    @Test
    @DisplayName("G-IS-06 indexOfSubList 随机访问路径：末尾候选命中")
    void indexOfSubListRandomAccessLastCandidateMatches() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(3, 4)
        );

        assertEquals(2, index);
    }

    @Test
    @DisplayName("G-IS-07 indexOfSubList 大 LinkedList：迭代器路径命中")
    void indexOfSubListIteratorPathMatches() {
        List<Integer> source = linkedRange(0, 40);
        List<Integer> target = new LinkedList<>(Arrays.asList(20, 21, 22));

        int index = CollectionsUnderTest.indexOfSubList(source, target);

        assertEquals(20, index);
    }

    @Test
    @DisplayName("G-IS-08 indexOfSubList 大 LinkedList：部分匹配失败后回退")
    void indexOfSubListIteratorPathBacktracksAfterPartialMismatch() {
        List<Integer> source = linkedRange(0, 36);
        source.set(0, 0);
        source.set(1, 1);
        source.set(2, 0);
        source.set(3, 99);
        List<Integer> target = new LinkedList<>(Arrays.asList(0, 99));

        int index = CollectionsUnderTest.indexOfSubList(source, target);

        assertEquals(2, index);
    }

    @Test
    @DisplayName("G-IS-09 indexOfSubList 大列表混合类型：迭代器路径失败返回 -1")
    void indexOfSubListIteratorPathAllCandidatesFail() {
        List<Integer> source = linkedRange(0, 36);
        List<Integer> target = new ArrayList<>(List.of(1000));

        int index = CollectionsUnderTest.indexOfSubList(source, target);

        assertEquals(-1, index);
    }

    @Test
    @DisplayName("G-DJ-01 disjoint：c1 为 Set 且无交集")
    void disjointWhenFirstCollectionIsSetAndNoOverlap() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                new HashSet<>(Arrays.asList(1, 2)),
                Arrays.asList(3, 4)
        );

        assertTrue(disjoint);
    }

    @Test
    @DisplayName("G-DJ-02 disjoint：c1 为 Set 且有交集")
    void disjointWhenFirstCollectionIsSetAndOverlapExists() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                new HashSet<>(Arrays.asList(1, 2)),
                Arrays.asList(2, 3)
        );

        assertFalse(disjoint);
    }

    @Test
    @DisplayName("G-DJ-03 disjoint：两个 List 且 c1 为空提前返回")
    void disjointReturnsTrueWhenFirstListIsEmpty() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                List.of(),
                Arrays.asList(1, 2)
        );

        assertTrue(disjoint);
    }

    @Test
    @DisplayName("G-DJ-04 disjoint：两个 List 且 c2 为空提前返回")
    void disjointReturnsTrueWhenSecondListIsEmpty() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                List.of()
        );

        assertTrue(disjoint);
    }

    @Test
    @DisplayName("G-DJ-05 disjoint：两个 List 且 c1 更大触发交换")
    void disjointSwapsIterationTargetWhenFirstListIsLarger() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2, 3),
                List.of(4)
        );

        assertTrue(disjoint);
    }

    @Test
    @DisplayName("G-DJ-06 disjoint：两个 List 不交换且无交集")
    void disjointKeepsIterationTargetWhenFirstListIsNotLarger() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                List.of(1),
                Arrays.asList(2, 3)
        );

        assertTrue(disjoint);
    }

    @Test
    @DisplayName("G-DJ-07 disjoint：c2 为 Set 且有交集")
    void disjointUsesSecondSetForContainsWhenOverlapExists() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                new LinkedHashSet<>(Arrays.asList(2, 3))
        );

        assertFalse(disjoint);
    }

    @Test
    @DisplayName("G-DJ-08 disjoint：公共元素为 null")
    void disjointDetectsNullCommonElement() {
        List<String> left = new ArrayList<>(Arrays.asList(null, "a"));
        List<String> right = new ArrayList<>(Arrays.asList("b", null));

        boolean disjoint = CollectionsUnderTest.disjoint(left, right);

        assertFalse(disjoint);
    }

    private static List<Integer> linkedRange(int startInclusive, int endExclusive) {
        List<Integer> values = new LinkedList<>();
        for (int value = startInclusive; value < endExclusive; value++) {
            values.add(value);
        }
        return values;
    }
}
