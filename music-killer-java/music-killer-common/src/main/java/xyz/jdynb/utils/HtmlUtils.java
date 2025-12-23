package xyz.jdynb.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlUtils {

    public static String parseNuxtState(String url) throws Exception {
        // 获取 HTML
        Document doc = Jsoup.connect(url).get();

        // 找所有 <script> 标签
        Elements scripts = doc.getElementsByTag("script");
        String nuxtJson = null;

        for (Element script : scripts) {
            for (DataNode node : script.dataNodes()) {
                String data = node.getWholeData().trim();

                if (data.startsWith("window.__NUXT__") || data.contains("window.__NUXT__")) {
                    // 找到文本，比如 "window.__NUXT__ = {...};"
                    int idx = data.indexOf("window.__NUXT__");
                    int eq = data.indexOf("=", idx);
                    if (eq < 0) continue;

                    String afterEq = data.substring(eq + 1).trim();
                    // 移除前缀或结尾分号
                    if (afterEq.endsWith(";")) {
                        afterEq = afterEq.substring(0, afterEq.length() - 1);
                    }
                    // 有可能前面还有 "window.__NUXT__ =" + 空格等
                    nuxtJson = afterEq;
                    break;
                }
            }
            if (nuxtJson != null) break;
        }

        if (nuxtJson == null) {
            throw new IllegalStateException("window.__NUXT__ data not found");
        }

        return nuxtJson;
    }

}
