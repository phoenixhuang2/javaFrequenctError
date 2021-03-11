HTTP超时，重试，并发等问题

与执行本地方法不同，进行 HTTP 调用本质上是通过 HTTP 协议进行一次网络请求。网络请求必然有超时的可能性，因此我们必须考虑到这三点：
首先，框架设置的默认超时是否合理；
其次，考虑到网络的不稳定，超时后的请求重试是一个不错的选择，但需要考虑服务端接口的幂等性设计是否允许我们重试；
最后，需要考虑框架是否会像浏览器那样限制并发连接数，以免在服务并发很大的情况下，HTTP 调用的并发数限制成为瓶颈。


连接超时与读取超时：
连接超时：连接超时参数 ConnectTimeout，让用户配置建连阶段的最长等待时间；
读取超时：读取超时参数 ReadTimeout，用来控制从 Socket 上读取数据的最长等待时间。

常见误区：
1、连接超时配置得特别长，比如 60 秒；一般来说，TCP 三次握手建立连接需要的时间非常短，通常在毫秒级最多到秒级，不可能需要十几秒甚至几十秒。如果很久都
无法建连，很可能是网络或防火墙配置的问题。
2、排查连接超时问题，却没理清连的是哪里。比如：客户端通过客户端负载均衡技术连接服务端，此时出现连接超时，大概率是服务端的问题。有些服务端会通过
nginx反向代理来负载均衡，此时连接超时极大可能是nginx。

3、读取超时，服务端的程序不会中断，会继续执行


Feign和ribbon的超时配置，以及二者的优先级

并发限制：
HttpClient有默认的并发数量的限制，在针对并发量较大的业务时，需要手动设置这些参数

重试：保证幂等性
ribbon的重试
nginx的重试：proxy_next_upstream

Syntax: proxy_next_upstream error | timeout | invalid_header | http_500 | http_502 | http_503 | http_504 | http_403 | http_404 | off ...;
Default:    proxy_next_upstream error timeout;
Context:    http, server, location

结合nginx的几个超时时间
proxy_send_timeout     后端服务器数据回传时间(代理发送超时时间)
proxy_read_timeout      连接成功后，后端服务器响应时间(代理接收超时时间)
proxy_connect_timeout    nginx连接后端的超时时间，一般不超过75s
