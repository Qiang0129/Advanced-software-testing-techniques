# 任务 4：AI 辅助测试用例设计矩阵

## 1. 说明

本文件记录任务 4 的 AI 辅助测试用例。AI 工具为当前 Codex，测试对象为正确版本 `sut.CollectionsUnderTest`，测试代码位于 `src/test/java/ai/AIGeneratedCollectionsTest.java`。

AI 生成过程材料保存在 `report/ai/`：

1. `codex-prompt.md`：AI 提示词。
2. `codex-raw-output.md`：AI 原始输出。
3. `codex-review-and-fixes.md`：人工审查与修正记录。

## 2. AI 测试设计过程

1. 向 AI 提供 JavaDoc 语义和本地源码摘要。
2. 明确要求生成 JUnit 5 测试，并覆盖三个被测方法。
3. 不提供任务 1-3 的测试编号，避免 AI 直接复述人工测试。
4. 对 AI 输出进行人工审查，补齐 import、断言、异常检查和编号。
5. 将最终修正版写入 `AIGeneratedCollectionsTest.java`。

## 3. replaceAll AI 测试表

| 用例编号 | 覆盖方法 | 输入 | 正确期望 | 是否人工修正 |
| --- | --- | --- | --- | --- |
| AI-RA-01 | 普通多次替换 | `["a","b","a"]`, `oldVal="a"`, `newVal="x"` | 返回 `true`，列表变为 `["x","b","x"]` | 补充断言和编号 |
| AI-RA-02 | 无匹配返回 `false` | `["a","b","c"]`, `oldVal="z"` | 返回 `false`，列表不变 | 补充断言和编号 |
| AI-RA-03 | `oldVal == null` | `[null,"b",null]`, `newVal="x"` | 返回 `true`，列表变为 `["x","b","x"]` | 避免使用 `List.of(null)` |
| AI-RA-04 | `newVal == null` | `["a","b","a"]`, `oldVal="a"` | 返回 `true`，列表变为 `[null,"b",null]` | 避免使用 `List.of(null)` |
| AI-RA-05 | 空列表 | `[]`, `oldVal="a"` | 返回 `false` | 补充断言和编号 |
| AI-RA-06 | 大 `LinkedList` 迭代器路径 | 12 个 `"a"` 的 `LinkedList` | 返回 `true`，全部变为 `"x"` | 明确长度为 12 |
| AI-RA-07 | 不可修改列表异常 | `List.of("a","b")`, `oldVal="a"` | 抛出 `UnsupportedOperationException` | 补充 `assertThrows` |

## 4. indexOfSubList AI 测试表

| 用例编号 | 覆盖方法 | 输入 | 正确期望 | 是否人工修正 |
| --- | --- | --- | --- | --- |
| AI-IS-01 | 中间匹配 | `source=[1,2,3,4]`, `target=[2,3]` | 返回 `1` | 补充断言和编号 |
| AI-IS-02 | 开头匹配 | `source=[1,2,3,4]`, `target=[1,2]` | 返回 `0` | 补充断言和编号 |
| AI-IS-03 | 末尾匹配 | `source=[1,2,3,4]`, `target=[3,4]` | 返回 `2` | 补充断言和编号 |
| AI-IS-04 | 空 `target` | `source=[1,2,3]`, `target=[]` | 返回 `0` | 补充断言和编号 |
| AI-IS-05 | `target` 长于 `source` | `source=[1,2]`, `target=[1,2,3]` | 返回 `-1` | 补充断言和编号 |
| AI-IS-06 | 无匹配 | `source=[1,2,3,4]`, `target=[2,4]` | 返回 `-1` | 补充断言和编号 |
| AI-IS-07 | 包含 `null` 的子列表匹配 | `source=["a",null,"c"]`, `target=[null,"c"]` | 返回 `1` | 避免使用 `List.of(null)` |

## 5. disjoint AI 测试表

| 用例编号 | 覆盖方法 | 输入 | 正确期望 | 是否人工修正 |
| --- | --- | --- | --- | --- |
| AI-DJ-01 | 无交集 | `[1,2]`, `[3,4]` | 返回 `true` | 补充断言和编号 |
| AI-DJ-02 | 有交集 | `[1,2]`, `[2,3]` | 返回 `false` | 补充断言和编号 |
| AI-DJ-03 | 第一个集合为空 | `[]`, `[1,2]` | 返回 `true` | 补充断言和编号 |
| AI-DJ-04 | 第二个集合为空 | `[1,2]`, `[]` | 返回 `true` | 补充断言和编号 |
| AI-DJ-05 | `Set` 与 `List` 组合 | `{1,2}`, `[2,3]` | 返回 `false` | 补充 `HashSet` import |
| AI-DJ-06 | 公共元素为 `null` | `[null,"a"]`, `["b",null]` | 返回 `false` | 避免使用 `List.of(null)` |

## 6. 运行证据

1. 测试运行截图：`test/software-testing-final/report/screenshots/stage7-ai-test-build-success.png`
2. JaCoCo 覆盖率截图：`test/software-testing-final/report/screenshots/stage7-ai-jacoco-coverage.png`
3. JaCoCo HTML 报告目录：`test/software-testing-final/report/jacoco-ai/`
4. 测试运行结果：`Tests run: 20, Failures: 0, Errors: 0, Skipped: 0`
5. `CollectionsUnderTest` 覆盖率：指令覆盖率 `73%`，分支覆盖率 `64%`，行覆盖 `46/61`，方法覆盖 `4/4`
