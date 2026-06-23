package sut.faulty;

import java.util.List;

/**
 * replaceAll 的缺陷版本：故意破坏 oldVal 为 null 时的处理逻辑。
 */
public final class FaultyReplaceAll {
    private FaultyReplaceAll() {
    }

    public static <T> boolean replaceAll(List<T> list, T oldVal, T newVal) {
        boolean result = false;
        for (int i = 0; i < list.size(); i++) {
            // 缺陷点：oldVal 为 null 时会触发 NullPointerException。
            if (oldVal.equals(list.get(i))) {
                list.set(i, newVal);
                result = true;
            }
        }
        return result;
    }
}
