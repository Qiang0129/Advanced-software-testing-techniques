# 高级软件测试技术期末报告README

本 Maven 工程用于集中管理被测方法源码、缺陷版本、JUnit 5 测试用例、JaCoCo 覆盖率报告、CFG 图、测试矩阵和实验截图。（仓库地址：[https://github.com/Qiang0129/Advanced-software-testing-techniques）](https://github.com/Qiang0129/Advanced-software-testing-techniques%EF%BC%89)

当前工程已经完成任务 1 至任务 4，以及不同测试方法的覆盖率和缺陷发现结果比较。

## 目录结构

```text
software-testing-final/
|-- pom.xml                                      Maven 工程配置，管理 JUnit 5、JaCoCo 和测试分组 profile
|-- README.md                                    工程说明文档
|-- src/
|   |-- main/
|   |   |-- java/
|   |   |   |-- sut/
|   |   |   |   |-- CollectionsUnderTest.java       正确版本被测类，本地化 OpenJDK Collections 源码
|   |   |   |   |-- faulty/
|   |   |   |   |   |-- FaultyReplaceAll.java          replaceAll 的单缺陷版本
|   |   |   |   |   |-- FaultyIndexOfSubList.java      indexOfSubList 的单缺陷版本
|   |   |   |   |   |-- FaultyDisjoint.java            disjoint 的单缺陷版本
|   |-- test/
|   |   |-- java/
|   |   |   |-- sanity/
|   |   |   |   |-- SanityTest.java                正确版本基线冒烟测试
|   |   |   |   |-- FaultySanityTest.java          三个 faulty 缺陷触发验证
|   |   |   |-- domain/
|   |   |   |   |-- DomainBasedTest.java           任务 1：输入域划分 / 基本选择覆盖测试
|   |   |   |-- graph/
|   |   |   |   |-- GraphCoverageTest.java         任务 2：CFG / 主路径覆盖测试
|   |   |   |-- logic/
|   |   |   |   |-- LogicCoverageTest.java         任务 3：谓词覆盖 / CACC 测试
|   |   |   |-- ai/
|   |   |   |   |-- AIGeneratedCollectionsTest.java 任务 4：AI 辅助测试
|   |   |   |-- comparison/
|   |   |   |   |-- DefectDiscoveryComparisonTest.java 不同测试方法缺陷发现证据测试
|   |   |-- resources/
|-- report/
|   |-- ai/                                      AI 提示词、原始输出和人工修正记录
|   |-- cfg/                                     CFG 设计说明、DOT 源文件和 CFG 图片
|   |-- screenshots/                             测试运行、覆盖率和缺陷发现截图
|   |-- jacoco-domain/                           输入域测试 JaCoCo 报告
|   |-- jacoco-graph/                            图覆盖测试 JaCoCo 报告
|   |-- jacoco-logic/                            逻辑覆盖测试 JaCoCo 报告
|   |-- jacoco-ai/                               AI 测试 JaCoCo 报告
|   |-- matrices/                                测试设计矩阵、缺陷验证表和结果比较表
|-- target/                                      Maven 编译和测试输出目录
```

## 被测方法与源码来源

本次作业选择 `java.util.Collections` 中的三个静态方法作为被测对象：

1. `replaceAll(List<T> list, T oldVal, T newVal)`
2. `indexOfSubList(List<?> source, List<?> target)`
3. `disjoint(Collection<?> c1, Collection<?> c2)`

正确版本源码放在：

```text
src/main/java/sut/CollectionsUnderTest.java
```

源码来源信息：

1. 仓库：`openjdk/jdk`
2. 分支：`master`
3. 文件路径：`src/java.base/share/classes/java/util/Collections.java`
4. PDF 指定目录链接：`https://github.com/openjdk/jdk/tree/master/src/java.base/share/classes/java/util`
5. Raw 源码链接：`https://raw.githubusercontent.com/openjdk/jdk/master/src/java.base/share/classes/java/util/Collections.java`
6. 下载日期：`2026-06-22`
7. commit：`2e179fec7b5113a3b526ee4ad5c66d6b7f0179e2`

将标准类源码复制到本地 SUT 的目的，是让 JaCoCo 能统计三个被测方法内部的行覆盖率、分支覆盖率和指令覆盖率。

## 缺陷版本说明

| 缺陷类                   | 对应方法           | 缺陷类型              | 植入方式                                      | 缺陷影响                                                 |
| ------------------------ | ------------------ | --------------------- | --------------------------------------------- | -------------------------------------------------------- |
| `FaultyReplaceAll`     | `replaceAll`     | 空值处理缺陷          | 使用`oldVal.equals(...)` 判断元素是否匹配   | 当`oldVal == null` 时触发 `NullPointerException`     |
| `FaultyIndexOfSubList` | `indexOfSubList` | 边界错误 / off-by-one | 候选起点循环漏掉`candidate == maxCandidate` | 当`target` 正好出现在 `source` 末尾时错误返回 `-1` |
| `FaultyDisjoint`       | `disjoint`       | 逻辑判断错误          | 发现公共元素时返回`true`                    | 两个集合存在公共元素时仍被错误判断为不相交               |

## 测试集与运行结果

| 阶段                     | 测试类                            | 测试数量 | JaCoCo 报告               | 覆盖率                                                         | 运行结果                                                                                                         |
| ------------------------ | --------------------------------- | -------- | ------------------------- | -------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------- |
| 基线与缺陷触发验证       | `SanityTest`                    | -        | -                         | -                                                              | 正确版本基线冒烟测试                                                                                             |
| 基线与缺陷触发验证       | `FaultySanityTest`              | 4        | -                         | -                                                              | `Tests run: 4, Failures: 0, Errors: 0, Skipped: 0`，验证 F1、F2、F3 均可被关键输入触发                         |
| 任务 1：输入域划分测试   | `DomainBasedTest`               | 34       | `report/jacoco-domain/` | 指令`90%`，分支 `80%`，行覆盖 `56/61`，方法覆盖 `4/4`  | -                                                                                                                |
| 任务 2：图覆盖测试       | `GraphCoverageTest`             | 24       | `report/jacoco-graph/`  | 指令`96%`，分支 `88%`，行覆盖 `61/61`，方法覆盖 `4/4`  | -                                                                                                                |
| 任务 3：逻辑覆盖测试     | `LogicCoverageTest`             | 28       | `report/jacoco-logic/`  | 指令`100%`，分支 `98%`，行覆盖 `61/61`，方法覆盖 `4/4` | -                                                                                                                |
| 任务 4：AI 辅助测试      | `AIGeneratedCollectionsTest`    | 20       | `report/jacoco-ai/`     | 指令`73%`，分支 `64%`，行覆盖 `46/61`，方法覆盖 `4/4`  | -                                                                                                                |
| 不同测试方法缺陷发现比较 | `DefectDiscoveryComparisonTest` | 12       | -                         | -                                                              | `Tests run: 12, Failures: 0, Errors: 0, Skipped: 0`，输入域、图覆盖、逻辑覆盖和 AI 辅助测试均能发现 F1、F2、F3 |

## 常用命令

运行全部测试：

```powershell
mvn test
```

运行缺陷触发验证：

```powershell
mvn "-Dtest=sanity.FaultySanityTest" test
```

按任务分组运行并导出 JaCoCo 报告：

```powershell
mvn -Pdomain clean test jacoco:report
mvn -Pgraph clean test jacoco:report
mvn -Plogic clean test jacoco:report
mvn -Pai clean test jacoco:report
```

运行不同测试方法缺陷发现比较：

```powershell
mvn "-Dtest=comparison.DefectDiscoveryComparisonTest" test
```

生成 CFG 图片：

```powershell
dot -Tpng report\cfg\replaceAll-cfg.dot -o report\cfg\replaceAll-cfg.png
dot -Tpng report\cfg\indexOfSubList-cfg.dot -o report\cfg\indexOfSubList-cfg.png
dot -Tpng report\cfg\disjoint-cfg.dot -o report\cfg\disjoint-cfg.png
```

## 报告材料说明

1. `report/cfg`

   - 保存三个方法的 CFG 设计说明、Graphviz DOT 源文件和 PNG 图片。
2. `report/screenshots`

   - 保存阶段 2 至阶段 8 的测试运行截图、覆盖率截图和缺陷发现比较截图。
3. `report/jacoco-domain`、`report/jacoco-graph`、`report/jacoco-logic`、`report/jacoco-ai`

   - 分别保存四类测试集的 JaCoCo HTML、XML 和 CSV 报告。
4. `report/matrices`

   - `domain-input-domain.md`：任务 1 输入域划分设计矩阵
   - `graph-coverage-design.md`：任务 2 图覆盖测试矩阵
   - `logic-coverage-design.md`：任务 3 逻辑覆盖测试矩阵
   - `ai-generated-test-design.md`：任务 4 AI 测试设计矩阵
   - `faulty-sanity-verification.md`：缺陷植入有效性验证表
   - `result-comparison.md`：覆盖率和缺陷发现结果比较表
5. `report/ai`

   - 保存 AI 提示词、AI 原始输出和人工审查修正记录。

