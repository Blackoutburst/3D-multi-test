package dev.blackoutburst.game.utils;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class SystemMonitor {
    public static int getCPUProcessUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        return (int) (osBean.getProcessCpuLoad() * 100);
    }

    public static int getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();

        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        return (int) ((totalMemory - freeMemory)  / (1024 * 1024));
    }

    public static int getMemoryTotal() {
        Runtime runtime = Runtime.getRuntime();
        return (int) (runtime.totalMemory() / (1024 * 1024));
    }

    public static int getMemoryFree() {
        Runtime runtime = Runtime.getRuntime();
        return (int) (runtime.freeMemory() / (1024 * 1024));
    }

    public static int getThreadCount() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        return threadBean.getThreadCount();
    }
}
