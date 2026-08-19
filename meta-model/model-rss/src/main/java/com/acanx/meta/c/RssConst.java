package com.acanx.meta.c;

/**
 * RssConst
 *
 * @author ACANX
 * @since 20251005
 */
public class RssConst {

    private RssConst() {
        // 工具类，禁止实例化
    }


    /**
     * RSS API v1.1 版本的基础URL地址
     * 用于构建完整的RSS订阅源URL
     */
    public static final String RSS_URL_V1_1 = "https://www.rss.com/api/v1/rss/feed/";
    /**
     * Atom命名空间URI
     * 用于RSS feed中的Atom相关元素命名空间声明
     */
    public static final String XMLNS_ATOM = "xmlns:atom";
    /**
     * Content模块命名空间URI
     * 用于RSS feed中内容相关元素的命名空间声明
     */
    public static final String XMLNS_CONTENT = "xmlns:content";

    /**
     * Atom命名空间URI
     * 用于RSS feed中的Atom相关元素命名空间声明
     */
    public static final String URL_XMLNS_ATOM = "http://www.w3.org/2005/Atom";

    /**
     * Content模块命名空间URI
     * 用于RSS feed中内容相关元素的命名空间声明
     */
    public static final String URL_XMLNS_CONTENT = "http://purl.org/rss/1.0/modules/content/";

    /**
     * RSS feed 基础 URL
     *
     * ⚠️ 命名历史（2026-08-19 SonarCloud java:S115 规范修复）：
     * 原常量名 {@code URL_XMLNS_ATOM_}（尾部下划线不符合命名规范
     * {@code ^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$}），经确认后调整为本名。
     * 原注释称"与XMLNS_ATOM相同"，但实际值为 RSS feed 地址
     * （与 {@link #RSS_URL_V1_1} 值相同），故按真实语义命名。
     * 如需追溯旧引用，请在代码库中搜索 {@code URL_XMLNS_ATOM_}。
     */
    public static final String URL_RSS_FEED = "https://www.rss.com/api/v1/rss/feed/";


    /**
     * 简体中文语言代码常量
     *
     * 该常量定义了简体中文的语言标识符，遵循RFC 4646标准格式，
     * 用于国际化和本地化相关的语言设置。
     */
    public static final String LANGUAGE_ZH_CN = "zh-CN";

}
