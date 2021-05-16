## 社招面经
### 知乎
交流了1h,自我感觉回答还可以，主要原因是个人主要做的toB业务，toC经验缺乏,知乎用golang
#### 一面(挂)
##### 网络
   + timewait 为什么等待2MSL?   
      - 保证TCP协议的全双工连接能够可靠关闭   
     如果Client直接CLOSED了，导致Server没有收到Client最后回复的ACK。那么Server就会在超时之后继续发送FIN，此时由于Client已经CLOSED了，就找不到与重发的FIN对应的连接，最后Server就会收到RST而不是ACK，Server就会以为是连接错误把问题报告给高层。这样的情况虽然不会造成数据丢失，但是却导致TCP协议不符合可靠连接的要求。所以，Client不是直接进入CLOSED，而是要保持TIME_WAIT，当再次收到FIN的时候，能够保证对方收到ACK，最后正确的关闭连接。
      - 保证这次连接的重复数据段从网络中消失
     TIME_WAIT状态等待2MSL，这样可以保证本次连接的所有数据都从网络中消失   
     这2个MSL中第一个MSL是为了等自己发出去的最后一个ACK从网络中消失，而第二MSL是为了等在对端收到ACK之前的一刹那可能重传的FIN报文从网络中消失。
   + 流量控制的机制   
     -[滑动窗口](https://zhuanlan.zhihu.com/p/133307545)
   - 网络IO模型有   
      - 阻塞IO
      - 非阻塞IO
      - IO多路复用
      - 信号驱动IO
      - 异步IO  (唯一的程序非阻塞)
   -  IO多路复用 （select/poll/epoll）  
   [多路复用](https://juejin.cn/post/6844904200141438984)  
      - select是把fd_Set从用户态拷贝到内核,内核轮询去监听fd数组,一旦某fd活跃,把整个fd_set从内核拷贝到用户态，socket个数有限制，一般为1024个,时间复杂度O(n)；   
      - poll本质上和select没有区别,无最大链接数限制(原因是链表存储)；  
      - epoll是event poll,事件驱动IO，当socket有流时会主动回调告知用户进程，时间复杂度O(1)。
         - 通过内核与用户空间mmap(内存映射)同一块内存,降低用户态/内核态数据交换
         - 红黑树存储所有socket/链表list存储活跃socket   
       [select与epoll区别](https://www.jianshu.com/p/430141f95ddb)
   -  close wait 说明了什么？  
      - 服务器端的代码，没有写 close 函数关闭 socket 连接，也就不会发出 FIN 报文段；或者出现死循环，服务器端的代码永远执行不到 close。  
      - close_wait的危害在于，在一个进程上打开的文件描述符超过一定数量，（在linux上默认是1024，可修改），新来的socket连接就无法建立了，因为每个socket连接也算是一个文件描述符。
   -  Rpc 调用服务timeout,排查的思路
      - 查看消费者/服务提供者的超时时间是否合理
      - 检查消费者和服务提供者的网络连接是否OK（telnet/nc命令）
      - 查看服务提供者的日志信息进行排查
      - 查看服务提供者业务逻辑是否有db操作，有的话check是否有慢查询
      - 查看服务提供者的堆栈信息，有无线程hang/有无死锁/有无阻塞等待
      - 查看服务提供者是否有内存溢出
      
#####　kafka
   1. kafka的架构是  
   2. 消费者数量一定时候如何提高消费能力 [提供消费能力](https://www.jianshu.com/p/4e00dff97f39)
   3. 吞吐量大的原因   
      - 日志顺序读写和快速检索  
      - partition机制 (并行但不保证全topic有序)  
      - 批量发送接收和数据压缩机制  
      - 通过sendfile实现零拷贝原则   [零拷贝](https://zhuanlan.zhihu.com/p/78335525)
         - 以前是文件->内核->用户缓冲区->内核socket->消费者进程  
         - 零拷贝是调用linux系统函数, 文件->内核read buffer->内核socket->消费者进程 
    

##### Redis
   * zset 的底层实现以及检索复杂度
      - 字典dict+ 跳表zskiplist
         - dict可以O(1)检索单个元素
         - zskiplist可以O(logn)检索范围 
            - zskiplist没有做插入的去重（会存在同一个key，插入两次）  
   * 说下持久化策略(rdb/aof)
      - rdb 和aof 若持久化时有新的请求过来是怎么处理？   
   [rbd 写时复制](https://blog.csdn.net/weixin_38405253/article/details/106416618)   
   [aof机制](https://redisbook.readthedocs.io/en/latest/internal/aof.html)   
   [aof重写](http://doc.redisfans.com/topic/persistence.html)
      - aof 重写: fork子进程写新aof文件，期间新的写命令，父进程一边累积到内存cache,一边写到旧aof文件,子进程重写完成发信号给父进程，父进程将内存cache数据追加到新aof，并替换老aof
   * 分布式锁实现   
ex表示expire, nx表示if not exist
```
set redis-key redis-value ex 5 nx      
```
解锁 del redis-key   
为防止误解锁,redis-value可设置为可标识当前线程的唯一id,eg:UUID   
[Redis 分布式锁的正确实现方式](https://mp.weixin.qq.com/s/qJK61ew0kCExvXrqb7-RSg)   
[分布式锁的实现之 redis 篇](https://xiaomi-info.github.io/2019/12/17/redis-distributed-lock/)
##### Mysql
- 基本了解   

##### golang
使用情况   
### Boss直聘
通过
#### 一面
##### 微服务
- 介绍微服务
- 微服务包含的模块有哪些

##### Kafka
- 基本介绍

##### Spring/boot
- spring 特性
- springboot 相比spring的好处
- aop使用的场景    
[aop原理以及实现](https://blog.csdn.net/u010452388/article/details/80868392)
   - 记录日志
   - 监控方法的运行时间
   - 权限控制
   - 事务管理 （调用方法前开启事务， 调用方法后提交关闭事务 ）

##### Redis
- 基本介绍
- 持久化策略的区别以及优点缺点

#### 二面
##### java
   - java所有类的父类是(object),里面有几个方法，为什么有hashcode这个方法，用途是
      - object中的hashcode函数用于将对象的内存地址转换为整数返回，用途是提高查找/比较对象（避免了多次equal）效率
      - Java中类的hashCode是使用在类似哈希表的数据结构中的，如HashMap、HashSet、HashTable等，用于定位元素的位置，在其它地方没有什么意义！
   - 在synchronized外面notify不行吗？（问的是notify的底层原理了，每个锁都有一个队列的，离开了synchronized就不知道notify哪些线程）
      - wait是本地方法，wait方式是通过对象的monitor对象来实现的，所以只要在同一对象上去调用notify/notifyAll方法，就可以唤醒对应对象monitor上等待的线程
   - 红黑树的特点是？hashmap为什么要用红黑树？
      - 红黑树降低了平衡性，对于频繁改动节点的自旋开销相对其他树低,最好/最坏复杂度保证O(logN)
      - 红黑树可以能确保树的最长路径不大于两倍的最短路径的长度
      [红黑树特点](https://blog.csdn.net/m0_37264516/article/details/84138212)
      [红黑树的适用场景](https://www.cnblogs.com/but999/p/12643956.html)
   
##### 设计
   - 项目的设计的原因是啥
   - 项目里面最深刻的问题以及最怎么解决的
   
### soul（广告推荐）
#### 一面  
#####项目介绍   
画出项目结构图 
##### springboot
   - 循环引用解决方法 
      - [三级缓存](https://blog.csdn.net/u010853261/article/details/77940767) 单例对象初始化:createBeanInstance实例化(解决循环依赖关键：是构造后调用addSingletonFactory提前曝光bean到三级缓存中)->populateBean填充属性->initializeBean 
      - @lazy 懒加载，动态代理创建了一个类，返回一个代理bean[lazy加载](https://blog.csdn.net/weixin_43859729/article/details/111059470)
   - aop在spring中的体现是
      - 拦截器就是aop的一种体现，基于动态代理实现
   - hashmap的实现
      - 为什么是数组+链表   
      链表是为了解决哈希冲突,jdk1.8前是数组+链表，之后是数组+链表/红黑树
      - 链表的长度有限制吗
      链表长度大于8时并且数组长度>=64，红黑树操作，否则只是resize扩容
   - threadLocal
      - 线程安全的，本质是内部的静态map,线程本地存储

##### 网络
   - cookies 和 session的优缺点   
      - cookies
##### mysql
   - 又被问到了,`需要开始搞mysql的原理`
##### kafka的使用
   - 介绍了使用以及分区机制
##### 系统设计
   - 以发微博为例，A用户发布了动态消息，只有好友能看到，如何设计
##### 算法题
```
输入是a="cbacbfhj",b="abc",也就是a中包含b的所有起始index
返回[0,1,2]
```

#### 二面
##### docker怎么分层存储的？问的底层实现
##### mysql
##### mongo遇到什么问题，mongo的底层实现/索引
##### 设计
   -  滑动时间窗口内的限流实现(zset)
```
def is_action_allowed(user_id, action_key, period, max_count):
    key = 'hist:%s:%s' % (user_id, action_key)
    now_ts = int(time.time() * 1000) # 毫秒时间戳
    with client.pipeline() as pipe: 
    pipe.zadd(key, now_ts, now_ts) # value 和 score 都使用毫秒时间戳
    pipe.zremrangebyscore(key, 0, now_ts - period * 1000) # 移除时间窗口之前的行为记录，剩下的都是时间窗口内的
    pipe.zcard(key)    # 获取窗口内的行为数量
    _, _, current_count, _ = pipe.execute()
    pipe.expire(key, period + 1)
    return current_count <= max_count # 比较数量是否超标
```
   - 有1亿用户,每个用户有100个tag,有100W个广告,每个广告有100个tag,设计找出最匹配用户的广告
##### 线程池
   - [线程池原理](https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html)
      - 线程池初始化配置corePoolSize为3，依次提交3个任务执行后，线程池里有3个线程
      - 线程6种状态(NEW Runnable WAITING TIMED_WAITING TERMINATED BLOCKED)
      
### 美团
体验很棒
#### 一面
##### kafka的追溯
##### Redis的持久化策略
##### java/sprintboot
   - springboot 的循环引用解决
   - 一个线程oom，会影响其他线程运行吗
      - 答案是不会影响,因为当一个线程抛出OOM异常(无论堆还是栈)后，它所占据的内存资源会全部被释放掉，从而不会影响其他线程的运行(线程不像进程，一个进程中的线程之间是没有父子之分的，都是平级关系。即线程都是一样的, 退出了一个不会影响另外一个)

### 商汤一面
##### 算法
手写生产者消费者
##### kafka
   - 为什么要用消费组
      - 高性能，同时消费多分区
      - 故障容错，消费组会对其成员进行管理，在有消费者加入或者退出后，消费者成员列表发生变化，消费组就会执行再平衡的操作
         - 重平衡就是让一个Consumer Group下所有的Consumer实例,合理分配消费订阅topic的所有分区的过程
      - 消费模式灵活
   - 消费者组中的一个消费者down掉，kafka如何处理？
      - [消费者重平衡](https://blog.csdn.net/qq_21383435/article/details/108720155)
   - 对于kafka的分区副本，写入一条消息如何才算写入成功？ [kafka副本机制](https://www.cnblogs.com/caoweixiong/p/12049462.html)
      - 分区leader的ISR同步副本中都向分区leader发送ack，leader才会commit
   - kafka的分区leader挂掉后，怎么处理？ 
##### mongo
   - 介绍mongo的底层实现
   - 如果自己设计mongo索引怎么做？
   - 数据在mongo存储的形式是
##### java   
- Java内存模型与Java内存区域的划分是不同的概念层次,Java内存模型中规定所有变量都存储在主内存，主内存是共享内存区域，所有线程都可以访问，但线程对变量的操作(读取赋值等)必须(线程私有内存)在工作内存中进行   
 [Java内存模型](https://blog.csdn.net/javazejian/article/details/72772461) 
##### 项目设计
   - KV具体设计  

### 网易
####一面
##### 算法
- 不用递归实现斐波那契数列
##### spring/springboot
- springboot相比较spring的优缺点
   - springboot 快速开发，配置简单(spring是xml配置)，但是也由于不用自己配置，报错时难定位
- spring 解决循环依赖方法
- 常用的spring注解
- 介绍spring 依赖注入
- 类加载器的了解
- 常用几种锁，synchronize和reetrantlock的区别是
##### mongo和mysql的索引区别？
mongo底层索引是用B树，mysql是用B+树存储[mongo和mysql索引实现](https://www.cnblogs.com/rjzheng/p/12316685.html)
##### 系统设计
- 设计扫码登录
   - [实现扫码登录的设计思想](https://www.cnblogs.com/sxdcgaq8080/p/10685818.html)
   - [扫码登录实现原理](https://cloud.tencent.com/developer/article/1347341)
   
##### GC收集器对比   
- CMS
   - 目的：获取最短回收停顿时间为目标
   - 标记-清除算法实现
   - 运行过程：
      - 初始标记：stop the world，stop用户线程,标记GcRoots直接关联的对象，速度很快
      - 并发标记：垃圾收集线程与用户线程并发，从GC Roots直接关联对象扫描整个对象图，耗时长（增量更新：解决并发标记时用户线程修改导致的对象消失问题）
      - 重新标记：stop the world,修正并发标记期间用户线程运作导致标记变动的那一部分对象记录
      - 并发清除：清理已标记死亡对象，无须移动存活对象，用户线程可并发
   - 优点：并发收集，低停顿
   - 缺点：
      - 1.对处理器资源敏感，CMS默认启动回收线程数=(处理器核心数+3)/4，当处理器核心数<4,cms的回收线程占据很大运算资源导致用户程序变慢
      - 2.无法处理浮动垃圾，无法处理并发标记/清理期间用户线程产生的新垃圾对象，只能等待下一次垃圾回收
      - 3.标记清除会产生大量空间碎片
- G1   
   - 特点：
```面向局部收集和基于region的内存布局，不再坚持固定大小以及固定数量的分代区域划分，
  把java堆划分为多个大小相等的region，每个region可扮演eden/survivor/老年代，
  ,region中有一类特殊的Humongous区域，用于存放大对象，主要面向服务端应用，可预测停顿时间（-XX:MaxGCPauseMills,默认200ms）
  ，收集器在后台维护了一个优先列表，每次根据允许的收集时间，优先选择回收价值最大的 Region
``` 
   - 整体看是"标记-整理算法"，从局部看（两个regionz之间）是"标记-复制算法"
   - 运行过程：
      - 初始标记：标记GcRoots直接关联的对象，stop the world，stop用户线程
      - 并发标记：从GcRoots开始对堆递归扫描，与用户线程并发（原始快照法：解决并发标记时用户线程修改导致的对象消失问题，每个region里面有TAMS空间，供并发时用户线程的新对象分配）
      - 最终标记：stop the world,修正并发标记期间用户线程运作导致标记变动的那一部分对象记录
      - 筛选回收：stop the world,stop用户线程，根据用户配置的停顿时间，选择多个region组成回收集，标记复制回收region
   - 优点：有限时间内可以尽可能高的收集效率(内存化整为零)，无内存碎片
   - 缺点：
      - 内存占用：G1和CMS都使用**卡表**来处理跨代引用，但G1实现复杂，占用更多内存
      - 执行负载：CMS用***写后屏障来维护卡表，G1同时使用写前屏障和写后屏障，所以消耗更多运算资源
   - reference   
  写屏障：hotspot虚拟机通过**写屏障**来维护卡表状态（解决对象跨代引用问题），写屏障可看作虚拟机层面对类型字段赋值的aop切面，在引用对象赋值时产生环形通知
- 选G1和CMS的依据
   - 小内存应用CMS表现大概率优于G1，而大内存应用G1大多发挥较好，这个优劣势的Java堆容量平衡点在6GB-8GB