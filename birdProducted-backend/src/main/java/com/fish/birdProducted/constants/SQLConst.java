package com.fish.birdProducted.constants;

/**
 * @author fish
 * <p>
 * 创建时间：2024/10/16 0:01
 */
public class SQLConst {
    /**
     * 推荐文章的字段标识
     */
    public static final String RECOMMEND_ARTICLE = "1";

    /**
     * 随机文章数量
     */
    public static final Integer RANDOM_ARTICLE_COUNT = 5;

    /**
     * 相关文章数量
     */
    public static final Integer RELATED_ARTICLE_COUNT = 5;

    /**
     * 公开文章的字段标识
     */
    public static final Integer PUBLIC_ARTICLE = 1;


    /**
     * 没有删除标记
     */
    public static final Integer NO_DELETED = 0;

    /**
     * 文章状态
     */
    public static final String STATUS = "status";
    /**
     * 公开
     */
    public static final Integer STATUS_PUBLIC = 1;

    /**
     * 文章id
     */
    public static final String ARTICLE_ID = "1";

    /**
     * 灭绝动物
     */
    public static final Integer LIVE_TYPE = 3;


    /**
     * 评论是否通过(0,否)
     */
    public static final Integer COMMENT_IS_CHECK = 1;

    /**
     * 是否通过(0,否)
     */
    public static final String IS_CHECK = "is_check";

    /**
     * 通过
     */
    public static final Integer IS_CHECK_YES = 1;

    /**
     * 创建时间
     */
    public static final String CREATE_TIME = "create_time";

    /**
     * id
     */
    public static final String ID = "id";
    /**
     * 管理员id
     */
    public static final Long ADMIN_ID = 1L;

    /**
     * 查询一条
      */
    public static final String LIMIT_ONE_SQL = "LIMIT 1";
}
