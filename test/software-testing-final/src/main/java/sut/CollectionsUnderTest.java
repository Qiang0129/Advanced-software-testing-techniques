package sut;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.Set;

/**
 * 正确版本的被测类。
 *
 * <p>源码来源：OpenJDK jdk/master，
 * src/java.base/share/classes/java/util/Collections.java。
 * 下载日期：2026-06-22。
 * commit：2e179fec7b5113a3b526ee4ad5c66d6b7f0179e2。
 */
public final class CollectionsUnderTest {
    private static final int REPLACEALL_THRESHOLD = 11;
    private static final int INDEXOFSUBLIST_THRESHOLD = 35;

    private CollectionsUnderTest() {
    }

    /**
     * 将列表中所有等于 oldVal 的元素替换为 newVal。
     *
     * @param list 要修改的列表
     * @param oldVal 要替换的旧值，可为 null
     * @param newVal 新值，可为 null
     * @param <T> 列表元素类型
     * @return 如果至少替换了一个元素，返回 true；否则返回 false
     */
    public static <T> boolean replaceAll(List<T> list, T oldVal, T newVal) {
        boolean result = false;
        int size = list.size();
        if (size < REPLACEALL_THRESHOLD || list instanceof RandomAccess) {
            if (oldVal == null) {
                for (int i = 0; i < size; i++) {
                    if (list.get(i) == null) {
                        list.set(i, newVal);
                        result = true;
                    }
                }
            } else {
                for (int i = 0; i < size; i++) {
                    if (oldVal.equals(list.get(i))) {
                        list.set(i, newVal);
                        result = true;
                    }
                }
            }
        } else {
            ListIterator<T> itr = list.listIterator();
            if (oldVal == null) {
                for (int i = 0; i < size; i++) {
                    if (itr.next() == null) {
                        itr.set(newVal);
                        result = true;
                    }
                }
            } else {
                for (int i = 0; i < size; i++) {
                    if (oldVal.equals(itr.next())) {
                        itr.set(newVal);
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 查找 target 在 source 中第一次完整出现的起始位置。
     *
     * @param source 源列表
     * @param target 目标子列表
     * @return target 首次出现的索引；若不存在返回 -1；若 target 为空返回 0
     */
    public static int indexOfSubList(List<?> source, List<?> target) {
        int sourceSize = source.size();
        int targetSize = target.size();
        int maxCandidate = sourceSize - targetSize;

        if (sourceSize < INDEXOFSUBLIST_THRESHOLD ||
                (source instanceof RandomAccess && target instanceof RandomAccess)) {
        nextCand:
            for (int candidate = 0; candidate <= maxCandidate; candidate++) {
                for (int i = 0, j = candidate; i < targetSize; i++, j++) {
                    if (!eq(target.get(i), source.get(j))) {
                        continue nextCand;
                    }
                }
                return candidate;
            }
        } else {
            ListIterator<?> si = source.listIterator();
        nextCand:
            for (int candidate = 0; candidate <= maxCandidate; candidate++) {
                ListIterator<?> ti = target.listIterator();
                for (int i = 0; i < targetSize; i++) {
                    if (!eq(ti.next(), si.next())) {
                        // 将 source 迭代器退回到下一个候选起点。
                        for (int j = 0; j < i; j++) {
                            si.previous();
                        }
                        continue nextCand;
                    }
                }
                return candidate;
            }
        }
        return -1;
    }

    /**
     * 判断两个集合是否没有公共元素。
     *
     * @param c1 第一个集合
     * @param c2 第二个集合
     * @return 如果两个集合没有公共元素，返回 true；否则返回 false
     */
    public static boolean disjoint(Collection<?> c1, Collection<?> c2) {
        Collection<?> contains = c2;
        Collection<?> iterate = c1;

        // 优先使用 contains() 复杂度更低的集合，并尽量迭代较小集合。
        if (c1 instanceof Set) {
            iterate = c2;
            contains = c1;
        } else if (!(c2 instanceof Set)) {
            int c1size = c1.size();
            int c2size = c2.size();
            if (c1size == 0 || c2size == 0) {
                return true;
            }

            if (c1size > c2size) {
                iterate = c2;
                contains = c1;
            }
        }

        for (Object e : iterate) {
            if (contains.contains(e)) {
                return false;
            }
        }

        return true;
    }

    private static boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }
}
