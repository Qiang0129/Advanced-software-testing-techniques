package sut.faulty;

import java.util.Collection;

/**
 * disjoint 的缺陷版本：故意把“存在公共元素”的结果判断写反。
 */
public final class FaultyDisjoint {
    private FaultyDisjoint() {
    }

    public static boolean disjoint(Collection<?> c1, Collection<?> c2) {
        for (Object element : c1) {
            if (c2.contains(element)) {
                // 缺陷点：发现公共元素时应返回 false。
                return true;
            }
        }
        return true;
    }
}
