# AndroidDBManger
  Android 开发中我们一般会考虑的ORM DB，有ORMLite，GreenDao等，国内Litepal 也很火，中文资料很多；
  当然直接撸SQLITE 的也可以，只要你不觉得繁琐;那么到底哪个数据库好呢？个人觉得好或不好都是相对的。
  这个要根据项目和实际的操作人员而定，我建议熟悉Sqlite 后直接上GreenDao，当然中间你也可以使用一下Ormlite  
  看看2016年对于Android ORM DB 的评测http://greenrobot.org/android/android-orm-performance-2016/

#GreenDao 的优缺点   
  - 相关代码通过另一个普通的Java项目直接生成，不会导致性能瓶颈
  - 功能特性相当完善，版本稳定，greenrobot 2011开始的项目。
  - 文档较完善，使用者较多  
  - 库文件比较小，小于100K，编译时间低，而且可以避免65K方法限制
  - 内存占用小，支持加密，API 简单易用
  
  ##GreenDao 3.0 重要的更新说明   
  > 正式支持SQLCipher 加密。   
  > greenDao V3.X 开始使用 编译时注解 + Gradle Plugin 去生成实体和框架代码。   
  > 开始支持RxJava   
  

#Android 数据库文件导出查看   
  我们知道数据库是保存在data/data/packagename/databases 目录下面,当很长一段时间需要查看数据接中的内容时我们需要导出db文件
  然后使用SQLite Expert（Windows 平台http://www.sqliteexpert.com/）查看是非常方便的。那么怎样能把私有目录的DB文件导出来呢？
  - 连接手机 adb shell
  - run-as packagename
  - cat /data/data/packagename/databases/database-name >/sdcard/new-dbname（有些手机可能失败，建议用google Nexus调试。没有？go home）
  - 这个时候我们就可以在Sdcard 对应的目录看见new-dbname 这个DB 文件了（不要手机连接Windows用默认的文件管理器看，按照一个应用宝，或者360 手机助手）
  - 从手机的Sdcard 目录导出db 文件用SQLite Expert 打开查看吧，SQLite Expert使用就不介绍了
  ![image](https://github.com/AnyLifeZLB/DBManger_GreenDao3/raw/master/pulldb.)   
  
  当然你也可以直接在你的代码中实现拷贝功能，直接把DB文件拷贝到你制定的目录里面，见：
 
#本项目的目的   
  以前使用的数据库一路走来SQLITE --> Ormlite  --> GreenDao.封装越来越高级，使用越来越简单，也许很长的时间会一直使用GReenDao.   
  
  - 数据库DB 文件和账户对应，就是一个账号对应一个单独独立的数据库db文件
  - xxxxx
  - 数据库的基本操作
  - 数据库加密
  - 数据库升级
  
# 本项目模拟的一个场景介绍
   为了减少网络请求的次数和开销，客户端拉取的消息需要本地用数据库保存，当很长的一段时间没有拉取的时间会出现以下情况：
  
   
