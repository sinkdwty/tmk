# 电销项目（TMK）

## 开发环境
- Java 1.8+
- Maven 3.2+ 
- IDEA
- SpringBoot 2.0.1.RELEASE

## 如何运行

1. 克隆项目到本地

```
git clone http://git.hjzddata.com/java/tmk.git
```
进入IDEA之后，右击pom.xml ，选择add as maven project ，即可完成导入。

2. 执行hjzd-admin 模块下的sql/database.sql 脚本，初始化项目的数据库环境

3. 拷贝配置文件`hjzd-admin/src/main/resources/application.yml.example` 为 `application.yml`  ，并修
改数据连接， 账号和密码，改为您所连接数据库的配置。

4. 执行HjzdApplication 类中的main方法，即可运行系统

5. 打开浏览器，输入localhost:8080 ，即可访问到的登录页面，默认登录账号密码:
admin/111111 

## 打包部署

> 目前项目支持两种打包方式，即jar包和war包；
> 
> 提示：若打的包为jar包，可通过`java -jar hjzd-admin-1.0.0-SNAPSHOT.jar --server.port=8090` 来启动系统；

1. 打包之前修改hjzd-admin.pom 中的packaging 节点，改为jar 或者war。
2. 在项目的根目录执行`mvn clean package -Dmaven.test.skip=true` ，即可打包。
3. 命令执行成功后，在`hjzd-admin/target` 目录下即可看到打包好的文件
提 `hzjd-admin-1.0.0-SNAPSHOT.war`。
4. 配置 Tomcat `conf/server.xml`
    ```
        <Host name="tmk-test.hjzd.com"  appBase="/home/webapps" unpackWARs="true" autoDeploy="true">
             <Context docBase="/home/webapps/tmk" path="" reloadable="true" />
             <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
                    prefix="localhost_access_log" suffix=".txt"
                    pattern="%h %l %u %t &quot;%r&quot; %s %b" />
        </Host>
    ```
5. 解压 `hzjd-admin-1.0.0-SNAPSHOT.war` 到 `/home/webapps/tmk` 目录
6. 启动 Tomcat
7. 添加本地 host 记录 `127.0.0.1 tmk-test.hjzd.com`，访问 http://tmk-test.hjzd.com:8080 
> 127.0.0.1 是 Tomcat 所在服务器ip，8080 是 Tomcat 默认端口

## 模块结构
1. hzjd-admin 模块为后台管理系统模块，包括管理系统的业务代码，前端页面，项目的配置
信息等等
2. hzjd-core 模块为抽象出的核心（通用）模块，以供其他模块调用，此模块主要封装了一
些通用的工具类，公共枚举，常量，配置等等
3. hzjd-generator 为代码生成模块，其中代码生成模块整合了mybatis-plus的代码生成器和
guns独有的代码生成器，可以一键生成entity，dao，service，html，js等代码，可减少新业
务70% 的工作量
4. hzjd-parent 模块为其他所有模块的父模块，主要功能是管理项目中所有用到的jar，以
及聚合其他模块


## 其他
> Lombok 是一种 Java™ 实用工具，可用来帮助开发人员消除 Java 的冗长，尤其是对于简单的 Java 对象（POJO）。它通过注解实现这一目的。

1. 如果您使用的为idea 只需要file -> setting->plugins->Browse Repositeories 输入 lombok 集成插件重启idea即可消除错误。
2. 如果您使用 eclipse 需要下载 lombk jar包 手动集成。
