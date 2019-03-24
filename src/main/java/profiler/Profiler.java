package profiler;

import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        Entry currentEntry = getCurrentEntry();
        if (currentEntry != null) {
            currentEntry.addChild(context);
        }
    }

    /**
     * 退出埋点方法
     */
    public static void exit() {
        Entry currentEntry = getCurrentEntry();
        if (currentEntry != null) {
            currentEntry.finish();
        }
    }

    private static Entry getCurrentEntry() {
        Entry root = STACK.get();
        if (root == null) {
            return null;
        }
        Entry entry = root;
        Entry childEntry = root.getUnFinishedChild();
        while (childEntry != null) {
            entry = childEntry;
            childEntry = entry.getUnFinishedChild();
        }
        return entry;
    }

    /**
     * 打印调用栈
     */
    public static String dump() {
        Entry root = STACK.get();
        if (root == null) {
            return StringUtils.EMPTY;
        }
        return root.dump(StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * 操作节点
     */
    private static final class Entry {

        private static final long UNKNOWN_TIME = -1;

        private final Entry root;
        private final Entry parent;
        private final List<Entry> childs;
        private final long startTime;
        private long endTime = UNKNOWN_TIME;
        private final String context;

        Entry(Entry root, Entry parent, String context) {
            this.root = (root == null) ? this : root;
            this.parent = parent;
            this.childs = new ArrayList<>();
            this.startTime = System.currentTimeMillis();
            this.context = context;
        }

        private String dump(String firstPrefix, String nextPrefix) {
            StringBuilder result = new StringBuilder();
            recursiveDump(result, firstPrefix, nextPrefix);
            return result.toString();
        }

        private void recursiveDump(StringBuilder result, String firstPrefix, String nextPrefix) {

            result.append(firstPrefix);

            StringBuilder temp = new StringBuilder();

            if (isFinished()) {

                temp.append("耗时:{1,number}ms ");

                if (getSelfDuration() != UNKNOWN_TIME) {
                    temp.append("自身耗时:{2,number}ms ");
                }

                if (getPercentage() != UNKNOWN_TIME) {
                    temp.append("在父节点里所占时间比:{3,number,##%} ");
                }

                if (getPercentageInAll() != UNKNOWN_TIME) {
                    temp.append("在总时间里所占时间比:{4,number,##%} ");
                }

            } else {
                temp.append("[UNFINISHED ENTRY]");
            }

            temp.append("内容:{0}");

            result.append(MessageFormat.format(temp.toString(), context, getDuration(), getSelfDuration(), getPercentage(), getPercentageInAll()));

            // 递归打印子节点
            for (int i = 0; i < childs.size(); i++) {
                Entry child = childs.get(i);
                result.append(System.lineSeparator());
                if (i == childs.size() - 1) {
                    // 最后一个子节点
                    child.recursiveDump(result, nextPrefix + "`---", nextPrefix + "    ");
                } else {
                    child.recursiveDump(result, nextPrefix + "+---", nextPrefix + "|   ");
                }
            }

        }

        /**
         * 节点总时间
         */
        private long getDuration() {
            if (endTime == UNKNOWN_TIME) {
                return UNKNOWN_TIME;
            } else {
                return endTime - startTime;
            }
        }

        /**
         * 节点自身时间
         */
        private long getSelfDuration() {
            long duration = getDuration();
            if (duration == UNKNOWN_TIME) {
                return UNKNOWN_TIME;
            }
            if (childs.isEmpty()) {
                return duration;
            }
            for (Entry child : childs) {
                //TODO：子节点未结束，则统计有问题
                duration -= child.getDuration();
            }
            return duration;
        }

        /**
         * 在父节点所占时间百分比
         */
        private double getPercentage() {
            double duration = getDuration();
            double parentDuration = UNKNOWN_TIME;
            if (parent != null && parent.isFinished()) {
                parentDuration = parent.getDuration();
            }
            if (duration == UNKNOWN_TIME || parentDuration == UNKNOWN_TIME) {
                return UNKNOWN_TIME;
            }
            return duration / parentDuration;
        }

        /**
         * 在总时间里所占时间百分比
         */
        private double getPercentageInAll() {
            double duration = getDuration();
            double rootDuration = UNKNOWN_TIME;
            if (root != null && root.isFinished()) {
                rootDuration = root.getDuration();
            }
            if (duration == UNKNOWN_TIME || rootDuration == UNKNOWN_TIME) {
                return UNKNOWN_TIME;
            }
            return duration / rootDuration;
        }

        private void addChild(String context) {
            Entry child = new Entry(root, this, context);
            childs.add(child);
        }

        private boolean isFinished() {
            return endTime != UNKNOWN_TIME;
        }

        private void finish() {
            endTime = System.currentTimeMillis();
        }

        private Entry getUnFinishedChild() {
            if (childs.isEmpty()) {
                return null;
            }
            Entry last = childs.get(childs.size() - 1);
            if (last.isFinished()) {
                return null;
            }
            return last;
        }

    }

}
