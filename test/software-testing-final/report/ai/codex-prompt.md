# 任务 4：AI 生成测试提示词

## 1. AI 工具

1. 工具名称：当前 Codex。
2. 使用日期：2026-06-23。
3. 使用目的：根据 JavaDoc 语义和本地 `CollectionsUnderTest` 源码摘要生成 JUnit 5 测试用例。

## 2. 提示词

```text
请基于下面的被测方法语义和源码摘要，为 sut.CollectionsUnderTest 生成 JUnit 5 测试用例。

要求：
1. 只测试正确版本 sut.CollectionsUnderTest，不测试 faulty 类。
2. 使用 JUnit 5。
3. 每个测试方法要有清晰名称。
4. 需要覆盖 java.util.Collections 中三个方法的典型行为和边界行为：
   - replaceAll(List<T> list, T oldVal, T newVal)
   - indexOfSubList(List<?> source, List<?> target)
   - disjoint(Collection<?> c1, Collection<?> c2)
5. 需要包含 null 元素、空集合、无匹配、有匹配、不可修改列表、LinkedList 大列表等情况。
6. 不要引用任务 1、任务 2、任务 3 的测试编号，不要复述已有人工测试。
7. 输出一个可放入 src/test/java/ai/AIGeneratedCollectionsTest.java 的测试类。

方法语义：
1. replaceAll 将列表中所有等于 oldVal 的元素替换为 newVal；如果至少替换一个元素返回 true，否则返回 false；列表不可修改且需要替换时会抛出 UnsupportedOperationException。
2. indexOfSubList 返回 target 在 source 中第一次完整出现的起始索引；如果不存在返回 -1；如果 target 为空返回 0。
3. disjoint 判断两个集合是否没有公共元素；没有公共元素返回 true，存在公共元素返回 false。

源码摘要：
1. replaceAll 根据 list 大小和 RandomAccess 分为索引路径和迭代器路径；oldVal 为 null 时使用 == null 比较，否则使用 equals 比较。
2. indexOfSubList 根据 source 大小和 RandomAccess 分为随机访问路径和迭代器路径；内部使用 eq 处理 null 元素。
3. disjoint 会优先使用 Set 作为 contains 集合；两个非 Set 集合时会处理空集合并尽量迭代较小集合。
```

## 3. 使用限制

1. 提示词没有提供任务 1-3 的测试编号。
2. 提示词只提供方法语义和源码摘要，避免 AI 直接复述人工设计测试。
3. 生成结果需要人工审查后才能进入最终测试类。
