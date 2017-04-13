# BookCraw
> 小说爬虫

##参数说明

- savePath
> 爬取到的文件保存路径

- threadCount
> 使用多少线程去爬取

- dep
> 网站爬取的深度

- suffix
> 网页的后缀名

- tagRex
> 目标网页的正则表达式验证

- url
> 开始的url路径

- crawClass
> 使用哪个类进行爬取

## 如果使用外部类 则使用下面参数

- external
> true or false 表示是否使用外部类 当为true的时候生效

- classPath
> 外部类的路径

- externalClass
> 外部类的完整类名需要包括报名,如 a.b.c.Abc,
类必须实现org.penitence.craw.event.HitTargetListener接口,
使用javac编译可以加上 -cp BookCraw.jar