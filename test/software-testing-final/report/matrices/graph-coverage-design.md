# 任务 2：图覆盖与主路径覆盖测试设计

## 1. 说明

本文件记录任务 2 的主路径覆盖需求和测试用例矩阵。测试对象为正确版本 `sut.CollectionsUnderTest`，测试代码位于 `src/test/java/graph/GraphCoverageTest.java`。

设计依据为本地复制的 OpenJDK `java.util.Collections` 源码，以及根据源码构造的 CFG。详细 CFG 节点、边集合和主路径说明保存在 `report/cfg/graph-coverage-design.md`，Graphviz DOT 源文件保存在 `report/cfg/`。

## 2. 测试用例设计过程

任务 2 采用图覆盖中的主路径覆盖思想。设计过程如下：

1. 识别基本块
   - 阅读 `CollectionsUnderTest.java` 中三个被测方法的源码。
   - 将连续执行且无内部跳转的语句合并为基本块。
   - 将判断、循环、`continue` 和 `return` 作为控制流关键节点。

2. 构造 CFG
   - 为 `replaceAll`、`indexOfSubList`、`disjoint` 分别建立 CFG。
   - 节点表示基本块或判断块。
   - 边表示控制流转移，包含真分支、假分支、循环回边和返回边。

3. 提取主路径
   - 从 CFG 中选择不作为其他简单路径子路径的关键路径。
   - 对包含循环的方法，重点覆盖循环 0 次、1 次和多次，而不是枚举无限路径。
   - 对包含提前返回的方法，单独保留提前返回路径。

4. 映射为测试输入
   - 每条主路径对应一个或多个可执行输入。
   - 测试编号采用 `G-方法缩写-编号`。
   - `G-RA` 表示 `replaceAll`，`G-IS` 表示 `indexOfSubList`，`G-DJ` 表示 `disjoint`。

5. 映射到 JUnit 5 测试方法
   - 在 `GraphCoverageTest` 中通过 `@DisplayName` 保留测试编号。
   - 使用 `assertEquals`、`assertTrue`、`assertFalse` 验证正确版本输出。

## 3. 设计推导摘要

1. `replaceAll`
   - 从源码中的 `size < REPLACEALL_THRESHOLD || list instanceof RandomAccess` 推导出索引路径和迭代器路径。
   - 从 `oldVal == null` 推导出空值比较路径和普通 `equals` 比较路径。
   - 从两个 `for` 循环推导出循环 0 次、无匹配、有匹配和多次匹配。
   - 从 `RandomAccess` 判断推导出“大 ArrayList 仍走索引路径”的测试需求。

2. `indexOfSubList`
   - 从 `sourceSize < INDEXOFSUBLIST_THRESHOLD || both RandomAccess` 推导出随机访问路径和迭代器路径。
   - 从候选起点循环推导出候选循环 0 次、首候选命中、末尾候选命中和全部候选失败。
   - 从内层匹配循环推导出空 `target`、完全匹配和中途失配。
   - 从迭代器路径中的 `si.previous()` 推导出部分匹配失败后的回退路径。

3. `disjoint`
   - 从 `c1 instanceof Set` 和 `c2 instanceof Set` 推导出 Set 优先路径。
   - 从两个非 Set 集合的大小判断推导出空集合提前返回和大小交换路径。
   - 从遍历中的 `contains.contains(e)` 推导出发现公共元素返回 `false` 和遍历结束返回 `true`。
   - 从集合元素允许为 `null` 推导出公共元素为 `null` 的测试需求。

## 4. replaceAll 图覆盖测试表

| 用例编号 | 主路径/覆盖目标 | 输入 | 正确期望 | 对应测试方法 |
| --- | --- | --- | --- | --- |
| G-RA-01 | 索引路径、`oldVal == null`、匹配分支 | `ArrayList [null,"a",null]`, `oldVal=null`, `newVal="x"` | 返回 `true`，列表变为 `["x","a","x"]` | `replaceAllSmallRandomAccessNullOldValueMatch` |
| G-RA-02 | 索引路径、`oldVal != null`、多次匹配 | `ArrayList ["a","b","a"]`, `oldVal="a"`, `newVal="x"` | 返回 `true`，列表变为 `["x","b","x"]` | `replaceAllSmallRandomAccessNonNullMultipleMatches` |
| G-RA-03 | 索引路径、非空旧值、无匹配 | `ArrayList ["a","b","c"]`, `oldVal="z"`, `newVal="x"` | 返回 `false`，列表不变 | `replaceAllSmallRandomAccessNoMatch` |
| G-RA-04 | 循环 0 次，直接返回 | 空 `ArrayList`, `oldVal="a"`, `newVal="x"` | 返回 `false` | `replaceAllEmptyListCoversZeroIteration` |
| G-RA-05 | 非 `RandomAccess` 迭代器路径、`oldVal == null` | 长度 12 的 `LinkedList`，第 3 和第 7 个元素为 `null` | 返回 `true`，两个 `null` 被替换为 `"x"` | `replaceAllLargeLinkedListNullOldValueIteratorPath` |
| G-RA-06 | 非 `RandomAccess` 迭代器路径、`oldVal != null` | 长度 12 的 `LinkedList`，全部元素为 `"a"` | 返回 `true`，全部元素变为 `"x"` | `replaceAllLargeLinkedListNonNullIteratorPath` |
| G-RA-07 | `size >= 11` 但 `RandomAccess` 为真，仍走索引路径 | 长度 12 的 `ArrayList`，全部元素为 `"a"` | 返回 `true`，全部元素变为 `"x"` | `replaceAllLargeArrayListStillUsesRandomAccessPath` |

## 5. indexOfSubList 图覆盖测试表

| 用例编号 | 主路径/覆盖目标 | 输入 | 正确期望 | 对应测试方法 |
| --- | --- | --- | --- | --- |
| G-IS-01 | 随机访问路径，内层循环 0 次 | `source=[1,2,3]`, `target=[]` | 返回 `0` | `indexOfSubListEmptyTargetReturnsZero` |
| G-IS-02 | 随机访问路径，首候选命中 | `source=[1,2,3]`, `target=[1,2]` | 返回 `0` | `indexOfSubListRandomAccessFirstCandidateMatches` |
| G-IS-03 | 随机访问路径，首候选失败后继续 | `source=[0,1,2,1,2]`, `target=[1,2]` | 返回 `1` | `indexOfSubListRandomAccessContinuesAfterMismatch` |
| G-IS-04 | 候选循环 0 次 | `source=[1]`, `target=[1,2]` | 返回 `-1` | `indexOfSubListTargetLongerThanSourceReturnsMinusOne` |
| G-IS-05 | 随机访问路径，全部候选失败 | `source=[1,2,3]`, `target=[4]` | 返回 `-1` | `indexOfSubListRandomAccessAllCandidatesFail` |
| G-IS-06 | 随机访问路径，末尾候选命中 | `source=[1,2,3,4]`, `target=[3,4]` | 返回 `2` | `indexOfSubListRandomAccessLastCandidateMatches` |
| G-IS-07 | 大列表迭代器路径，匹配成功返回 | 长度 40 的 `LinkedList`, `target=[20,21,22]` | 返回 `20` | `indexOfSubListIteratorPathMatches` |
| G-IS-08 | 迭代器路径，部分匹配失败后执行 `si.previous()` 回退 | 长度 36 的 `LinkedList`，前部构造部分匹配后失败，`target=[0,99]` | 返回 `2` | `indexOfSubListIteratorPathBacktracksAfterPartialMismatch` |
| G-IS-09 | 迭代器路径，全部候选失败 | 长度 36 的 `LinkedList`, `target=ArrayList [1000]` | 返回 `-1` | `indexOfSubListIteratorPathAllCandidatesFail` |

## 6. disjoint 图覆盖测试表

| 用例编号 | 主路径/覆盖目标 | 输入 | 正确期望 | 对应测试方法 |
| --- | --- | --- | --- | --- |
| G-DJ-01 | `c1 instanceof Set`，遍历结束无公共元素 | `c1=HashSet [1,2]`, `c2=List [3,4]` | 返回 `true` | `disjointWhenFirstCollectionIsSetAndNoOverlap` |
| G-DJ-02 | `c1 instanceof Set`，发现公共元素 | `c1=HashSet [1,2]`, `c2=List [2,3]` | 返回 `false` | `disjointWhenFirstCollectionIsSetAndOverlapExists` |
| G-DJ-03 | 两个非 Set，`c1size == 0` 提前返回 | `c1=[]`, `c2=[1,2]` | 返回 `true` | `disjointReturnsTrueWhenFirstListIsEmpty` |
| G-DJ-04 | 两个非 Set，`c2size == 0` 提前返回 | `c1=[1,2]`, `c2=[]` | 返回 `true` | `disjointReturnsTrueWhenSecondListIsEmpty` |
| G-DJ-05 | 两个非 Set，`c1.size() > c2.size()` 触发交换 | `c1=[1,2,3]`, `c2=[4]` | 返回 `true` | `disjointSwapsIterationTargetWhenFirstListIsLarger` |
| G-DJ-06 | 两个非 Set，不交换，遍历结束无公共元素 | `c1=[1]`, `c2=[2,3]` | 返回 `true` | `disjointKeepsIterationTargetWhenFirstListIsNotLarger` |
| G-DJ-07 | `c2 instanceof Set`，发现公共元素 | `c1=List [1,2]`, `c2=LinkedHashSet [2,3]` | 返回 `false` | `disjointUsesSecondSetForContainsWhenOverlapExists` |
| G-DJ-08 | 两个非 Set，公共元素为 `null` | `c1=[null,"a"]`, `c2=["b",null]` | 返回 `false` | `disjointDetectsNullCommonElement` |

## 7. 运行证据

1. 测试运行结果
   - Surefire 报告：`test/software-testing-final/target/surefire-reports/graph.GraphCoverageTest.txt`
   - 结果摘要：`Tests run: 24, Failures: 0, Errors: 0, Skipped: 0`
   - 测试运行截图：`test/software-testing-final/report/screenshots/stage5-graph-test-build-success.png`

2. JaCoCo 覆盖率结果
   - JaCoCo HTML 报告目录：`test/software-testing-final/report/jacoco-graph/`
   - 类级覆盖率页面：`test/software-testing-final/report/jacoco-graph/sut/CollectionsUnderTest.html`
   - CSV 数据文件：`test/software-testing-final/report/jacoco-graph/jacoco.csv`
   - 指令覆盖率：`96%`，覆盖 `237/246`，未覆盖 `9/246`
   - 分支覆盖率：`88%`，覆盖 `55/62`，未覆盖 `7/62`
   - 行覆盖：覆盖 `61/61`，未覆盖 `0/61`
   - 方法覆盖：覆盖 `4/4`，未覆盖 `0/4`
   - JaCoCo 覆盖率截图：`test/software-testing-final/report/screenshots/stage5-graph-jacoco-coverage.png`
