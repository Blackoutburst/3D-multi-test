package dev.blackoutburst.game.utils;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class SystemMonitor {
    private static final OperatingSystemMXBean OS_BEAN = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    private static final Runtime RUNTIME = Runtime.getRuntime();
    private static final ThreadMXBean THREAD_BEAN = ManagementFactory.getThreadMXBean();
    
    public static int getCPUProcessUsage() {
        return (int) (OS_BEAN.getProcessCpuLoad() * 100);
    }

    public static int getMemoryUsage() {
        long totalMemory = RUNTIME.totalMemory();
        long freeMemory = RUNTIME.freeMemory();
        return (int) ((totalMemory - freeMemory)  / (1024 * 1024));
    }

    public static int getMemoryTotal() {
        return (int) (RUNTIME.totalMemory() / (1024 * 1024));
    }

    public static int getMemoryFree() {
        return (int) (RUNTIME.freeMemory() / (1024 * 1024));
    }

    public static int getThreadCount() {
        return THREAD_BEAN.getThreadCount();
    }
}
