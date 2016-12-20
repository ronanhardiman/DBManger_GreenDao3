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
 
#本项目的目的   
  以前使用的数据库一路走来SQLITE --> Ormlite  --> GreenDao.封装越来越高级，使用越来越简单，也许很长的时间会一直使用GReenDao.   
  
  - 数据库DB 文件和账户对应，就是一个账号对应一个单独独立的数据库db文件
  - 数据库加密
  - xxxxx
  - 数据库的基本操作
  - 
  
   
