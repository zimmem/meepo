Meepo
=============
  
- 根据策略分配主库/从库  
- 根据策略实现多 slave 负载均衡 

基于数据源及事务实现数据库读写分离的一种方案， 对开发人员几乎透明。

Who is Meepo.
--------------

Meepo 是游戏 Dota 中的一个英雄， 一般称狗头人或大忽悠。 它的招牌技能是有 3 个跟主英雄一模一样的分身， 如同数据库架构中的  Master/Slave 模式。 因此本项目以此命名。

业界怎么实现对开发人员的透明的读写分离方案？
----------------------------------------------

比较熟知的方案都是架设一个中间代理， 解析 SQL 来确定 SQL 是读是写， 从而决定路由到主库还是从库。  
比较出名的有  
- mysql 自已的  mysql-proxy  
- 阿里巴巴出品的  Amoeba  

这种方案存在一些问题，  中间代理是一个单点， Amoeba 好像可以配置 HA， 但相对来说代价较高， 对事务支持不好。

Meepo 是怎么实现的？
--------------------

通过 `DataSourceWrapper` 代理 master/slave 对应的 DataSource, 当调用  getConnection 时根据上下文确定从哪个 DataSource 获取 Connection.
目前有两个默认实现   
- `MeepoDataSourceTransactionManager` 利用事务管理器， 当当前事务为只写事务时， 分配 slave connection ， 否则分配 master connection.  
- `EnforceMasterStrategy` 通过 AOP 实现， 当 service 方法上带有 `@EnforceMaster` 注解时， 强制分配主库， 否则分配从库  
以上两个策略一般配合使用， 如需自定义策略， 可实现 `MasterSlaveStrategy` 接口。

 