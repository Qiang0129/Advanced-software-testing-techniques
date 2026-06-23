package sut.faulty;

import java.util.List;
import java.util.Objects;

/**
 * indexOfSubList 的缺陷版本：故意漏掉最后一个候选起点。
 */
public final class FaultyIndexOfSubList {
    private FaultyIndexOfSubList() {
    }

    public static int indexOfSubList(List<?> source, List<?> target) {
        int sourceSize = source.size();
        int targetSize = target.size();
        if (targetSize == 0) {
            return 0;
        }
        if (targetSize > sourceSize) {
            return -1;
        }

        int maxCandidate = sourceSize - targetSize;
        // 缺陷点：这里应包含 candidate == maxCandidate 的情况。
        for (int candidate = 0; candidate < maxCandidate; candidate++) {
            boolean matched = true;
            for (int i = 0; i < targetSize; i++) {
                if (!Objects.equals(source.get(candidate + i), target.get(i))) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                return candidate;
            }
        }
        return -1;
    }
}
