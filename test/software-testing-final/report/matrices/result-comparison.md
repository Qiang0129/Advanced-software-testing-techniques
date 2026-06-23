# 不同测试方法结果比较

## 1. 说明

本文件对应期末作业“不同测试方法的结果比较”部分，用于比较任务 1 至任务 4 的测试覆盖率、缺陷发现情况和方法适用性。

比较对象：

1. 输入域划分测试：`domain.DomainBasedTest`
2. 图覆盖测试：`graph.GraphCoverageTest`
3. 逻辑覆盖测试：`logic.LogicCoverageTest`
4. AI 辅助测试：`ai.AIGeneratedCollectionsTest`

缺陷发现证据测试：

```text
test/software-testing-final/src/test/java/comparison/DefectDiscoveryComparisonTest.java
```

## 2. 覆盖率比较

| 测试方法 | 测试类 | 测试数 | 指令覆盖率 | 分支覆盖率 | 行覆盖 | 方法覆盖 | JaCoCo 报告目录 |
| --- | --- | ---: | ---: | ---: | ---: | ---: | --- |
| 输入域划分测试 | `DomainBasedTest` | 34 | 90% (`222/246`) | 80% (`50/62`) | `56/61` | `4/4` | `report/jacoco-domain/` |
| 图覆盖测试 | `GraphCoverageTest` | 24 | 96% (`237/246`) | 88% (`55/62`) | `61/61` | `4/4` | `report/jacoco-graph/` |
| 逻辑覆盖测试 | `LogicCoverageTest` | 28 | 100% (`246/246`) | 98% (`61/62`) | `61/61` | `4/4` | `report/jacoco-logic/` |
| AI 辅助测试 | `AIGeneratedCollectionsTest` | 20 | 73% (`181/246`) | 64% (`40/62`) | `46/61` | `4/4` | `report/jacoco-ai/` |

覆盖率结论：

1. 逻辑覆盖测试的覆盖率最高，指令覆盖率达到 `100%`，分支覆盖率达到 `98%`。
2. 图覆盖测试用例数量少于输入域测试，但行覆盖达到 `61/61`，说明 CFG 和主路径分析更直接覆盖源码结构。
3. 输入域划分测试覆盖率较高，能够覆盖大量边界值和输入类别，但对部分内部结构路径覆盖不如图覆盖和逻辑覆盖。
4. AI 辅助测试的测试数最少，能覆盖常见语义场景，但 `indexOfSubList` 的内部路径覆盖不足，导致整体覆盖率最低。

## 3. 缺陷发现情况比较

| 测试方法 | F1 `replaceAll` 空值处理缺陷 | F2 `indexOfSubList` 末尾候选缺陷 | F3 `disjoint` 交集判断缺陷 | 结论 |
| --- | --- | --- | --- | --- |
| 输入域划分测试 | 发现：`D-RA-04` | 发现：`D-IS-05`、`D-IS-09` | 发现：`D-DJ-02`、`D-DJ-05`、`D-DJ-06`、`D-DJ-08`、`D-DJ-09`、`D-DJ-11` | 三个缺陷均可发现 |
| 图覆盖测试 | 发现：`G-RA-01`、`G-RA-05` | 发现：`G-IS-06` | 发现：`G-DJ-02`、`G-DJ-07`、`G-DJ-08` | 三个缺陷均可发现 |
| 逻辑覆盖测试 | 发现：`L-RA-04`、`L-RA-08` | 发现：`L-IS-11` | 发现：`L-DJ-02`、`L-DJ-03`、`L-DJ-09` | 三个缺陷均可发现 |
| AI 辅助测试 | 发现：`AI-RA-03` | 发现：`AI-IS-03` | 发现：`AI-DJ-02`、`AI-DJ-05`、`AI-DJ-06` | 三个缺陷均可发现 |

缺陷发现结论：

1. 四类测试方法都能发现三个植入缺陷。
2. F1 依赖 `oldVal == null` 输入，输入域、逻辑覆盖和 AI 测试都能自然生成该边界；图覆盖通过 `oldVal == null` 分支路径覆盖发现该缺陷。
3. F2 依赖最后候选起点或等长完全匹配边界，输入域和 AI 测试通过边界值发现，图覆盖和逻辑覆盖通过候选循环边界路径发现。
4. F3 依赖存在公共元素的输入，四类测试都包含有交集场景，因此均能发现 faulty 版本将结果写反的问题。

## 4. 缺陷发现证据测试

证据测试类 `DefectDiscoveryComparisonTest` 使用 12 个测试方法，每类测试方法对应 3 个缺陷。测试方式是同时调用正确版本 `CollectionsUnderTest` 和对应 faulty 类，确认 faulty 行为与正确期望不一致。

运行命令：

```powershell
cd "D:\Desktop\高级软件测试技术课程作业\test\software-testing-final"
mvn "-Dtest=comparison.DefectDiscoveryComparisonTest" test
```

实际结果：

```text
Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.089 s -- in comparison.DefectDiscoveryComparisonTest
```

截图保存路径：

```text
test/software-testing-final/report/screenshots/stage8-defect-discovery-comparison.png
```

## 5. 方法优缺点和适用场景

| 测试方法 | 优点 | 不足 | 适用场景 |
| --- | --- | --- | --- |
| 输入域划分测试 | 从 JavaDoc 和参数语义出发，容易覆盖等价类、边界值和异常输入 | 对源码内部路径、短路逻辑和隐藏分支不够敏感 | 需求和接口语义清晰，源码不一定完全可见的场景 |
| 图覆盖测试 | 直接面向源码 CFG，能系统覆盖分支、循环和返回路径 | 需要手工构造 CFG，分析成本高，复杂方法容易遗漏路径 | 源码可见、需要证明路径覆盖充分性的场景 |
| 逻辑覆盖测试 | 对复合谓词、短路求值和条件边界最敏感，本实验覆盖率最高 | 谓词提取和 CACC 配对成本最高，部分子句组合可能不可行 | 条件判断复杂、需要精细验证布尔逻辑的场景 |
| AI 辅助测试 | 生成速度快，能快速补充常见正常、边界和异常用例 | 覆盖目标不系统，可能遗漏内部路径，必须人工审查和修正 | 快速起草测试、补充候选用例或做测试设计辅助的场景 |
