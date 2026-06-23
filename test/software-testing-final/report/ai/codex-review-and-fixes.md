# 任务 4：AI 输出审查与人工修正记录

## 1. 审查结论

AI 原始输出可以作为测试设计基础，但不能直接作为最终提交代码。主要原因是原始输出偏向测试场景列表，缺少课程报告所需的统一编号、完整 JUnit 类结构和人工可追溯记录。

## 2. 编译与运行风险审查

1. import 不完整
   - 原始草案没有列出 `ArrayList`、`LinkedList`、`HashSet`、`Collections`、`assertEquals`、`assertFalse`、`assertThrows` 等 import。
   - 修正方式：在最终测试类中补全所有 import。

2. `null` 与不可变集合
   - Java `List.of(...)` 不允许包含 `null`。
   - 修正方式：包含 `null` 的测试统一使用 `new ArrayList<>(Arrays.asList(...))`。

3. 不可修改列表异常
   - `replaceAll` 只有在存在匹配并尝试 `set` 时才会触发 `UnsupportedOperationException`。
   - 修正方式：使用 `List.of("a", "b")` 且 `oldVal="a"`，并使用 `assertThrows`。

4. 大 `LinkedList` 路径
   - 原始方案只写“大 LinkedList”，没有明确长度。
   - 修正方式：使用长度 `12`，超过 `REPLACEALL_THRESHOLD=11`。

5. 测试编号缺失
   - 原始输出没有课程报告用例编号。
   - 修正方式：添加 `AI-RA-01` 至 `AI-RA-07`、`AI-IS-01` 至 `AI-IS-07`、`AI-DJ-01` 至 `AI-DJ-06`。

## 3. 最终人工修正结果

1. 最终测试类：`src/test/java/ai/AIGeneratedCollectionsTest.java`
2. 最终测试数量：`20`
3. 修正原则：
   - 不改变 AI 原始选择的测试场景。
   - 只补充使测试可编译、可运行、可追溯所需的最小结构。
   - 保留 AI 生成测试与任务 1-3 人工测试的独立编号。

## 4. 运行验证结果

1. 运行命令：

```powershell
cd "D:\Desktop\高级软件测试技术课程作业\test\software-testing-final"
Remove-Item -Recurse -Force "report\jacoco-ai" -ErrorAction SilentlyContinue
mvn -Pai clean test jacoco:report
```

2. 实际结果：

```text
Tests run: 20, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.077 s -- in ai.AIGeneratedCollectionsTest
```

3. 覆盖率结果：

```text
CollectionsUnderTest 指令覆盖率 73%，分支覆盖率 64%，行覆盖 46/61，方法覆盖 4/4
```
