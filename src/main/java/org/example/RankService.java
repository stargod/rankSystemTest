package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RankService {

    private final String rankMonthRedisZSetKey = "activity:ranking:month:zset:%s";
    private Jedis jedis = RedisUtil.getJedis();
    //2023-03-01时间戳小数部分开始时间
    private long timeDecimalStart = 1709222400000L;
    //2099-03-01时间戳小数部分结束时间
    private long timeDecimalEnd = 4075977600000L;

    /**
     * 获取当前月数
     * @return
     */
    private String getFormatMonth() {
        LocalDate currentDate = LocalDate.now();

        // 创建一个自定义的日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 格式化当前日期
        String formattedDate = currentDate.format(formatter);

        return formattedDate;
    }

    private String getRankMonthRedisKey() {
        return String.format(rankMonthRedisZSetKey, getFormatMonth());
    }

    private Double getScoreWithTime(Integer score) {
        Long time = System.currentTimeMillis();
        return score + (double)(timeDecimalEnd - time) / (timeDecimalEnd - timeDecimalStart);
    }
    /**
     * 更新用户的得分
     * @param uid 用户id
     * @param score 得分
     * @return
     */
    public Boolean setUserScore(Long uid, Integer score) {
        if (score > 10000 || score < 0) {
            return false;
        }
        String rankRedisKey = getRankMonthRedisKey();
        Double beforeScore = jedis.zscore(rankRedisKey, uid.toString());
        if (beforeScore != null && beforeScore > score) {
            return  false;
        }
        jedis.zadd(rankRedisKey,getScoreWithTime(score), uid.toString());
        return true;
    }

    /**
     * 根据名次查询排行榜
     * @param month 排行榜月份格式yyyy-MM
     * @param start 开始名次
     * @param end 结束名次
     * @return
     */
    public List<RankInfo> getRankList(String month, Long start, Long end) {
        String rankRedisKey = String.format(rankMonthRedisZSetKey, month);
        List<RankInfo> result = new ArrayList<>();
        if (start <=0) {
            start = 0L;
        }
        if (end < start) {
            return  result;
        }
        Set<Tuple> rankSet = jedis.zrevrangeWithScores(rankRedisKey, start, end);
        for (Tuple tuple:rankSet) {
            start = start + 1;
            RankInfo rankInfo = new RankInfo();
            rankInfo.setUid(Long.valueOf(tuple.getElement()));
            rankInfo.setScore((int)tuple.getScore());
            rankInfo.setRank(start);
            result.add(rankInfo);
        }

        return result;
    }

    /**
     * 查询用户的前后排名
     * @param month 排行榜月份格式yyyy-MM
     * @param uid 用户uid
     * @param range 用户前后的排名用户，
     * @return
     */
    public List<RankInfo> getUserRankList(String month, Long uid, Long range) {
        String rankRedisKey = String.format(rankMonthRedisZSetKey, month);
        List<RankInfo> result = new ArrayList<>();
        Long userRank = jedis.zrevrank(rankRedisKey, uid.toString());
        if (userRank != null) {
            result = getRankList(month, userRank - range, userRank + range);
        }

        return result;
    }
}
