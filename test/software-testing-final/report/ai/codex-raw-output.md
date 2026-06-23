# 任务 4：AI 原始输出记录

## 1. 原始输出说明

以下内容记录当前 Codex 根据提示词生成的初始测试方案。原始输出的核心是为三个方法分别生成典型场景和边界场景测试，再由人工审查后整理为最终 `AIGeneratedCollectionsTest.java`。

## 2. AI 原始测试方案

1. `replaceAll`
   - 普通列表中同一旧值出现多次，应全部替换并返回 `true`。
   - 旧值不存在时应返回 `false`，列表保持不变。
   - `oldVal` 为 `null` 时，应替换列表中的 `null` 元素。
   - `newVal` 为 `null` 时，应把匹配元素替换为 `null`。
   - 空列表应返回 `false`。
   - 大 `LinkedList` 应能正常替换所有匹配元素。
   - 不可修改列表在需要替换时应抛出 `UnsupportedOperationException`。

2. `indexOfSubList`
   - 目标子列表在中间出现时返回中间索引。
   - 目标子列表在开头出现时返回 `0`。
   - 目标子列表在末尾出现时返回最后候选起点。
   - 空 `target` 返回 `0`。
   - `target` 长于 `source` 返回 `-1`。
   - 不存在完整匹配时返回 `-1`。
   - 子列表包含 `null` 时应正确匹配。

3. `disjoint`
   - 两个集合无交集时返回 `true`。
   - 两个集合有公共元素时返回 `false`。
   - 第一个集合为空时返回 `true`。
   - 第二个集合为空时返回 `true`。
   - `Set` 与 `List` 组合存在公共元素时返回 `false`。
   - 公共元素为 `null` 时返回 `false`。

## 3. AI 原始代码草案

```java
package ai;

import org.junit.jupiter.api.Test;
import sut.CollectionsUnderTest;

class AIGeneratedCollectionsTest {
    // AI 建议为 replaceAll、indexOfSubList、disjoint 生成多个 @Test。
    // 初始输出中的重点场景包括普通替换、null、空集合、末尾匹配和 Set/List 组合。
}
```

## 4. 原始输出评价

1. 原始方案覆盖了三个方法的主要语义和常见边界。
2. 原始方案没有按课程报告需要添加统一编号，因此人工增加了 `AI-RA-*`、`AI-IS-*`、`AI-DJ-*` 编号。
3. 原始方案需要人工补齐完整 import、断言和异常测试细节。
