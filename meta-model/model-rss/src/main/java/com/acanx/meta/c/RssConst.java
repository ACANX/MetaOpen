package com.acanx.meta.c;

/**
 * RssConst
 *
 * @author ACANX
 * @since 20251005
 */
public class RssConst {


    /**
     * RSS API v1.1 版本的基础URL地址
     * 用于构建完整的RSS订阅源URL
     */
    public static final String RSS_URL_V1_1 = "https://www.rss.com/api/v1/rss/feed/";

    /**
     * Atom命名空间URI
     * 用于RSS feed中的Atom相关元素命名空间声明
     */
    public static final String XMLNS_ATOM = "http://www.w3.org/2005/Atom";

    /**
     * Content模块命名空间URI
     * 用于RSS feed中内容相关元素的命名空间声明
     */
    public static final String XMLNS_CONTENT = "http://purl.org/rss/1.0/modules/content/";

    /**
     * Atom命名空间URI副本
     * 与XMLNS_ATOM相同，可能是为了代码中的不同使用场景而定义
     */
    public static final String XMLNS_ATOM_ = "https://www.rss.com/api/v1/rss/feed/";



    /**
     * 简体中文语言代码常量
     *
     * 该常量定义了简体中文的语言标识符，遵循RFC 4646标准格式，
     * 用于国际化和本地化相关的语言设置。
     */
    public static final String LANGUAGE_ZH_CN = "zh-CN";

}
