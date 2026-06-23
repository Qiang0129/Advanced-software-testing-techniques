package ai;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sut.CollectionsUnderTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 任务 4：AI 辅助生成并经人工审查修正后的测试用例。
 */
class AIGeneratedCollectionsTest {
    @Test
    @DisplayName("AI-RA-01 replaceAll 普通值多次替换")
    void replaceAllReplacesMultipleOccurrences() {
        List<String> values = new ArrayList<>(Arrays.asList("a", "b", "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(Arrays.asList("x", "b", "x"), values);
    }

    @Test
    @DisplayName("AI-RA-02 replaceAll 无匹配返回 false")
    void replaceAllReturnsFalseWhenNoValueMatches() {
        List<String> values = new ArrayList<>(Arrays.asList("a", "b", "c"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "z", "x");

        assertFalse(changed);
        assertEquals(Arrays.asList("a", "b", "c"), values);
    }

    @Test
    @DisplayName("AI-RA-03 replaceAll oldVal 为 null")
    void replaceAllHandlesNullOldValue() {
        List<String> values = new ArrayList<>(Arrays.asList(null, "b", null));

        boolean changed = CollectionsUnderTest.replaceAll(values, null, "x");

        assertTrue(changed);
        assertEquals(Arrays.asList("x", "b", "x"), values);
    }

    @Test
    @DisplayName("AI-RA-04 replaceAll newVal 为 null")
    void replaceAllHandlesNullNewValue() {
        List<String> values = new ArrayList<>(Arrays.asList("a", "b", "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", null);

        assertTrue(changed);
        assertEquals(Arrays.asList(null, "b", null), values);
    }

    @Test
    @DisplayName("AI-RA-05 replaceAll 空列表")
    void replaceAllReturnsFalseForEmptyList() {
        List<String> values = new ArrayList<>();

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertFalse(changed);
        assertEquals(List.of(), values);
    }

    @Test
    @DisplayName("AI-RA-06 replaceAll 大 LinkedList")
    void replaceAllHandlesLargeLinkedList() {
        List<String> values = new LinkedList<>(Collections.nCopies(12, "a"));

        boolean changed = CollectionsUnderTest.replaceAll(values, "a", "x");

        assertTrue(changed);
        assertEquals(new LinkedList<>(Collections.nCopies(12, "x")), values);
    }

    @Test
    @DisplayName("AI-RA-07 replaceAll 不可修改列表")
    void replaceAllThrowsForUnmodifiableList() {
        List<String> values = List.of("a", "b");

        assertThrows(
                UnsupportedOperationException.class,
                () -> CollectionsUnderTest.replaceAll(values, "a", "x")
        );
    }

    @Test
    @DisplayName("AI-IS-01 indexOfSubList 中间匹配")
    void indexOfSubListFindsMiddleMatch() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(2, 3)
        );

        assertEquals(1, index);
    }

    @Test
    @DisplayName("AI-IS-02 indexOfSubList 开头匹配")
    void indexOfSubListFindsPrefixMatch() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(1, 2)
        );

        assertEquals(0, index);
    }

    @Test
    @DisplayName("AI-IS-03 indexOfSubList 末尾匹配")
    void indexOfSubListFindsSuffixMatch() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(3, 4)
        );

        assertEquals(2, index);
    }

    @Test
    @DisplayName("AI-IS-04 indexOfSubList 空 target")
    void indexOfSubListReturnsZeroForEmptyTarget() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3),
                List.of()
        );

        assertEquals(0, index);
    }

    @Test
    @DisplayName("AI-IS-05 indexOfSubList target 长于 source")
    void indexOfSubListReturnsMinusOneWhenTargetIsLonger() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2),
                Arrays.asList(1, 2, 3)
        );

        assertEquals(-1, index);
    }

    @Test
    @DisplayName("AI-IS-06 indexOfSubList 无匹配")
    void indexOfSubListReturnsMinusOneWhenNoMatchExists() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList(1, 2, 3, 4),
                Arrays.asList(2, 4)
        );

        assertEquals(-1, index);
    }

    @Test
    @DisplayName("AI-IS-07 indexOfSubList 包含 null 的子列表匹配")
    void indexOfSubListHandlesNullElements() {
        int index = CollectionsUnderTest.indexOfSubList(
                Arrays.asList("a", null, "c"),
                Arrays.asList(null, "c")
        );

        assertEquals(1, index);
    }

    @Test
    @DisplayName("AI-DJ-01 disjoint 无交集")
    void disjointReturnsTrueWhenNoCommonElementsExist() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4)
        );

        assertTrue(disjoint);
    }

    @Test
    @DisplayName("AI-DJ-02 disjoint 有交集")
    void disjointReturnsFalseWhenCommonElementExists() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                Arrays.asList(2, 3)
        );

        assertFalse(disjoint);
    }

    @Test
    @DisplayName("AI-DJ-03 disjoint 第一个集合为空")
    void disjointReturnsTrueWhenFirstCollectionIsEmpty() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                List.of(),
                Arrays.asList(1, 2)
        );

        assertTrue(disjoint);
    }

    @Test
    @DisplayName("AI-DJ-04 disjoint 第二个集合为空")
    void disjointReturnsTrueWhenSecondCollectionIsEmpty() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                Arrays.asList(1, 2),
                List.of()
        );

        assertTrue(disjoint);
    }

    @Test
    @DisplayName("AI-DJ-05 disjoint Set 与 List 组合")
    void disjointHandlesSetAndListCombination() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                new HashSet<>(Arrays.asList(1, 2)),
                Arrays.asList(2, 3)
        );

        assertFalse(disjoint);
    }

    @Test
    @DisplayName("AI-DJ-06 disjoint 公共元素为 null")
    void disjointHandlesNullCommonElement() {
        boolean disjoint = CollectionsUnderTest.disjoint(
                new ArrayList<>(Arrays.asList(null, "a")),
                new ArrayList<>(Arrays.asList("b", null))
        );

        assertFalse(disjoint);
    }
}
