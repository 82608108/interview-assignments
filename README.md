## 这是一个短网址服务API
---
#使用示例：
+ http://localhost:8080/compress?sourceUrl=www.baidu.com
+ http://localhost:8080/decompress?shortUrl=a.cn/3

--- 
#设计思路：
+ 对需要转换的源网址用long类型的数字进行编号 
+ 把lang类型的编号转换成64进制 
+ 64进制下8位的长度几乎可以表示281,474,976,710,656个地址 