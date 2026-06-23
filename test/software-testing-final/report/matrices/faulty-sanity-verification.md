# 缺陷触发验证表

说明：本表用于阶段 3“缺陷植入”的有效性验证，证明三个 faulty 版本均能被关键输入触发。该表不是任务 1-4 的最终缺陷发现矩阵。

| 缺陷编号 | 对应方法 | 缺陷类型 | 植入方式 | 缺陷影响 | 预期发现测试 | 触发输入 | 正确期望 | faulty 实际 | 验证结论 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| F1 | `replaceAll` | 空值处理缺陷 | 使用 `oldVal.equals(list.get(i))` 判断元素是否匹配 | 当 `oldVal == null` 时触发 `NullPointerException`，无法正确替换列表中的 `null` 元素 | 覆盖 `oldVal == null` 且列表中包含 `null` 元素的测试用例 | `list=[null, b, null]`，`oldVal=null`，`newVal=x` | 返回 `true`，列表变为 `[x, b, x]` | 抛出 `NullPointerException` | 缺陷已触发 |
| F2 | `indexOfSubList` | 边界错误 / off-by-one | 候选起点循环使用 `candidate < maxCandidate`，漏掉 `candidate == maxCandidate` | 当 `target` 正好出现在 `source` 末尾时，错误版本返回 `-1` | 覆盖 `target` 位于 `source` 末尾的测试用例 | `source=[1, 2, 3, 4]`，`target=[3, 4]` | 返回 `2` | 返回 `-1` | 缺陷已触发 |
| F3 | `disjoint` | 逻辑判断错误 | 发现公共元素时返回 `true` | 当两个集合存在公共元素时，错误版本仍判断为不相交 | 覆盖两个集合存在公共元素的测试用例 | `c1=[1, 2]`，`c2=[2, 3]` | 返回 `false` | 返回 `true` | 缺陷已触发 |

验证测试类：

```text
test/software-testing-final/src/test/java/sanity/FaultySanityTest.java
```

运行命令：

```powershell
mvn "-Dtest=sanity.FaultySanityTest" test
```

运行结果摘要：

```text
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
Finished at: 2026-06-22T15:39:28+08:00
```
