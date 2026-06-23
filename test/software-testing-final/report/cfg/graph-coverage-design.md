# 任务 2：基于图覆盖的测试用例设计

## 说明

本文件记录 `replaceAll`、`indexOfSubList`、`disjoint` 三个方法的 CFG 设计、主路径覆盖需求和 JUnit 测试映射。CFG 依据本地正确版本 `src/main/java/sut/CollectionsUnderTest.java` 构造，测试类为 `src/test/java/graph/GraphCoverageTest.java`。

## 设计过程

1. 读取本地 SUT 源码，按顺序识别入口、判断、循环、循环体、提前返回和最终返回。
2. 将连续执行且无内部跳转的语句合并为基本块，给每个方法建立独立节点编号。
3. 根据 `if`、`else if`、`for`、`continue`、`return` 建立控制流边。
4. 从 CFG 中提取关键主路径，重点覆盖分支选择、循环 0 次、循环 1 次或多次、提前返回和失败返回。
5. 为每条主路径选择能执行该路径的输入，形成 `G-RA-*`、`G-IS-*`、`G-DJ-*` 测试编号。
6. 用 JUnit 5 断言验证正确版本输出，覆盖率由 `graph` Maven profile 生成。

## replaceAll 的 CFG

节点说明：

1. RA-N1：入口，读取 `size`，初始化 `result=false`。
2. RA-N2：判断 `size < 11 || list instanceof RandomAccess`。
3. RA-N3：索引路径判断 `oldVal == null`。
4. RA-N4：索引路径 `oldVal == null` 的循环条件。
5. RA-N5：索引路径判断 `list.get(i) == null`。
6. RA-N6：索引路径执行 `set` 并置 `result=true`。
7. RA-N7：索引路径 `oldVal != null` 的循环条件。
8. RA-N8：索引路径判断 `oldVal.equals(list.get(i))`。
9. RA-N9：索引路径执行 `set` 并置 `result=true`。
10. RA-N10：迭代器路径初始化 `listIterator()`。
11. RA-N11：迭代器路径判断 `oldVal == null`。
12. RA-N12：迭代器路径 `oldVal == null` 的循环条件。
13. RA-N13：迭代器路径判断 `itr.next() == null`。
14. RA-N14：迭代器路径执行 `itr.set` 并置 `result=true`。
15. RA-N15：迭代器路径 `oldVal != null` 的循环条件。
16. RA-N16：迭代器路径判断 `oldVal.equals(itr.next())`。
17. RA-N17：迭代器路径执行 `itr.set` 并置 `result=true`。
18. RA-N18：返回 `result`。

边集合：

```text
RA-N1 -> RA-N2
RA-N2(T) -> RA-N3
RA-N2(F) -> RA-N10
RA-N3(T) -> RA-N4
RA-N3(F) -> RA-N7
RA-N4(T) -> RA-N5
RA-N4(F) -> RA-N18
RA-N5(T) -> RA-N6
RA-N5(F) -> RA-N4
RA-N6 -> RA-N4
RA-N7(T) -> RA-N8
RA-N7(F) -> RA-N18
RA-N8(T) -> RA-N9
RA-N8(F) -> RA-N7
RA-N9 -> RA-N7
RA-N10 -> RA-N11
RA-N11(T) -> RA-N12
RA-N11(F) -> RA-N15
RA-N12(T) -> RA-N13
RA-N12(F) -> RA-N18
RA-N13(T) -> RA-N14
RA-N13(F) -> RA-N12
RA-N14 -> RA-N12
RA-N15(T) -> RA-N16
RA-N15(F) -> RA-N18
RA-N16(T) -> RA-N17
RA-N16(F) -> RA-N15
RA-N17 -> RA-N15
```

主路径与测试映射：

1. G-RA-01：RA-N1 -> RA-N2(T) -> RA-N3(T) -> RA-N4(T) -> RA-N5(T) -> RA-N6 -> RA-N4 -> RA-N18。
2. G-RA-02：RA-N1 -> RA-N2(T) -> RA-N3(F) -> RA-N7(T) -> RA-N8(T) -> RA-N9 -> RA-N7 -> RA-N18。
3. G-RA-03：RA-N1 -> RA-N2(T) -> RA-N3(F) -> RA-N7(T) -> RA-N8(F) -> RA-N7 -> RA-N18。
4. G-RA-04：RA-N1 -> RA-N2(T) -> RA-N3(F) -> RA-N7(F) -> RA-N18。
5. G-RA-05：RA-N1 -> RA-N2(F) -> RA-N10 -> RA-N11(T) -> RA-N12(T) -> RA-N13(T) -> RA-N14 -> RA-N12 -> RA-N18。
6. G-RA-06：RA-N1 -> RA-N2(F) -> RA-N10 -> RA-N11(F) -> RA-N15(T) -> RA-N16(T) -> RA-N17 -> RA-N15 -> RA-N18。
7. G-RA-07：RA-N1 -> RA-N2(T) -> RA-N3(F) -> RA-N7(T) -> RA-N8(T) -> RA-N9 -> RA-N7 -> RA-N18，其中 `size >= 11` 但 `RandomAccess` 为真。

## indexOfSubList 的 CFG

节点说明：

1. IS-N1：入口，计算 `sourceSize`、`targetSize`、`maxCandidate`。
2. IS-N2：判断 `sourceSize < 35 || both RandomAccess`。
3. IS-N3：随机访问候选循环条件。
4. IS-N4：随机访问内层匹配循环条件。
5. IS-N5：判断 `!eq(target.get(i), source.get(j))`。
6. IS-N6：随机访问路径 `continue nextCand`。
7. IS-N7：随机访问路径匹配成功，返回 `candidate`。
8. IS-N8：迭代器路径初始化 `source.listIterator()`。
9. IS-N9：迭代器候选循环条件。
10. IS-N10：初始化 `target.listIterator()`。
11. IS-N11：迭代器内层匹配循环条件。
12. IS-N12：判断 `!eq(ti.next(), si.next())`。
13. IS-N13：回退循环条件 `j < i`。
14. IS-N14：执行 `si.previous()`。
15. IS-N15：迭代器路径 `continue nextCand`。
16. IS-N16：迭代器路径匹配成功，返回 `candidate`。
17. IS-N17：全部候选失败，返回 `-1`。

边集合：

```text
IS-N1 -> IS-N2
IS-N2(T) -> IS-N3
IS-N2(F) -> IS-N8
IS-N3(T) -> IS-N4
IS-N3(F) -> IS-N17
IS-N4(T) -> IS-N5
IS-N4(F) -> IS-N7
IS-N5(T) -> IS-N6
IS-N5(F) -> IS-N4
IS-N6 -> IS-N3
IS-N8 -> IS-N9
IS-N9(T) -> IS-N10
IS-N9(F) -> IS-N17
IS-N10 -> IS-N11
IS-N11(T) -> IS-N12
IS-N11(F) -> IS-N16
IS-N12(T) -> IS-N13
IS-N12(F) -> IS-N11
IS-N13(T) -> IS-N14
IS-N13(F) -> IS-N15
IS-N14 -> IS-N13
IS-N15 -> IS-N9
```

主路径与测试映射：

1. G-IS-01：IS-N1 -> IS-N2(T) -> IS-N3(T) -> IS-N4(F) -> IS-N7，覆盖空 `target`。
2. G-IS-02：IS-N1 -> IS-N2(T) -> IS-N3(T) -> IS-N4(T) -> IS-N5(F) -> IS-N4(F) -> IS-N7。
3. G-IS-03：IS-N1 -> IS-N2(T) -> IS-N3(T) -> IS-N4(T) -> IS-N5(T) -> IS-N6 -> IS-N3(T) -> IS-N7。
4. G-IS-04：IS-N1 -> IS-N2(T) -> IS-N3(F) -> IS-N17。
5. G-IS-05：IS-N1 -> IS-N2(T) -> IS-N3(T) -> IS-N5(T) -> IS-N6 -> IS-N3(F) -> IS-N17。
6. G-IS-06：IS-N1 -> IS-N2(T) -> IS-N3(T) 多次循环，末尾候选返回 IS-N7。
7. G-IS-07：IS-N1 -> IS-N2(F) -> IS-N8 -> IS-N9(T) -> IS-N10 -> IS-N11(T) -> IS-N12(F) -> IS-N11(F) -> IS-N16。
8. G-IS-08：IS-N1 -> IS-N2(F) -> IS-N8 -> IS-N9(T) -> IS-N10 -> IS-N11(T) -> IS-N12(T) -> IS-N13(T) -> IS-N14 -> IS-N15 -> IS-N9(T) -> IS-N16。
9. G-IS-09：IS-N1 -> IS-N2(F) -> IS-N8 -> IS-N9(T) -> IS-N12(T) -> IS-N15 -> IS-N9(F) -> IS-N17。

## disjoint 的 CFG

节点说明：

1. DJ-N1：入口，初始化 `contains=c2`、`iterate=c1`。
2. DJ-N2：判断 `c1 instanceof Set`。
3. DJ-N3：将 `iterate=c2`、`contains=c1`。
4. DJ-N4：判断 `!(c2 instanceof Set)`。
5. DJ-N5：读取两个集合大小。
6. DJ-N6：判断 `c1size == 0 || c2size == 0`。
7. DJ-N7：空集合提前返回 `true`。
8. DJ-N8：判断 `c1size > c2size`。
9. DJ-N9：交换 `iterate` 和 `contains`。
10. DJ-N10：遍历 `iterate`。
11. DJ-N11：判断 `contains.contains(e)`。
12. DJ-N12：发现公共元素，返回 `false`。
13. DJ-N13：遍历结束，返回 `true`。

边集合：

```text
DJ-N1 -> DJ-N2
DJ-N2(T) -> DJ-N3
DJ-N2(F) -> DJ-N4
DJ-N3 -> DJ-N10
DJ-N4(T) -> DJ-N5
DJ-N4(F) -> DJ-N10
DJ-N5 -> DJ-N6
DJ-N6(T) -> DJ-N7
DJ-N6(F) -> DJ-N8
DJ-N8(T) -> DJ-N9
DJ-N8(F) -> DJ-N10
DJ-N9 -> DJ-N10
DJ-N10(T) -> DJ-N11
DJ-N10(F) -> DJ-N13
DJ-N11(T) -> DJ-N12
DJ-N11(F) -> DJ-N10
```

主路径与测试映射：

1. G-DJ-01：DJ-N1 -> DJ-N2(T) -> DJ-N3 -> DJ-N10 -> DJ-N11(F) -> DJ-N13。
2. G-DJ-02：DJ-N1 -> DJ-N2(T) -> DJ-N3 -> DJ-N10 -> DJ-N11(T) -> DJ-N12。
3. G-DJ-03：DJ-N1 -> DJ-N2(F) -> DJ-N4(T) -> DJ-N5 -> DJ-N6(T) -> DJ-N7。
4. G-DJ-04：DJ-N1 -> DJ-N2(F) -> DJ-N4(T) -> DJ-N5 -> DJ-N6(T) -> DJ-N7。
5. G-DJ-05：DJ-N1 -> DJ-N2(F) -> DJ-N4(T) -> DJ-N5 -> DJ-N6(F) -> DJ-N8(T) -> DJ-N9 -> DJ-N10 -> DJ-N13。
6. G-DJ-06：DJ-N1 -> DJ-N2(F) -> DJ-N4(T) -> DJ-N5 -> DJ-N6(F) -> DJ-N8(F) -> DJ-N10 -> DJ-N13。
7. G-DJ-07：DJ-N1 -> DJ-N2(F) -> DJ-N4(F) -> DJ-N10 -> DJ-N11(T) -> DJ-N12。
8. G-DJ-08：DJ-N1 -> DJ-N2(F) -> DJ-N4(T) -> DJ-N5 -> DJ-N6(F) -> DJ-N10 -> DJ-N11(T) -> DJ-N12，公共元素为 `null`。

## 测试用例明细

### replaceAll

1. G-RA-01
   - 输入：`ArrayList [null, "a", null]`，`oldVal=null`，`newVal="x"`。
   - 期望：返回 `true`，列表变为 `["x", "a", "x"]`。
   - 覆盖路径：索引路径、`oldVal == null`、匹配分支。
2. G-RA-02
   - 输入：`ArrayList ["a", "b", "a"]`，`oldVal="a"`，`newVal="x"`。
   - 期望：返回 `true`，列表变为 `["x", "b", "x"]`。
   - 覆盖路径：索引路径、`oldVal != null`、多次匹配。
3. G-RA-03
   - 输入：`ArrayList ["a", "b", "c"]`，`oldVal="z"`，`newVal="x"`。
   - 期望：返回 `false`，列表不变。
   - 覆盖路径：索引路径、非空旧值、无匹配分支。
4. G-RA-04
   - 输入：空 `ArrayList`，`oldVal="a"`，`newVal="x"`。
   - 期望：返回 `false`。
   - 覆盖路径：循环 0 次，直接返回。
5. G-RA-05
   - 输入：长度 12 的 `LinkedList`，第 3 和第 7 个元素为 `null`，`oldVal=null`，`newVal="x"`。
   - 期望：返回 `true`，两个 `null` 被替换为 `"x"`。
   - 覆盖路径：非 `RandomAccess` 迭代器路径、`oldVal == null`。
6. G-RA-06
   - 输入：长度 12 的 `LinkedList`，全部元素为 `"a"`，`oldVal="a"`，`newVal="x"`。
   - 期望：返回 `true`，全部元素变为 `"x"`。
   - 覆盖路径：非 `RandomAccess` 迭代器路径、`oldVal != null`。
7. G-RA-07
   - 输入：长度 12 的 `ArrayList`，全部元素为 `"a"`，`oldVal="a"`，`newVal="x"`。
   - 期望：返回 `true`，全部元素变为 `"x"`。
   - 覆盖路径：`size >= 11` 但 `RandomAccess` 为真，仍走索引路径。

### indexOfSubList

1. G-IS-01
   - 输入：`source=[1,2,3]`，`target=[]`。
   - 期望：返回 `0`。
   - 覆盖路径：随机访问路径，内层循环 0 次。
2. G-IS-02
   - 输入：`source=[1,2,3]`，`target=[1,2]`。
   - 期望：返回 `0`。
   - 覆盖路径：随机访问路径，首候选命中。
3. G-IS-03
   - 输入：`source=[0,1,2,1,2]`，`target=[1,2]`。
   - 期望：返回 `1`。
   - 覆盖路径：随机访问路径，首候选失败后继续。
4. G-IS-04
   - 输入：`source=[1]`，`target=[1,2]`。
   - 期望：返回 `-1`。
   - 覆盖路径：候选循环 0 次。
5. G-IS-05
   - 输入：`source=[1,2,3]`，`target=[4]`。
   - 期望：返回 `-1`。
   - 覆盖路径：随机访问路径，全部候选失败。
6. G-IS-06
   - 输入：`source=[1,2,3,4]`，`target=[3,4]`。
   - 期望：返回 `2`。
   - 覆盖路径：随机访问路径，末尾候选命中。
7. G-IS-07
   - 输入：长度 40 的 `LinkedList`，`target=[20,21,22]`。
   - 期望：返回 `20`。
   - 覆盖路径：大列表迭代器路径，匹配成功返回。
8. G-IS-08
   - 输入：长度 36 的 `LinkedList`，前部构造部分匹配后失败，`target=[0,99]`。
   - 期望：返回 `2`。
   - 覆盖路径：迭代器路径，部分匹配失败后执行 `si.previous()` 回退。
9. G-IS-09
   - 输入：长度 36 的 `LinkedList`，`target=ArrayList [1000]`。
   - 期望：返回 `-1`。
   - 覆盖路径：迭代器路径，全部候选失败。

### disjoint

1. G-DJ-01
   - 输入：`c1=HashSet [1,2]`，`c2=List [3,4]`。
   - 期望：返回 `true`。
   - 覆盖路径：`c1 instanceof Set`，遍历结束无公共元素。
2. G-DJ-02
   - 输入：`c1=HashSet [1,2]`，`c2=List [2,3]`。
   - 期望：返回 `false`。
   - 覆盖路径：`c1 instanceof Set`，发现公共元素。
3. G-DJ-03
   - 输入：`c1=[]`，`c2=[1,2]`。
   - 期望：返回 `true`。
   - 覆盖路径：两个非 Set，`c1size == 0` 提前返回。
4. G-DJ-04
   - 输入：`c1=[1,2]`，`c2=[]`。
   - 期望：返回 `true`。
   - 覆盖路径：两个非 Set，`c2size == 0` 提前返回。
5. G-DJ-05
   - 输入：`c1=[1,2,3]`，`c2=[4]`。
   - 期望：返回 `true`。
   - 覆盖路径：两个非 Set，`c1.size() > c2.size()` 触发交换。
6. G-DJ-06
   - 输入：`c1=[1]`，`c2=[2,3]`。
   - 期望：返回 `true`。
   - 覆盖路径：两个非 Set，不交换，遍历结束无公共元素。
7. G-DJ-07
   - 输入：`c1=List [1,2]`，`c2=LinkedHashSet [2,3]`。
   - 期望：返回 `false`。
   - 覆盖路径：`c2 instanceof Set`，发现公共元素。
8. G-DJ-08
   - 输入：`c1=[null,"a"]`，`c2=["b",null]`。
   - 期望：返回 `false`。
   - 覆盖路径：两个非 Set，公共元素为 `null`。

## 运行命令

生成 CFG 图片：

```powershell
cd "D:\Desktop\高级软件测试技术课程作业\test\software-testing-final"

dot -Tpng report\cfg\replaceAll-cfg.dot -o report\cfg\replaceAll-cfg.png
dot -Tpng report\cfg\indexOfSubList-cfg.dot -o report\cfg\indexOfSubList-cfg.png
dot -Tpng report\cfg\disjoint-cfg.dot -o report\cfg\disjoint-cfg.png
```

运行图覆盖测试并生成 JaCoCo 报告：

```powershell
cd "D:\Desktop\高级软件测试技术课程作业\test\software-testing-final"

Remove-Item -Recurse -Force "report\jacoco-graph" -ErrorAction SilentlyContinue

mvn -Pgraph clean test jacoco:report
```

预期测试结果：

```text
Tests run: 24, Failures: 0, Errors: 0, Skipped: 0
```
