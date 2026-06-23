# 任务 1：输入域划分与基本选择覆盖测试设计

## 1. 说明

本文件记录任务 1 的输入域划分、边界值分析和基本选择覆盖测试用例。测试对象为正确版本 `sut.CollectionsUnderTest`，测试代码位于 `src/test/java/domain/DomainBasedTest.java`。

语义依据为作业 PDF 指定的 Java SE 8 JavaDoc；实现依据为本地复制的 OpenJDK `java.util.Collections` 源码。

## 2. 测试用例设计过程

任务 1 采用输入域划分、边界值分析和基本选择覆盖。设计过程如下：

1. 查阅 JavaDoc
   - 先从 JavaDoc 获取方法语义，而不是直接按源码分支罗列测试。
   - 记录每个方法的参数含义、返回值含义和可能异常。
   - `replaceAll`：关注是否替换成功、替换后列表状态、列表不可修改时的异常。
   - `indexOfSubList`：关注返回第一次匹配位置、找不到时返回 `-1`、`target` 为空时返回 `0`。
   - `disjoint`：关注两个集合是否存在公共元素，返回值含义为“是否不相交”。

2. 提取输入特征
   - `replaceAll`：列表类型、列表大小、`oldVal`、`newVal`、匹配次数、列表是否可修改。
   - `indexOfSubList`：`source` 类型、`target` 类型、长度关系、匹配位置、元素是否包含 `null`、是否触发非 `RandomAccess` 路径。
   - `disjoint`：集合类型、集合大小、两个集合的关系、元素是否包含 `null`、是否为同一集合对象。

3. 进行区块划分
   - 每个输入特征划分为普通有效区块和边界区块。
   - 普通有效区块用于构造基本选择值。
   - 边界区块用于构造空集合、`null` 元素、末尾匹配、不可修改列表、同一集合对象等测试需求。

4. 选择基本选择值
   - `replaceAll` 基本选择值：可修改 `ArrayList`、非空列表、普通 `oldVal`、普通 `newVal`、多处匹配。
   - `indexOfSubList` 基本选择值：普通列表，`target` 在 `source` 中间出现一次。
   - `disjoint` 基本选择值：两个非空 `List`，且没有公共元素。

5. 生成基本选择覆盖测试
   - 每次只替换一个非基本区块或边界区块，形成测试需求编号。
   - 例如 `replaceAll` 从基本选择 `D-RA-01` 出发，分别替换为空列表、无匹配、`oldVal == null`、`newVal == null`、`LinkedList`、不可修改列表。
   - 例如 `indexOfSubList` 从中间匹配出发，分别替换为空 `target`、`target` 长于 `source`、开头匹配、末尾匹配、不匹配、包含 `null`、`LinkedList` 阈值路径。
   - 例如 `disjoint` 从无交集出发，分别替换为有交集、空集合、`Set` 类型、大小交换、`null` 公共元素、同一集合对象。

6. 映射到 JUnit 5 测试方法
   - 每条测试需求使用 `D-方法缩写-编号` 命名。
   - `D-RA` 表示 `replaceAll`，`D-IS` 表示 `indexOfSubList`，`D-DJ` 表示 `disjoint`。
   - 在 `DomainBasedTest` 中通过 `@DisplayName` 保留测试需求编号。
   - 用 `assertEquals`、`assertTrue`、`assertFalse`、`assertThrows` 检查返回值、列表状态和异常行为。

## 3. 设计推导摘要

1. `replaceAll`
   - 从“替换所有等于 `oldVal` 的元素”推导出匹配次数区块：0 次、1 次、多次。
   - 从 Java 集合允许元素为 `null` 推导出 `oldVal == null` 和 `newVal == null` 的边界区块。
   - 从方法会修改传入列表推导出可修改列表和不可修改列表两个区块。
   - 从本地 OpenJDK 实现中的阈值和 `RandomAccess` 判断补充 `ArrayList` 与超过阈值的 `LinkedList`。

2. `indexOfSubList`
   - 从“返回第一次完整出现的位置”推导出开头、中间、末尾和不存在四种匹配位置。
   - 从 JavaDoc 对空 `target` 的语义推导出 `target` 为空时返回 `0`。
   - 从长度约束推导出 `target` 长于 `source` 时返回 `-1`。
   - 从元素比较语义推导出包含 `null` 元素时仍应正确匹配。
   - 从本地 OpenJDK 实现中的阈值和 `RandomAccess` 判断补充达到阈值的 `LinkedList`。

3. `disjoint`
   - 从“不相交”语义推导出无公共元素返回 `true`，存在公共元素返回 `false`。
   - 从集合大小边界推导出第一个集合为空、第二个集合为空。
   - 从源码中对 `Set` 的特殊处理推导出 `c1` 为 `Set` 和 `c2` 为 `Set` 的测试区块。
   - 从源码中根据大小交换迭代对象的逻辑推导出两个非 `Set` 且 `c1.size() > c2.size()` 的测试区块。
   - 从集合元素允许为 `null` 和集合对象可能相同推导出 `null` 公共元素和同一非空集合对象。

运行命令：

```powershell
cd test/software-testing-final
Remove-Item -Recurse -Force "report\jacoco-domain" -ErrorAction SilentlyContinue
mvn -Pdomain clean test jacoco:report
```

## 4. replaceAll 输入域

输入特征：

1. 列表类型：`ArrayList`、`LinkedList`、不可修改列表。
2. 列表大小：空列表、单元素、小于阈值、等于 `REPLACEALL_THRESHOLD=11`、超过阈值。
3. `oldVal`：普通值、`null`、不存在于列表中。
4. `newVal`：普通值、`null`。
5. 匹配次数：0 次、1 次、多次。
6. 修改能力：可修改、不可修改。

基本选择值：

1. `ArrayList`、非空、普通旧值、普通新值、多处匹配、可修改。

边界值：

1. 空列表。
2. 单元素列表。
3. `oldVal == null`。
4. `newVal == null`。
5. 长度等于 11 的 `LinkedList`。
6. 长度大于 11 的 `LinkedList`。
7. 不可修改列表中存在匹配元素。

| 用例编号 | 输入域/边界点 | 输入 | 正确期望 | 对应测试方法 |
| --- | --- | --- | --- | --- |
| D-RA-01 | 基本选择：普通值多处匹配 | `["a","b","a"]`, `oldVal="a"`, `newVal="x"` | 返回 `true`，列表变为 `["x","b","x"]` | `replaceAllBaseChoiceMultipleMatches` |
| D-RA-02 | 空列表边界 | `[]`, `oldVal="a"`, `newVal="x"` | 返回 `false`，列表仍为空 | `replaceAllReturnsFalseForEmptyList` |
| D-RA-03 | 无匹配元素 | `["a","b","c"]`, `oldVal="z"` | 返回 `false`，列表不变 | `replaceAllReturnsFalseWhenNoElementMatches` |
| D-RA-04 | `oldVal == null` | `[null,"b",null]`, `newVal="x"` | 返回 `true`，列表变为 `["x","b","x"]` | `replaceAllHandlesNullOldValue` |
| D-RA-05 | `newVal == null` | `["a","b","a"]`, `oldVal="a"` | 返回 `true`，列表变为 `[null,"b",null]` | `replaceAllHandlesNullNewValue` |
| D-RA-06 | 非 `RandomAccess` 且长度大于阈值 | 12 个 `"a"` 的 `LinkedList` | 返回 `true`，12 个元素全部变为 `"x"` | `replaceAllUsesIteratorPathForLargeLinkedList` |
| D-RA-07 | 不可修改列表且存在匹配 | `List.of("a","b")`, `oldVal="a"` | 抛出 `UnsupportedOperationException` | `replaceAllThrowsForUnmodifiableListWhenReplacementIsNeeded` |
| D-RA-08 | 单元素列表且匹配 | `["a"]`, `oldVal="a"`, `newVal="x"` | 返回 `true`，列表变为 `["x"]` | `replaceAllHandlesSingleElementMatch` |
| D-RA-09 | 非 `RandomAccess` 且长度等于阈值 | 11 个 `"a"` 的 `LinkedList` | 返回 `true`，11 个元素全部变为 `"x"` | `replaceAllHandlesLinkedListAtThreshold` |
| D-RA-10 | 只有一次匹配 | `["a","b","c"]`, `oldVal="b"`, `newVal="x"` | 返回 `true`，列表变为 `["a","x","c"]` | `replaceAllHandlesSingleMatch` |

## 5. indexOfSubList 输入域

输入特征：

1. 列表类型：`ArrayList`/`List.of`、`LinkedList`、混合列表类型。
2. 长度关系：`target` 为空、`target` 长于 `source`、`target` 等长、`target` 短于 `source`。
3. 匹配位置：开头、中间、末尾、不存在。
4. 匹配形态：完全匹配、部分前缀匹配后失败、包含 `null` 元素匹配。
5. 阈值路径：`source.size() < 35`、`source.size() == 34`、`source.size() == 35`、`source.size() > 35`。

基本选择值：

1. `source` 和 `target` 为普通列表，`target` 在 `source` 中间出现一次。

边界值：

1. `target` 为空。
2. `target` 长于 `source`。
3. 匹配在第一个候选位置。
4. 匹配在最后一个候选位置。
5. 包含 `null` 元素。
6. `source` 与 `target` 等长。
7. 部分前缀匹配后失败。
8. `sourceSize == 34`。
9. `sourceSize == 35` 且混合列表类型。
10. `LinkedList` 长度达到 `INDEXOFSUBLIST_THRESHOLD=35` 以上。

| 用例编号 | 输入域/边界点 | 输入 | 正确期望 | 对应测试方法 |
| --- | --- | --- | --- | --- |
| D-IS-01 | 基本选择：目标在中间 | `[1,2,3,4]`, `[2,3]` | 返回 `1` | `indexOfSubListFindsMiddleMatch` |
| D-IS-02 | `target` 为空 | `[1,2,3]`, `[]` | 返回 `0` | `indexOfSubListReturnsZeroForEmptyTarget` |
| D-IS-03 | `target` 长于 `source` | `[1,2]`, `[1,2,3]` | 返回 `-1` | `indexOfSubListReturnsNegativeOneWhenTargetLongerThanSource` |
| D-IS-04 | 匹配在开头 | `[1,2,3,4]`, `[1,2]` | 返回 `0` | `indexOfSubListFindsPrefixMatch` |
| D-IS-05 | 匹配在末尾 | `[1,2,3,4]`, `[3,4]` | 返回 `2` | `indexOfSubListFindsSuffixMatch` |
| D-IS-06 | 不存在匹配 | `[1,2,3,4]`, `[2,4]` | 返回 `-1` | `indexOfSubListReturnsNegativeOneWhenNoMatchExists` |
| D-IS-07 | 包含 `null` 的匹配 | `["a",null,"c"]`, `[null,"c"]` | 返回 `1` | `indexOfSubListHandlesNullElements` |
| D-IS-08 | 非 `RandomAccess` 且长度达到阈值 | 0 到 39 的 `LinkedList`, `[35,36,37]` | 返回 `35` | `indexOfSubListUsesIteratorPathForLargeLinkedList` |
| D-IS-09 | `source` 与 `target` 等长且完全匹配 | `[1,2,3]`, `[1,2,3]` | 返回 `0` | `indexOfSubListFindsEqualLengthMatch` |
| D-IS-10 | 部分前缀匹配后失败 | `[1,2,9,1,2,3]`, `[1,2,3]` | 返回 `3` | `indexOfSubListHandlesPartialPrefixMatchFailure` |
| D-IS-11 | `sourceSize == 34` | 0 到 33 的 `ArrayList`, `[31,32,33]` | 返回 `31` | `indexOfSubListHandlesSourceSizeJustBelowThreshold` |
| D-IS-12 | `sourceSize == 35` 且混合列表类型 | 0 到 34 的 `LinkedList`, `ArrayList [32,33,34]` | 返回 `32` | `indexOfSubListHandlesThresholdWithMixedListTypes` |

## 6. disjoint 输入域

输入特征：

1. 集合类型：两个非 `Set` 集合、`c1` 为 `Set`、`c2` 为 `Set`、两个集合均为 `Set`。
2. 集合大小：两者都为空、一方为空、两者均非空、两个非 `Set` 时 `c1.size() > c2.size()`。
3. 集合关系：无交集、有一个公共元素、多个公共元素、同一集合对象。
4. 元素情况：普通值、`null`。

基本选择值：

1. 两个非空列表且无公共元素。

边界值：

1. `c1` 为空。
2. `c2` 为空。
3. 两个集合都为空。
4. 存在一个公共元素。
5. 存在多个公共元素。
6. 公共元素为 `null`。
7. 同一非空集合对象。
8. 两个集合均为 `Set`。

| 用例编号 | 输入域/边界点 | 输入 | 正确期望 | 对应测试方法 |
| --- | --- | --- | --- | --- |
| D-DJ-01 | 基本选择：无交集 | `[1,2]`, `[3,4]` | 返回 `true` | `disjointReturnsTrueWhenCollectionsDoNotOverlap` |
| D-DJ-02 | 存在公共元素 | `[1,2]`, `[2,3]` | 返回 `false` | `disjointReturnsFalseWhenCollectionsOverlap` |
| D-DJ-03 | 第一个集合为空 | `[]`, `[1,2]` | 返回 `true` | `disjointReturnsTrueWhenFirstCollectionIsEmpty` |
| D-DJ-04 | 第二个集合为空 | `[1,2]`, `[]` | 返回 `true` | `disjointReturnsTrueWhenSecondCollectionIsEmpty` |
| D-DJ-05 | `c1` 为 `Set` 且存在公共元素 | `{1,2}`, `[2,3]` | 返回 `false` | `disjointUsesFirstSetAsContainsCollection` |
| D-DJ-06 | `c2` 为 `Set` 且存在公共元素 | `[1,2]`, `{2,3}` | 返回 `false` | `disjointUsesSecondSetAsContainsCollection` |
| D-DJ-07 | 两个非 `Set` 且 `c1.size() > c2.size()` | `[1,2,3,4]`, `[4]` | 返回 `false` | `disjointIteratesSmallerCollectionWhenNeitherCollectionIsSet` |
| D-DJ-08 | 公共元素为 `null` | `[null,"a"]`, `["b",null]` | 返回 `false` | `disjointHandlesNullCommonElement` |
| D-DJ-09 | 同一非空集合对象 | 同一个 `["a","b"]` 同时作为 `c1` 和 `c2` | 返回 `false` | `disjointReturnsFalseForSameNonEmptyCollectionObject` |
| D-DJ-10 | 两个集合都为空 | `[]`, `[]` | 返回 `true` | `disjointReturnsTrueWhenBothCollectionsAreEmpty` |
| D-DJ-11 | 存在多个公共元素 | `[1,2,3]`, `[2,3,4]` | 返回 `false` | `disjointReturnsFalseWhenMultipleElementsOverlap` |
| D-DJ-12 | `Set` 和 `Set` 组合且无交集 | `{1,2}`, `{3,4}` | 返回 `true` | `disjointHandlesTwoSetsWithoutOverlap` |

## 7. 与植入缺陷的关系

1. F1 `FaultyReplaceAll` 空值处理缺陷：`D-RA-04` 覆盖 `oldVal == null` 且列表包含 `null` 的输入。
2. F2 `FaultyIndexOfSubList` 末尾候选边界缺陷：`D-IS-05` 覆盖匹配位于最后一个候选起点的输入。
3. F3 `FaultyDisjoint` 交集判断结果写反缺陷：`D-DJ-02`、`D-DJ-05`、`D-DJ-06`、`D-DJ-07`、`D-DJ-08`、`D-DJ-09`、`D-DJ-11` 覆盖存在公共元素的输入。

## 8. 运行证据

1. 测试运行截图：`test/software-testing-final/report/screenshots/stage4-domain-test-build-success.png`
2. JaCoCo 覆盖率截图：`test/software-testing-final/report/screenshots/stage4-domain-jacoco-coverage.png`
3. JaCoCo HTML 报告目录：`test/software-testing-final/report/jacoco-domain/`
4. 测试运行结果：`Tests run: 34, Failures: 0, Errors: 0, Skipped: 0`
5. `CollectionsUnderTest` 覆盖率：指令覆盖率 `90%`，分支覆盖率 `80%`，行覆盖 `56/61`，方法覆盖 `4/4`
