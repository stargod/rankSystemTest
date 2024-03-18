# rankSystemTest
按月份排名排行榜

核心代码：
src/main/org.example.RankService 排行榜服务，提供更新得分，按照排名展示排行，按照用户展示范围排行方法
项目采用redis的zset结构存储用户的排行耪数据
![](https://github.com/stargod/rankSystemTest/blob/main/src/main/resources/%E6%8E%92%E5%90%8D%E7%B3%BB%E7%BB%9F.png)

如果玩家分数，触发时间均相同，则根据玩家等级，名字依次排序如何实现？
玩家分数，触发时间均相同属于小概率事件，且玩家等级，名字可能会变动。如果出现这种情况，
可以当出现玩家分数，触发时间均相同用户时，实时获取玩家的等级和名字，进行额外的排序。
