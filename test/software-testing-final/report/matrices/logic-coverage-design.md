# 任务 3：逻辑覆盖与 CACC 测试设计

## 1. 说明

本文件记录任务 3 的谓词提取、谓词覆盖和相关性有效子句覆盖（CACC）测试用例。测试对象为正确版本 `sut.CollectionsUnderTest`，测试代码位于 `src/test/java/logic/LogicCoverageTest.java`。

设计依据为本地复制的 OpenJDK `java.util.Collections` 源码。对只有一个子句的谓词，本任务做谓词覆盖；对包含多个子句的复合谓词，本任务做 CACC，并在存在 Java 短路求值影响时说明可行性。

## 2. 测试用例设计过程

1. 提取谓词
   - 从三个被测方法源码中提取 `if` 条件、循环条件和辅助方法中的条件表达式。
   - 将源码谓词按方法编号为 `P-RA-*`、`P-IS-*`、`P-DJ-*`。

2. 标记子句
   - 对复合谓词继续拆分子句，例如 `A || B` 或 `A || (B && C)`。
   - 对每个子句标记为 `a`、`b`、`c`，便于描述 CACC 需求。

3. 区分覆盖准则
   - 单子句谓词要求谓词结果取到 `true` 和 `false`。
   - 复合谓词要求每个主子句能独立决定整个谓词结果。

4. 处理短路求值
   - Java 的 `||` 和 `&&` 存在短路求值。
   - 如果前一子句已经决定结果，后一子句在运行时不会被求值。
   - CACC 测试只选择运行时可行的输入组合，不把不可求值组合伪装成已覆盖。

5. 映射到 JUnit 5
   - 每条测试需求使用 `L-方法缩写-编号` 命名。
   - `L-RA` 表示 `replaceAll`，`L-IS` 表示 `indexOfSubList`，`L-DJ` 表示 `disjoint`。
   - 在 `LogicCoverageTest` 中通过 `@DisplayName` 保留测试需求编号。

## 3. 谓词与子句编号

| 谓词编号 | 方法 | 源码谓词 | 子句编号 | 覆盖准则 |
| --- | --- | --- | --- | --- |
| P-RA-01 | `replaceAll` | `size < REPLACEALL_THRESHOLD || list instanceof RandomAccess` | a: `size < 11`; b: `list instanceof RandomAccess` | CACC |
| P-RA-02 | `replaceAll` | `oldVal == null` | 单子句 | PC |
| P-RA-03 | `replaceAll` | `i < size` | 单子句 | PC |
| P-RA-04 | `replaceAll` | `list.get(i) == null` | 单子句 | PC |
| P-RA-05 | `replaceAll` | `oldVal.equals(list.get(i))` | 单子句 | PC |
| P-RA-06 | `replaceAll` | `itr.next() == null` | 单子句 | PC |
| P-RA-07 | `replaceAll` | `oldVal.equals(itr.next())` | 单子句 | PC |
| P-IS-01 | `indexOfSubList` | `sourceSize < INDEXOFSUBLIST_THRESHOLD || (source instanceof RandomAccess && target instanceof RandomAccess)` | a: `sourceSize < 35`; b: `source instanceof RandomAccess`; c: `target instanceof RandomAccess` | CACC |
| P-IS-02 | `indexOfSubList` | `candidate <= maxCandidate` | 单子句 | PC |
| P-IS-03 | `indexOfSubList` | `i < targetSize` | 单子句 | PC |
| P-IS-04 | `indexOfSubList` | `!eq(...)` | 单子句 | PC |
| P-IS-05 | `indexOfSubList` | `j < i` | 单子句 | PC |
| P-IS-06 | `eq` | `o1 == null ? o2 == null : o1.equals(o2)` | a: `o1 == null`; b: `o2 == null`; c: `o1.equals(o2)` | PC |
| P-DJ-01 | `disjoint` | `c1 instanceof Set` | 单子句 | PC |
| P-DJ-02 | `disjoint` | `!(c2 instanceof Set)` | 单子句 | PC |
| P-DJ-03 | `disjoint` | `c1size == 0 || c2size == 0` | a: `c1size == 0`; b: `c2size == 0` | CACC |
| P-DJ-04 | `disjoint` | `c1size > c2size` | 单子句 | PC |
| P-DJ-05 | `disjoint` | `contains.contains(e)` | 单子句 | PC |

## 4. CACC 需求说明

1. `P-RA-01`
   - 主子句 a：固定 b 为 `false`，用小 `LinkedList` 与大 `LinkedList` 使 a 从 `true` 变为 `false`，复合谓词随之从 `true` 变为 `false`。
   - 主子句 b：固定 a 为 `false`，用大 `LinkedList` 与大 `ArrayList` 使 b 从 `false` 变为 `true`，复合谓词随之从 `false` 变为 `true`。

2. `P-IS-01`
   - 主子句 a：固定 b 为 `false`、c 为 `true`，用小 `LinkedList` 与大 `LinkedList` 使复合谓词从 `true` 变为 `false`。
   - 主子句 b：固定 a 为 `false`、c 为 `true`，用大 `LinkedList` 与大 `ArrayList` 使复合谓词从 `false` 变为 `true`。
   - 主子句 c：固定 a 为 `false`、b 为 `true`，用大 `ArrayList` 搭配 `ArrayList target` 与 `LinkedList target` 使复合谓词从 `true` 变为 `false`。

3. `P-DJ-03`
   - 主子句 a：固定 b 为 `false`，用 `c1=[]` 与 `c1` 非空使复合谓词从 `true` 变为 `false`。
   - 主子句 b：固定 a 为 `false`，用 `c2=[]` 与 `c2` 非空使复合谓词从 `true` 变为 `false`。

## 5. replaceAll 逻辑覆盖测试表

| 用例编号 | 覆盖需求 | 输入 | 正确期望 | 对应测试方法 |
| --- | --- | --- | --- | --- |
| L-RA-01 | P-RA-01 CACC：a 为真、b 为假，复合谓词为真 | 小 `LinkedList ["a","b","a"]`, `oldVal="a"` | 返回 `true`，两个 `"a"` 被替换 | `replaceAllSmallLinkedListMakesSizeClauseDetermineTrue` |
| L-RA-02 | P-RA-01 CACC：a 为假、b 为假，复合谓词为假 | 长度 12 的 `LinkedList`，全部 `"a"` | 返回 `true`，走迭代器路径并全部替换 | `replaceAllLargeLinkedListMakesSizeClauseDetermineFalse` |
| L-RA-03 | P-RA-01 CACC：a 为假、b 为真，复合谓词为真 | 长度 12 的 `ArrayList`，全部 `"a"` | 返回 `true`，仍走索引路径并全部替换 | `replaceAllLargeArrayListMakesRandomAccessClauseDetermineTrue` |
| L-RA-04 | P-RA-02、P-RA-04：`oldVal == null` 为真，空值匹配为真 | `ArrayList [null,"a",null]`, `oldVal=null` | 返回 `true`，两个 `null` 替换为 `"x"` | `replaceAllIndexPathCoversNullOldValuePredicateTrue` |
| L-RA-05 | P-RA-05：普通 equals 匹配为假 | `ArrayList ["a","b","c"]`, `oldVal="z"` | 返回 `false`，列表不变 | `replaceAllIndexPathCoversEqualsPredicateFalse` |
| L-RA-06 | P-RA-03：循环条件为假 | 空 `ArrayList` | 返回 `false` | `replaceAllCoversLoopPredicateFalseWithEmptyList` |
| L-RA-07 | P-RA-06：迭代器空值匹配为假 | 长度 12 的 `LinkedList`，无 `null`，`oldVal=null` | 返回 `false` | `replaceAllIteratorPathCoversNullComparisonFalse` |
| L-RA-08 | P-RA-06：迭代器空值匹配为真 | 长度 12 的 `LinkedList`，包含一个 `null`，`oldVal=null` | 返回 `true`，该 `null` 替换为 `"x"` | `replaceAllIteratorPathCoversNullComparisonTrue` |

## 6. indexOfSubList 逻辑覆盖测试表

| 用例编号 | 覆盖需求 | 输入 | 正确期望 | 对应测试方法 |
| --- | --- | --- | --- | --- |
| L-IS-01 | P-IS-01 CACC：a 为真，复合谓词为真 | 小 `LinkedList [1,2,3,4]`, `target=ArrayList [2,3]` | 返回 `1` | `indexOfSubListSmallLinkedListMakesSizeClauseDetermineTrue` |
| L-IS-02 | P-IS-01 CACC：a 假、b 假、c 真，复合谓词为假 | 长度 40 的 `LinkedList`, `target=ArrayList [20,21]` | 返回 `20` | `indexOfSubListLargeLinkedListMakesSizeClauseDetermineFalse` |
| L-IS-03 | P-IS-01 CACC：a 假、b 真、c 真，复合谓词为真 | 长度 40 的 `ArrayList`, `target=ArrayList [20,21]` | 返回 `20` | `indexOfSubListLargeArrayListMakesSourceRandomAccessClauseTrue` |
| L-IS-04 | P-IS-01 CACC：a 假、b 真、c 假，复合谓词为假 | 长度 40 的 `ArrayList`, `target=LinkedList [20,21]` | 返回 `20` | `indexOfSubListLargeArrayListWithLinkedTargetMakesTargetRandomAccessClauseFalse` |
| L-IS-05 | P-IS-02：候选循环条件为假 | `source=[1]`, `target=[1,2]` | 返回 `-1` | `indexOfSubListCoversCandidateLoopFalseWhenTargetLonger` |
| L-IS-06 | P-IS-03：内层循环条件为假 | `source=[1,2,3]`, `target=[]` | 返回 `0` | `indexOfSubListCoversInnerLoopFalseWithEmptyTarget` |
| L-IS-07 | P-IS-04：`!eq(...)` 为真后继续，随后为假并匹配 | `source=[0,1,2,1,2]`, `target=[1,2]` | 返回 `1` | `indexOfSubListCoversMismatchPredicateInRandomAccessPath` |
| L-IS-08 | P-IS-05：回退循环 `j < i` 为真 | 长度 36 的 `LinkedList`，前部构造部分匹配失败，`target=[0,99]` | 返回 `2` | `indexOfSubListCoversIteratorBacktrackLoopTrue` |
| L-IS-09 | P-IS-06：`eq` 覆盖 `null` 匹配 | `source=["a",null,"c"]`, `target=[null,"c"]` | 返回 `1` | `indexOfSubListCoversEqNullMatch` |
| L-IS-10 | P-IS-02、P-IS-04：迭代器路径全部候选失败 | 长度 36 的 `LinkedList`, `target=ArrayList [1000]` | 返回 `-1` | `indexOfSubListCoversIteratorAllCandidatesFail` |
| L-IS-11 | P-IS-03、P-IS-04：等长完全匹配 | `source=[1,2,3]`, `target=[1,2,3]` | 返回 `0` | `indexOfSubListCoversEqualLengthFullMatch` |

## 7. disjoint 逻辑覆盖测试表

| 用例编号 | 覆盖需求 | 输入 | 正确期望 | 对应测试方法 |
| --- | --- | --- | --- | --- |
| L-DJ-01 | P-DJ-01：`c1 instanceof Set` 为真；P-DJ-05 为假 | `c1=HashSet [1,2]`, `c2=List [3,4]` | 返回 `true` | `disjointCoversFirstCollectionSetPredicateTrueNoOverlap` |
| L-DJ-02 | P-DJ-01：`c1 instanceof Set` 为真；P-DJ-05 为真 | `c1=HashSet [1,2]`, `c2=List [2,3]` | 返回 `false` | `disjointCoversFirstCollectionSetPredicateTrueWithOverlap` |
| L-DJ-03 | P-DJ-02：`!(c2 instanceof Set)` 为假 | `c1=List [1,2]`, `c2=LinkedHashSet [2,3]` | 返回 `false` | `disjointCoversSecondCollectionSetPredicateFalse` |
| L-DJ-04 | P-DJ-02：`!(c2 instanceof Set)` 为真；P-DJ-03 为假 | `c1=List [1,2]`, `c2=List [3,4]` | 返回 `true` | `disjointCoversNeitherCollectionSetNoOverlap` |
| L-DJ-05 | P-DJ-03 CACC：a 为真、b 为假 | `c1=[]`, `c2=[1,2]` | 返回 `true` | `disjointCoversFirstSizeZeroClauseTrue` |
| L-DJ-06 | P-DJ-03 CACC：a 为假、b 为真 | `c1=[1,2]`, `c2=[]` | 返回 `true` | `disjointCoversSecondSizeZeroClauseTrue` |
| L-DJ-07 | P-DJ-04：`c1size > c2size` 为真 | `c1=[1,2,3]`, `c2=[4]` | 返回 `true` | `disjointCoversFirstCollectionLargerPredicateTrue` |
| L-DJ-08 | P-DJ-04：`c1size > c2size` 为假 | `c1=[1]`, `c2=[2,3]` | 返回 `true` | `disjointCoversFirstCollectionLargerPredicateFalse` |
| L-DJ-09 | P-DJ-05：`contains.contains(e)` 对 `null` 为真 | `c1=[null,"a"]`, `c2=["b",null]` | 返回 `false` | `disjointCoversContainsPredicateTrueForNullElement` |

## 8. 运行证据

1. 测试运行结果
   - Surefire 报告：`test/software-testing-final/target/surefire-reports/logic.LogicCoverageTest.txt`
   - 结果摘要：`Tests run: 28, Failures: 0, Errors: 0, Skipped: 0`
   - 测试运行截图：`test/software-testing-final/report/screenshots/stage6-logic-test-build-success.png`

2. JaCoCo 覆盖率结果
   - JaCoCo HTML 报告目录：`test/software-testing-final/report/jacoco-logic/`
   - 类级覆盖率页面：`test/software-testing-final/report/jacoco-logic/sut/CollectionsUnderTest.html`
   - CSV 数据文件：`test/software-testing-final/report/jacoco-logic/jacoco.csv`
   - 指令覆盖率：`100%`，覆盖 `246/246`，未覆盖 `0/246`
   - 分支覆盖率：`98%`，覆盖 `61/62`，未覆盖 `1/62`
   - 行覆盖：覆盖 `61/61`，未覆盖 `0/61`
   - 方法覆盖：覆盖 `4/4`，未覆盖 `0/4`
   - JaCoCo 覆盖率截图：`test/software-testing-final/report/screenshots/stage6-logic-jacoco-coverage.png`
