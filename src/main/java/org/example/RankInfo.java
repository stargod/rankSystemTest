package org.example;

public class RankInfo {
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 排名
     */
    private Long rank;
    /**
     * 得分
     */
    private Integer Score;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Integer getScore() {
        return Score;
    }

    public void setScore(Integer score) {
        Score = score;
    }
}
