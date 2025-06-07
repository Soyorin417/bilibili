package com.backend.bilibili.service.recommend.cofig;

public class BehaviorWeights {
    public static final double LIKE = 1.0;
    public static final double COIN = 3.0;
    public static final double COLLECT = 2.0;
    public static final double WATCH_BASE = 0.5;
    public static final double COMMENT = 1.5;
    public static final double DANMAKU = 1.2;
    public static final double SHARE = 3.5;
    public static final double BLOCK = -5.0; // 拉黑

    public static double getScoreByAction(String action) {
        switch (action.toLowerCase()) {
            case "like": return LIKE;
            case "unlike": return -LIKE;
            case "collect": return COLLECT;
            case "uncollected": return -COLLECT;
            case "coin": return COIN; // 投币不可取消
            case "watch": return WATCH_BASE;
            case "comment": return COMMENT;
            case "danmaku": return DANMAKU;
            case "share": return SHARE;
            case "unshared": return -SHARE;
            case "block": return BLOCK;
            default: return 0;
        }
    }
}
