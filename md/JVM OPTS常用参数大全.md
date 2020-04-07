
# JVM OPTS常用参数大全

## 堆内存设置
- -Xmx 设置堆启动时初识时大小
- -Xms 设置堆最大大小
- -XX:SurvivorRatio=8 设置Eden与Survior大小占比，默认为 8:1:1，此值意思为 8:1:1

## GC设置
- -XX:+PrintTenuringDistribution 输出显示在survivor空间里面有效的对象的岁数情况
- -XX:+PrintGCDetails 打印GC详细信息
- -XX:+PrintGCDateStamps 打印GC详细时间
- -XX:+PrintGCTimeStamps 打印CG发生的时间戳
- -verbose:gc 在控制台输出GC情况
- -Xloggc: 指定GC log的位置
- -XX:GCLogFileSize 设置滚动日志文件的大小，必须大于8k
- -XX:+UseGCLogFileRotation 打开GC日志滚动记录功能
- -XX:NumberOfGCLogFiles 设置滚动日志文件个数

## OOM设置
- -XX:+HeapDumpOnOutOfMemoryError 参数表示当JVM发生OOM时，自动生成DUMP文件
- -XX:HeapDumpPath 参数表示生成DUMP文件的路径，也可以指定文件名称
