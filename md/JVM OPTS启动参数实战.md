# PROD JAVA OPTS启动参数说明

## Tomcat

因为是通过web容器Tomcat启动，故相关配置在Tomcat/bin目录下

```
/opt/tomcat/apache-tomcat/bin

.
├── bootstrap.jar
├── catalina.bat
├── catalina.sh
├── catalina-tasks.xml
├── commons-daemon.jar
├── commons-daemon-native.tar.gz
├── configtest.bat
├── configtest.sh
├── daemon.sh
├── digest.bat
├── digest.sh
├── setclasspath.bat
├── setclasspath.sh
├── setenv.sh
├── setenv.sh20180515
├── shutdown.bat
├── shutdown.sh
├── startup.bat
├── startup.sh
├── tomcat-juli.jar
├── tomcat-native.tar.gz
├── tool-wrapper.bat
├── tool-wrapper.sh
├── version.bat
└── version.sh

```

setenv.sh 文件里设置启动环境配置

### OPTS
```
cat /opt/tomcat/apache-tomcat/bin/setenv.sh
#!/bin/sh

UMASK=0022

JAVA_OPTS=" $JAVA_OPTS 
-Dspring.profiles.active=prod
-Dfile.encoding=UTF-8 
-Xmx1500m -Xms1500m 
-XX:SurvivorRatio=8 
-XX:+PrintTenuringDistribution 
-XX:+PrintGCDetails 
-XX:+PrintGCDateStamps 
-XX:+PrintGCTimeStamps 
-verbose:gc 
-Xloggc:${CATALINA_BASE}/logs/gc-$(date +%Y-%m-%d).log 
-XX:GCLogFileSize=100M 
-XX:+UseGCLogFileRotation 
-XX:NumberOfGCLogFiles=5 
-XX:+HeapDumpOnOutOfMemoryError 
-XX:HeapDumpPath=${CATALINA_BASE}/logs/heapdump.hprof "
```

#### 常规设置
- -Dspring.profiles.active=prod 启用spring环境prod
- -Dfile.encoding 设置文件字符编码
- -Xmx、-Xms 设置堆启动时大小、设置堆最大大小
- -XX:SurvivorRatio=8 设置Eden与Survior大小占比，默认为 8:1:1，此值意思为 8:1:1

#### GC设置
- -XX:+PrintTenuringDistribution 输出显示在survivor空间里面有效的对象的岁数情况
- -XX:+PrintGCDetails 打印GC详细信息
- -XX:+PrintGCDateStamps 打印GC详细时间
- -XX:+PrintGCTimeStamps 打印CG发生的时间戳
- -verbose:gc 在控制台输出GC情况
- -Xloggc: 指定GC log的位置
- -XX:GCLogFileSize 设置滚动日志文件的大小，必须大于8k
- -XX:+UseGCLogFileRotation 打开GC日志滚动记录功能
- -XX:NumberOfGCLogFiles 设置滚动日志文件个数
- -XX:+HeapDumpOnOutOfMemoryError 参数表示当JVM发生OOM时，自动生成DUMP文件
- -XX:HeapDumpPath 参数表示生成DUMP文件的路径，也可以指定文件名称


## Springboot

内容配置在 /etc/init.d/ 目录下，最终通过命令 service 来启动。

下面以project为`micro-service`举例说明。

e.g. 

```
service micro-service restart
```

```
cat /etc/init.d/micro-service

#!/bin/bash
# description: micro-service Start Stop Restart
# processname: micro-service
# chkconfig: 234 20 80

DATETIME=`date +%Y-%m-%d`
NAME=micro-service
JAVA_HOME=/opt/java/jdk8
USER=root
PROFILE=prod

APP_BASE=/opt/springboot/$NAME
PID_FILE=$APP_BASE/$NAME.pid

JAVA_OPTS=" ${JVM_OPTS} 
-Dfile.encoding=UTF-8 
-Xms1024M 
-Xmx1536M 
-XX:+PrintGC 
-XX:+PrintGCDetails 
-XX:+PrintGCTimeStamps 
-XX:+PrintGCDateStamps 
-Xloggc:$APP_BASE/logs/gc.log 
-Dapp.base=${APP_BASE} 
-Dspring.profiles.active=$PROFILE "

DAEMON="$JAVA_HOME/bin/java $JAVA_OPTS -jar $APP_BASE/$NAME.jar "

function startservice (){

    if [ -f $PID_FILE ] ;then
        pid=$(cat $PID_FILE)
        if [ "$(ps --pid $pid | wc -l)" -ne 1 ] ;then
            echo -e "$NAME is already running "
        else
            echo -e "Starting daemon: $NAME "
            rm -rf $PID_FILE
            start-stop-daemon --start --quiet --chuid $USER  --make-pidfile --pidfile $PID_FILE --background --exec /bin/bash -- -c "$DAEMON"
        fi
    else
        echo -e "Starting daemon: $NAME "
        start-stop-daemon --start --quiet --chuid $USER  --make-pidfile --pidfile $PID_FILE --background --exec /bin/bash -- -c "$DAEMON"
    fi

}


function stopservice (){

    echo -e "Stopping daemon: $NAME "
    if [ -f $PID_FILE ] ;then
        pid=$(cat $PID_FILE)
        if [ "$(ps --pid $pid | wc -l)" -ne 1 ] ;then
            # kill -9 $(ps -o pid --ppid $pid | sed 1d)
            kill -9 $pid
            rm -rf $PID_FILE
        else
            rm -rf $PID_FILE
        fi
        echo -e "$NAME stopped"
    else
        echo -e "$NAME is not started or $PID_FILE not exist"
    fi

}

case $1 in
start)

    startservice
;;
stop)

    stopservice

;;
restart)

    stopservice
    sleep 2
    startservice

;;
esac
exit 0
```