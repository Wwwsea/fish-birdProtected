package com.fish.birdProducted.constants;

/**
 * @author fish
 * <p>
 * 创建时间：2023/10/16 20:56
 * RabbitMq常量类
 */
public class RabbitConst {

    /**
     * 邮件队列
     */
    public static final String MAIL_QUEUE = "email_queue";

    /**
     * 登录日志队列
     */
    public static final String LOG_LOGIN_QUEUE = "log_login_queue";

    /**
     * 系统操作日志队列
     */
    public static final String LOG_SYSTEM_QUEUE = "log_system_queue";


    /**
     * 邮件交换机
     */
    public static final String EMAIL_EXCHANGE = "email.topic";



    /**
     * 邮件HTML队列
     */
    public static final String EMAIL_HTML_QUEUE = "email.html.queue";

    /**
     * 邮件Simple RoutingKey
     */
    public static final String EMAIL_SIMPLE_KEY = "email.simple.key";

    /**
     * 邮件Html RoutingKey
     */
    public static final String EMAIL_HTML_KEY = "email.html.key";

    /**
     * 文章交换机
     */
    public static final String ARTICLE_EXCHANGE = "article_exchange";

    /**
     * 文章队列
     */
    public static final String ARTICLE_QUEUE = "article_queue";

    /**
     * 文章RoutingKey
     */
    public final static String ARTICLE_KEY = "article_key";
}
