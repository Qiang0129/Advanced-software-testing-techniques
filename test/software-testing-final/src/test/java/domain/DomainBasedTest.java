package domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sut.CollectionsUnderTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 基于输入域划分、边界值分析和基本选择覆盖的测试用例。
 */
class DomainBasedTest {
    @Test
    @DisplayName("D-RA-01 replaceAll 基本选择：ArrayList 普通值多处匹配")
    void replaceAllBaseChoiceMultipleMatches() {
        List<String> values = new ArrayList<>(Arrays.asList("a", "b", "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(Arrays.asList("x", "b", "x"), values);
    }

    @Test
    @DisplayName("D-RA-02 replaceAll 边界值：空列表")
    void replaceAllReturnsFalseForEmptyList() {
        List<String> values = new ArrayList<>();

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertFalse(changed);
        assertEquals(List.of(), values);
    }

    @Test
    @DisplayName("D-RA-03 replaceAll 输入域：无匹配元素")
    void replaceAllReturnsFalseWhenNoElementMatches() {
        List<String> values = new ArrayList<>(Arrays.asList("a", "b", "c"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "z", "x");

        assertFalse(changed);
        assertEquals(Arrays.asList("a", "b", "c"), values);
    }

    @Test
    @DisplayName("D-RA-04 replaceAll 边界值：oldVal 为 null")
    void replaceAllHandlesNullOldValue() {
        List<String> values = new ArrayList<>(Arrays.asList(null, "b", null));

        boolean changed = CollectionsUnderTest.replaceAll(values, null, "x");

        assertTrue(changed);
        assertEquals(Arrays.asList("x", "b", "x"), values);
    }

    @Test
    @DisplayName("D-RA-05 replaceAll 边界值：newVal 为 null")
    void replaceAllHandlesNullNewValue() {
        List<String> values = new ArrayList<>(Arrays.asList("a", "b", "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", null);

        assertTrue(changed);
        assertEquals(Arrays.asList(null, "b", null), values);
    }

    @Test
    @DisplayName("D-RA-06 replaceAll 输入域：LinkedList 且长度大于阈值")
    void replaceAllUsesIteratorPathForLargeLinkedList() {
        List<String> values = new LinkedList<>(java.util.Collections.nCopies(12, "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(new LinkedList<>(java.util.Collections.nCopies(12, "x")), values);
    }

    @Test
    @DisplayName("D-RA-07 replaceAll 输入域：不可修改列表且存在匹配")
    void replaceAllThrowsForUnmodifiableListWhenReplacementIsNeeded() {
        List<String> values = List.of("a", "b");

        assertThrows(
                UnsupportedOperationException.class,
                () -> CollectionsUnderTest.replaceAll(values, "a", "x")
        );
    }

    @Test
    @DisplayName("D-RA-08 replaceAll 边界值：单元素列表且匹配")
    void replaceAllHandlesSingleElementMatch() {
        List<String> values = new ArrayList<>(List.of("a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(List.of("x"), values);
    }

    @Test
    @DisplayName("D-RA-09 replaceAll 边界值：LinkedList 长度正好为阈值 11")
    void replaceAllHandlesLinkedListAtThreshold() {
        List<String> values = new LinkedList<>(java.util.Collections.nCopies(11, "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(new LinkedList<>(java.util.Collections.nCopies(11, "x")), values);
    }

    @Test
    @DisplayName("D-RA-10 replaceAll 输入域：只有一次匹配")
    void replaceAllHandlesSingleMatch() {
        List<String> values = new ArrayList<>(Arrays.asList("a", "b", "c"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "b", "x");

        assertTrue(changed);
        assertEquals(Arrays.asList("a", "x", "c"), values);
    }

    @Test
    @DisplayName("D-IS-01 indexOfSubList 基本选择：目标在中间")
    void indexOfSubListFindsMiddleMatch() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(2, 3)
        );

        assertEquals(1, index);
    }

    @Test
    @DisplayName("D-IS-02 indexOfSubList 边界值：target 为空")
    void indexOfSubListReturnsZeroForEmptyTarget() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3),
                List.of()
        );

        assertEquals(0, index);
    }

    @Test
    @DisplayName("D-IS-03 indexOfSubList 边界值：target 长于 source")
    void indexOfSubListReturnsNegativeOneWhenTargetLongerThanSource() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2),
                Arrays.asList(1, 2, 3)
        );

        assertEquals(-1, index);
    }

    @Test
    @DisplayName("D-IS-04 indexOfSubList 输入域：匹配在开头")
    void indexOfSubListFindsPrefixMatch() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(1, 2)
        );

        assertEquals(0, index);
    }

    @Test
    @DisplayName("D-IS-05 indexOfSubList 边界值：匹配在末尾")
    void indexOfSubListFindsSuffixMatch() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(3, 4)
        );

        assertEquals(2, index);
    }

    @Test
    @DisplayName("D-IS-06 indexOfSubList 输入域：不存在匹配")
    void indexOfSubListReturnsNegativeOneWhenNoMatchExists() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(2, 4)
        );

        assertEquals(-1, index);
    }

    @Test
    @DisplayName("D-IS-07 indexOfSubList 边界值：包含 null 的匹配")
    void indexOfSubListHandlesNullElements() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList("a", null, "c"),
                Arrays.asList(null, "c")
        );

        assertEquals(1, index);
    }

    @Test
    @DisplayName("D-IS-08 indexOfSubList 输入域：LinkedList 且长度达到阈值")
    void indexOfSubListUsesIteratorPathForLargeLinkedList() {
        List<Integer> source = new LinkedList<>();
        for (int i = 0; i < 40; i++) {
            source.add(i);
        }
        List<Integer> target = new LinkedList<>(Arrays.asList(35, 36, 37));

        int index = CollectionsUnderTest.indexOfSubList(source, target);

        assertEquals(35, index);
    }

    @Test
    @DisplayName("D-IS-09 indexOfSubList 输入域：source 与 target 等长且完全匹配")
    void indexOfSubListFindsEqualLengthMatch() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(1, 2, 3)
        );

        assertEquals(0, index);
    }

    @Test
    @DisplayName("D-IS-10 indexOfSubList 输入域：部分前缀匹配后失败")
    void indexOfSubListHandlesPartialPrefixMatchFailure() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 9, 1, 2, 3),
                Arrays.asList(1, 2, 3)
        );

        assertEquals(3, index);
    }

    @Test
    @DisplayName("D-IS-11 indexOfSubList 边界值：sourceSize 等于 34")
    void indexOfSubListHandlesSourceSizeJustBelowThreshold() {
        List<Integer> source = new ArrayList<>();
        for (int i = 0; i < 34; i++) {
            source.add(i);
        }

        int index = CollectionsUnderTest.indexOfSubList(source, Arrays.asList(31, 32, 33));

        assertEquals(31, index);
    }

    @Test
    @DisplayName("D-IS-12 indexOfSubList 边界值：sourceSize 等于 35 且混合列表类型")
    void indexOfSubListHandlesThresholdWithMixedListTypes() {
        List<Integer> source = new LinkedList<>();
        for (int i = 0; i < 35; i++) {
            source.add(i);
        }

        int index = CollectionsUnderTest.indexOfSubList(source, Arrays.asList(32, 33, 34));

        assertEquals(32, index);
    }

    @Test
    @DisplayName("D-DJ-01 disjoint 基本选择：两个集合无交集")
    void disjointReturnsTrueWhenCollectionsDoNotOverlap() {
        boolean result = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4)
        );

        assertTrue(result);
    }

    @Test
    @DisplayName("D-DJ-02 disjoint 输入域：存在公共元素")
    void disjointReturnsFalseWhenCollectionsOverlap() {
        boolean result = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                Arrays.asList(2, 3)
        );

        assertFalse(result);
    }

    @Test
    @DisplayName("D-DJ-03 disjoint 边界值：第一个集合为空")
    void disjointReturnsTrueWhenFirstCollectionIsEmpty() {
        boolean result = CollectionsUnderTest.disjoint(
                List.of(),
                Arrays.asList(1, 2)
        );

        assertTrue(result);
    }

    @Test
    @DisplayName("D-DJ-04 disjoint 边界值：第二个集合为空")
    void disjointReturnsTrueWhenSecondCollectionIsEmpty() {
        boolean result = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                List.of()
        );

        assertTrue(result);
    }

    @Test
    @DisplayName("D-DJ-05 disjoint 输入域：c1 为 Set 且存在公共元素")
    void disjointUsesFirstSetAsContainsCollection() {
        Set<Integer> first = new HashSet<>(Arrays.asList(1, 2));

        boolean result = CollectionsUnderTest.disjoint(first, Arrays.asList(2, 3));

        assertFalse(result);
    }

    @Test
    @DisplayName("D-DJ-06 disjoint 输入域：c2 为 Set 且存在公共元素")
    void disjointUsesSecondSetAsContainsCollection() {
        Set<Integer> second = new HashSet<>(Arrays.asList(2, 3));

        boolean result = CollectionsUnderTest.disjoint(Arrays.asList(1, 2), second);

        assertFalse(result);
    }

    @Test
    @DisplayName("D-DJ-07 disjoint 输入域：两个非 Set 且 c1 大于 c2")
    void disjointIteratesSmallerCollectionWhenNeitherCollectionIsSet() {
        boolean result = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(4)
        );

        assertFalse(result);
    }

    @Test
    @DisplayName("D-DJ-08 disjoint 边界值：公共元素为 null")
    void disjointHandlesNullCommonElement() {
        boolean result = CollectionsUnderTest.disjoint(
                Arrays.asList(null, "a"),
                Arrays.asList("b", null)
        );

        assertFalse(result);
    }

    @Test
    @DisplayName("D-DJ-09 disjoint 边界值：同一个非空集合对象")
    void disjointReturnsFalseForSameNonEmptyCollectionObject() {
        List<String> values = Arrays.asList("a", "b");

        boolean result = CollectionsUnderTest.disjoint(values, values);

        assertFalse(result);
    }

    @Test
    @DisplayName("D-DJ-10 disjoint 边界值：两个集合都为空")
    void disjointReturnsTrueWhenBothCollectionsAreEmpty() {
        boolean result = CollectionsUnderTest.disjoint(List.of(), List.of());

        assertTrue(result);
    }

    @Test
    @DisplayName("D-DJ-11 disjoint 输入域：存在多个公共元素")
    void disjointReturnsFalseWhenMultipleElementsOverlap() {
        boolean result = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2, 3),
                Arrays.asList(2, 3, 4)
        );

        assertFalse(result);
    }

    @Test
    @DisplayName("D-DJ-12 disjoint 输入域：Set 和 Set 组合且无交集")
    void disjointHandlesTwoSetsWithoutOverlap() {
        Set<Integer> first = new HashSet<>(Arrays.asList(1, 2));
        Set<Integer> second = new HashSet<>(Arrays.asList(3, 4));

        boolean result = CollectionsUnderTest.disjoint(first, second);

        assertTrue(result);
    }
}
