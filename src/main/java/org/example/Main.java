package org.example;

import java.util.List;

public class Main {
    public static void (String[] args) {
        RankService rankService = new RankService();
        for (int i = 1; i < 100000; i++) {
            rankService.setUserScore(Long.valueOf(i), i);
        }

        List<RankInfo> rankList = rankService.getUserRankList("2024-03", 999L, 10L);

        for (RankInfo rankInfo:rankList) {
            System.out.println("用户id:" + rankInfo.getUid() + " 得分:" + rankInfo.getScore() + " 排名:" + rankInfo.getRank());
        }
    }
}