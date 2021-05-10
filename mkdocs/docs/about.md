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
- aop使用的场景    [aop原理以及实现](https://blog.csdn.net/u010452388/article/details/80868392)
   - 记录日志
   - 监控方法的运行时间
   - 权限控制
   - 事务管理 （调用方法前开启事务， 调用方法后提交关闭事务 ）

##### Redis
- 基本介绍
- 持久化策略的区别以及优点缺点