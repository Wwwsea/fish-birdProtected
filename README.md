> 设计（论文）的目的：
>
> 在数字时代背景下，博物馆的线上展示逐渐成为一种重要趋势。设计一个关于鸟类博物馆的线上展示程序，有利于数字化传承与普及，通过线上展示，将鸟类知识、文化和历史传承给更广泛的受众。不仅能够吸引鸟类爱好者，还能让更多人了解鸟类的多样性和生态意义；同时拓宽观众群体，传统博物馆通常需要身体到场，而线上展示能够突破地域限制，吸引全球观众。尤其对于那些无法亲临博物馆的人，线上展示提供了便利；在教育与科普上线上展示程序可以结合多媒体、虚拟现实等技术，生动地展示鸟类的特点、栖息地、行为习性等。这有助于教育公众，提高环保意识，促进生态平衡。从而进一步推进科学研究与保护，线上展示不仅是展示，还可以收集用户反馈、数据统计等，为鸟类研究和保护提供有价值的信息。例如，用户观看次数、喜好等可以帮助博物馆更好地规划展览；最后线上展示能够促进国际间的文化交流。与国外的鸟类博物馆合作，共同举办线上展览，推动鸟类保护和研究的国际合作

# 技术介绍

- 设计页面美观，响应式布局
- 后台管理基于 Antdv Pro 后台通用框架二次开发
- 采用 RABC 权限模型，使用 SpringSecurity 进行权限管理
- 通过第三方登录和前端保持**半长链接映射**关系，实现系统自动登录；
- 将用户注册、收藏、系统消息发送到RabbitMQ，实现消息的**异步解耦**
- 通过Canal框架实现了MySQL与ElasticSearch的数据同步，确保了实时搜索的准确性和高效性。
- 利用 RestHighLevelClient 接入 ElasticSearch 的全文搜索能力，极大提升了首页文章的关键字检索速度，同时兼容 ES 未安装时继续走 MySQL 的查询逻辑。
- 通过Redis实现计数统计鸟信息热度排行，并通过先写MySQL，再删除Redis的方案来保证高并发场景下的缓存一致性。
- 基于 ThreadLocal 在登录校验拦截器中封装线程隔离的全局上下文，以便在线程内部存储用户信息，减少用户信息的数据库查询次数。
- 集成本地缓存 Guava 和 Caffeine，有效提高服务的吞吐率、**QPS 近 30%**；
- 为了满足在高并发场景下业务 ID 的唯一性和可追溯性，实现了一套基于雪花算法（Snowflake）的 ID 生成方案，进一步**降低了ID生成的延迟**。
- 采用自旋锁策略优化缓存架构，针对热 key 的并发访问进行同步，防止其失效时导致的**缓存击穿**
- 整合**AI 助手**通过 WebSocket 实现前后端的及时通信，并且通过异步流的方式实现消息提示；
- 结合 MyBatis 拦截器和 DFA 算法实现了一套完善的**敏感词自定义**过滤方案，确保了网站内容的健康和安全。
- 接入第三方 gitee、github登录，减少注册成本
- 实现日志管理（操作、登录），服务监控、用户、菜单、角色、权限管理
- 使用 自己搭建 minio 进行图片存储（避免了使用第三方对象存储被刷流量问题）
- 使用 Spring Aop + Redis 对接口进行了限流处理（每分钟）,后端使用 JSR 303 对参数校验，使用 Spring Aop + RabbitMQ 对后台操作日志处理
- 采用 Restful 风格的 API，注释完善，后端代码使用了大量 stream 流编程方式，代码非常美观
- ……

**前台前端（线上展览）：** Vue3 + Pinia + Vue Router + TypeScript + Axios + Element Plus + Echarts……

**后台启动（管理）：** Vue3 + Pinia + Vue Router + TypeScript + Axios + Antdv Pro + Ant Design Vue……

**后端：** JDK17 + SpringBoot3 + SpringSecurity + Mysql + Redis + Quartz + RabbitMQ + Minio + Mybatis-Plus + Nginx + Docker + ElasticSearch……

后台管理员账号密码：fish 123456

fd77585725deb614c915e48329c5f079

https://gitee.com/seachil/typore-bed-diagram

## 页面展示

<img src="https://s21.ax1x.com/2024/07/17/pkIqEQg.jpg" style="zoom:55%;text-align: center;"/>

<img src="https://s21.ax1x.com/2024/07/17/pkIqZLj.jpg" style="zoom:55%;text-align: center;"/>

<img src="https://s21.ax1x.com/2024/07/17/pkIqASS.jpg" style="zoom:55%;text-align: center;"/>

<img src="https://s21.ax1x.com/2024/07/17/pkIqmes.jpg" style="zoom:55%;text-align: center;"/>

<img src="https://s21.ax1x.com/2024/07/17/pkIquoq.jpg" style="zoom:55%;text-align: center;"/>

<img src="https://s21.ax1x.com/2024/07/17/pkIqVyQ.jpg" style="zoom:55%;text-align: center;"/>

<img src="https://s21.ax1x.com/2024/07/17/pkIqMF0.jpg" style="zoom:55%;text-align: center;"/> 

<img src="https://s21.ax1x.com/2024/07/17/pkIbOzD.png" style="zoom:55%;text-align: center;"/><img src="https://s21.ax1x.com/2024/07/17/pkIqEQg.jpg" style="zoom:55%;text-align: center;"/><img src="https://s21.ax1x.com/2024/07/17/pkIqEQg.jpg" style="zoom:55%;text-align: center;"/>



## node

node16.17.0:  https://nodejs.org/dist/v16.17.1/node-v16.17.1-x64.msi

pnpm 8.15.4:  命令框  npm install -g pnpm

​						nvm ls

​						nvm use 版本号

​						pnpm v

pnpm cache clear --force         //清除依赖

pnpm install

pnpm run dev/[build]



## Vue

pnpm uninstall 包名

opt: http://www.erlang.org/download/otp_win64_26.0.exe



## RabbiMQ:

 https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.13.0/rabbitmq-server-3.13.0.exe

```
启动net start RabbitMQ
浏览器打开http://127.0.0.1:15672/
```

## minio: 

https://min.io/download#/windows

```
启动：
  D:\EXPLOIT\minio\bin>.\minio.exe server D:\TOOL\MinIO\data --console-address "127.0.0.1:9000" --address "127.0.0.1:9005"
```



## 登录认证：

开发人员认证页面：https://github.com/settings/applications/new

>  
>
>  1. 网站让用户跳转到 GitHub。
>
>  2. GitHub 要求用户登录，然后询问" 网站要求获得 xx 权限，你是否同意？"
>
>  3. 用户同意，GitHub 就会重定向回  网站，同时发回一个授权码。
>
>  4. 网站使用授权码，向 GitHub 请求令牌。
>
>  5. GitHub 返回令牌.
>
>  6. 网站使用令牌，向 GitHub 请求用户数据。



## 细粒度访问控制



## WebGL：

WebGL是一种用于在Web浏览器中呈现交互式3D和2D图形的JavaScript API。它可以与Vue.js和后端Spring Boot集成，用于实现更复杂的3D效果



## adtdv-pro设计模板

[模板github地址](https://github.com/antdv-pro/antdv-pro)



## 3D模型

https://blog.csdn.net/m0_56023096/article/details/135604100
