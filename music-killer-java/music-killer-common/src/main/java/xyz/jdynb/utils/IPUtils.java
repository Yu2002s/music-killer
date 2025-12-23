package xyz.jdynb.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;

@Slf4j
public class IPUtils {

    /**
     * 获取客户端IP地址
     */
    public static String getIpAddress(@Nullable HttpServletRequest request) {
        try {
            if (request == null) {
                return "unknown";
            }

            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }

            // 处理多个IP的情况，取第一个
            if (ip != null && ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }

            return ip;
        } catch (Exception e) {
            log.error("获取IP地址失败", e);
            return "unknown";
        }
    }

}
