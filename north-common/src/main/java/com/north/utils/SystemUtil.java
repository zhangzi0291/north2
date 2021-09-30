package com.north.utils;

import com.sun.management.OperatingSystemMXBean;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-21
 */
public class SystemUtil {

    public static Map<String, String> getSystemMemery() {
        Map<String, String> result = new HashMap<>();
        OperatingSystemMXBean mxBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        result.put("used", (mxBean.getTotalPhysicalMemorySize() - mxBean.getFreePhysicalMemorySize()) / 1024 / 1024 + "MB");
        result.put("total", mxBean.getTotalPhysicalMemorySize() / 1024 / 1024 + "MB");
        return result;
    }

    public static Map<String, String> getSystemCpu() {
        Map<String, String> result = new HashMap<>();
        OperatingSystemMXBean mxBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        result.put("system", String.format("%.2f", mxBean.getSystemCpuLoad() * 100));
        result.put("process", String.format("%.2f", mxBean.getProcessCpuLoad() * 100));
        result.put("loadAverage", String.format("%.2f", mxBean.getSystemLoadAverage() * 100));
        return result;
    }

    public static Map<String, Map<String, String>> getSystemDisk() {
        Map<String, Map<String, String>> result = new HashMap<>();
        File[] roots = File.listRoots();//获取磁盘分区列表
        for (File file : roots) {
            Map<String, String> rootMap = new HashMap<>();
            rootMap.put("used", (file.getTotalSpace() - file.getUsableSpace()) / 1024 / 1024 / 1024 + "G");
            rootMap.put("total", file.getTotalSpace() / 1024 / 1024 / 1024 + "G");
            result.put(file.getPath(), rootMap);
        }
        return result;
    }

}
