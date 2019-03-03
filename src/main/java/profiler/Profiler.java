package profiler;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 性能埋点工具
 */
public class Profiler {

    private static final ThreadLocal<Entry> STACK = new ThreadLocal<>();

    private Profiler() {
    }

    /**
     * 重置方法
     */
    public static void reset() {
        STACK.set(null);
    }

    /**
     * 初始化方法
     */
    public static void init() {
        init(StringUtils.EMPTY);
    }

    /**
     * 初始化方法
     */
    public static void init(String context) {
        STACK.set(new Entry(null, null, context));
    }

    /**
     * 进入埋点方法
     */
    public static void enter(String context) {
        Entry currentEntry = STACK.get();
        if (currentEntry == null) {
            return;
        }
        if (currentEntry.endTime == 0) {
            // 当前操作未完成
            Entry newEntry = new Entry(currentEntry.root, currentEntry, context);
            currentEntry.childs.add(newEntry);
            STACK.set(newEntry);
        } else {
            // 当前操作已完成
            Entry newEntry = new Entry(currentEntry.root, currentEntry.parent, context);
            currentEntry.parent.childs.add(newEntry);
            STACK.set(newEntry);
        }
    }

    /**
     * 退出埋点方法
     */
    public static void exit() {
        Entry entry = STACK.get();
        if (entry == null) {
            return;
        }
        if (entry.endTime == 0) {
            entry.endTime = System.currentTimeMillis();
            STACK.set(entry.parent);
        } else {
            return;
        }
    }

    /**
     * 打印调用栈
     */
    public static String dump() {
        Entry currentEntry = STACK.get();
        if (currentEntry == null) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        Entry root = currentEntry.root;
        sb.append(recursiveDump(root, 0));
        return sb.toString();
    }

    /**
     * 递归打印
     */
    private static String recursiveDump(Entry entry, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append(entry.dump());
        sb.append("\n");
        index++;
        for (int i = 0; i < entry.childs.size(); i++) {
            sb.append(new String(new char[index]).replace("\0", "  "));
            sb.append("\\--");
            sb.append(recursiveDump(entry.childs.get(i), index));
        }
        return sb.toString();
    }


    /**
     * 操作实例
     */
    private static final class Entry {

        private final Entry root;
        private final Entry parent;
        private final List<Entry> childs;
        private final long startTime;
        private long endTime;
        private final String context;

        Entry(Entry root, Entry parent, String context) {
            this.root = (root == null) ? this : root;
            this.parent = parent;
            this.childs = new ArrayList<>();
            this.startTime = System.currentTimeMillis();
            this.context = context;
        }

        private String dump() {
            StringBuilder sb = new StringBuilder();
            sb.append("耗时：");
            if (endTime == 0) {
                return "操作调用树";
            } else {
                sb.append(endTime - startTime);
            }
            sb.append("ms");
            sb.append(" - ");
            sb.append(context);
            return sb.toString();
        }

    }

}
