# software-testing-final

本 Maven 工程用于“高级软件测试技术”期末作业，集中管理被测方法、缺陷版本、JUnit 测试用例、覆盖率报告和实验截图。

## 目录结构

```text
）software-testing-final/
|-- pom.xml                         Maven 工程配置，管理 JUnit 5、JaCoCo 和测试分组 profile
|-- README.md                       工程说明文档，记录目录用途、运行命令和后续补充内容
|-- src/
|   |-- main/
|   |   |-- java/
|   |   |   |-- sut/
|   |   |   |   |-- CollectionsUnderTest.java          正确版本被测类
|   |   |   |   |-- faulty/
|   |   |   |   |   |-- FaultyReplaceAll.java             replaceAll 的单缺陷版本
|   |   |   |   |   |-- FaultyIndexOfSubList.java         indexOfSubList 的单缺陷版本
|   |   |   |   |   |-- FaultyDisjoint.java               disjoint 的单缺陷版本
|   |-- test/
|   |   |-- java/
|   |   |   |-- sanity/
|   |   |   |   |-- SanityTest.java                   基线冒烟测试
|   |   |   |-- domain/
|   |   |   |   |-- DomainBasedTest.java              输入域划分测试
|   |   |   |-- graph/
|   |   |   |   |-- GraphCoverageTest.java            CFG 与主路径覆盖测试
|   |   |   |-- logic/
|   |   |   |   |-- LogicCoverageTest.java            谓词覆盖与 CACC 测试
|   |   |   |-- ai/
|   |   |   |   |-- AIGeneratedCollectionsTest.java   AI 生成测试
|   |   |-- resources/                 测试资源目录，后续可放测试数据或配置文件
|-- report/
|   |-- cfg/                         保存三个方法的控制流图
|   |-- screenshots/                 保存测试运行、覆盖率和缺陷暴露截图
|   |-- jacoco-domain/               保存输入域划分测试的 JaCoCo 报告
|   |-- jacoco-graph/                保存图覆盖测试的 JaCoCo 报告
|   |-- jacoco-logic/                保存逻辑覆盖测试的 JaCoCo 报告
|   |-- jacoco-ai/                   保存 AI 生成测试的 JaCoCo 报告
|   |-- matrices/                    保存覆盖率比较表和缺陷发现矩阵
|   |-- ai/                          保存 AI 提示词、原始输出和人工修正记录
|-- target/                         编译和测试产生的临时输出目录（最后可删除）
|   |-- classes/                     Java 主源码编译后的 .class 文件
|   |-- smoke/                       临时冒烟验证程序的编译输出
```

## 代码文件说明

1. `src/main/java/sut/CollectionsUnderTest.java`

   - 正确版本的被测类，统一放置本次作业选择的三个标准方法：`replaceAll`、`indexOfSubList`、`disjoint`。
   - 当前已经使用作业 PDF 指定的 OpenJDK 源码链接完成本地化，不再委托调用 `java.util.Collections`。
   - 后续 JaCoCo 可以直接统计这三个方法内部的行覆盖率和分支覆盖率。
2. `src/main/java/sut/faulty/FaultyReplaceAll.java`

   - `replaceAll` 的缺陷版本。
   - 当前缺陷点是破坏 `oldVal == null` 时的空值处理逻辑，使用 `oldVal.equals(...)` 会在 `oldVal` 为 `null` 时触发异常。
   - 后续用于验证输入域测试和逻辑覆盖测试是否能发现空值相关缺陷。
3. `src/main/java/sut/faulty/FaultyIndexOfSubList.java`

   - `indexOfSubList` 的缺陷版本。
   - 当前缺陷点是候选起点循环使用 `candidate < maxCandidate`，漏掉 `candidate == maxCandidate` 的最后一个合法位置。
   - 后续用于验证测试集是否覆盖“target 正好出现在 source 末尾”的边界场景。
4. `src/main/java/sut/faulty/FaultyDisjoint.java`

   - `disjoint` 的缺陷版本。
   - 当前缺陷点是发现公共元素时仍返回 `true`，把“有交集”的判断结果写反。
   - 后续用于验证测试集是否覆盖两个集合存在公共元素的失败场景。
5. `src/test/java/sanity/SanityTest.java`

   - 基线冒烟测试，用于确认正确版本的三个方法具备基本行为。
   - 该测试不属于正式覆盖准则分析，主要用于快速发现工程配置或基本实现错误。
6. `src/test/java/domain/DomainBasedTest.java`

   - 输入域划分测试类。
   - 后续按照基本选择覆盖补全 `replaceAll`、`indexOfSubList`、`disjoint` 的输入域区块、基本值和边界值用例。
7. `src/test/java/graph/GraphCoverageTest.java`

   - 图覆盖测试类。
   - 后续根据三个方法的 CFG、节点含义、边集合和主路径需求补全测试用例。
8. `src/test/java/logic/LogicCoverageTest.java`

   - 逻辑覆盖测试类。
   - 后续根据谓词覆盖、子句覆盖和相关性有效子句覆盖（CACC）补全测试用例。
9. `src/test/java/ai/AIGeneratedCollectionsTest.java`

   - AI 生成测试类。
   - 后续用于保存 AI 生成或经最小修正后的 JUnit 测试，并与人工设计测试集分开统计覆盖率和缺陷发现情况。

## 当前状态

`CollectionsUnderTest` 已经将 `replaceAll`、`indexOfSubList`、`disjoint` 三个方法复制为本地源码实现，便于后续用 JaCoCo 统计被测源码的行覆盖率和分支覆盖率。

源码来源信息：

1. 仓库：`openjdk/jdk`
2. 分支：`master`
3. 文件路径：`src/java.base/share/classes/java/util/Collections.java`
4. PDF 指定目录链接：`https://github.com/openjdk/jdk/tree/master/src/java.base/share/classes/java/util`
5. Raw 源码链接：`https://raw.githubusercontent.com/openjdk/jdk/master/src/java.base/share/classes/java/util/Collections.java`
6. 下载日期：`2026-06-22`
7. commit：`2e179fec7b5113a3b526ee4ad5c66d6b7f0179e2`

`sut.faulty` 包下的三个类分别对应一个植入缺陷。每个缺陷单独放在一个类中，便于后续分别运行测试并比较不同测试集的缺陷发现能力。

## 生成目录说明

`target/` 是 `javac`、Maven 或临时测试验证过程自动生成的输出目录，不是需要手工维护的源码目录。当前 `target/classes` 保存主源码编译后的 `.class` 文件，`target/smoke` 保存临时冒烟验证程序的编译输出。

如果需要重新验证工程，可以删除 `target/` 后重新编译或运行测试。正式提交作业时，重点材料仍然是 `src/`、`pom.xml`、`README.md` 和 `report/` 下的实验材料。

## 常用命令

运行全部测试：

```powershell
mvn test
```

按测试设计方法单独运行并导出 JaCoCo 报告：

```powershell
mvn -Pdomain test jacoco:report
mvn -Pgraph test jacoco:report
mvn -Plogic test jacoco:report
mvn -Pai test jacoco:report
```

## 报告材料

1. `report/cfg`

   - 保存 `replaceAll`、`indexOfSubList`、`disjoint` 的控制流图。
   - 图中节点编号应与报告中的主路径表保持一致。
2. `report/screenshots`

   - 保存 `mvn test`、各分组测试运行结果、JaCoCo 页面和缺陷暴露断言信息截图。
3. `report/jacoco-domain`、`report/jacoco-graph`、`report/jacoco-logic`、`report/jacoco-ai`

   - 分别保存输入域划分、图覆盖、逻辑覆盖、AI 测试四类测试集的覆盖率报告。
   - 后续报告中应分别引用这些结果，而不是只使用一次总覆盖率。
4. `report/matrices`

   - 保存覆盖率比较表和缺陷发现矩阵。
   - 建议记录每类测试集的用例数量、行覆盖率、分支覆盖率、方法覆盖率和发现的缺陷编号。
5. `report/ai`

   - 保存 AI 测试使用的提示词、AI 原始输出、首次运行问题和人工修正记录。
