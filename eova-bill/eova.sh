#!/bin/bash
# ----------------------------------------------------------------------
# name:         单实例管理脚本
# version:      1.2
# author:       Jieven
# url:        	www.eova.cn
# ----------------------------------------------------------------------

# 自定义配置

# ----------------------------------------------------------------------
# 实例端口号
APP_PORT=9001
# 项目名
APP_MOD=eova-pro
# 实例名
APP_NAME=${APP_MOD}-${APP_PORT}
# JVM参数 -Djava.awt.headless=true
APP_OPTS="-Xms512m -Xmx1024m -Dundertow.port=${APP_PORT} -Dapp.name=${APP_NAME}"
# 项目启动类
MAIN_CLASS=com.pro.AppConfig
# 项目打包文件名
RELEASE_NAME=${APP_MOD}-release.zip

#自定义Java环境变量(EOVA基于JAVA8,如默认为JAVA8,可注释下2行)
export JAVA_HOME=/usr/java/jdk1.8.0_162
export PATH=$JAVA_HOME/bin:$PATH
# ----------------------------------------------------------------------


COMMAND="$1"
C2="$2"


# 生成 class path 值
APP_BASE_PATH=$(cd `dirname $0`; pwd)
CP=${APP_BASE_PATH}/config:${APP_BASE_PATH}/lib/*

# 后台运行
function nolog(){
    # 运行为后台进程，并且将信息输出到 console.log(多实例输出到同一个文件,方便集中查看)
    nohup java -Xverify:none ${APP_OPTS} -cp ${CP} ${MAIN_CLASS} >> console.log 2>&1 &
}

# 运行并查看日志
function start(){
	nolog
	tail -f console.log
}

function stop(){
    # kill 命令不使用 -9 参数时，会回调 onStop() 方法，确定不需要此回调建议使用 -9 参数
    # 根据实例名停止服务
    kill $(pgrep -f ${APP_NAME}) 2>/dev/null
}

function status(){
    PID=`pgrep -f ${APP_NAME}`
    if [[ "$PID" == "" ]]; then
		echo "-- Undertow [${APP_NAME}] stopped !!!"
		exit 0
	fi
    #memery(KB)
    M1=`cat /proc/$PID/status | grep -e VmRSS | awk 'NR==1 {print $2}'`
    #memery%
    M2=`top -b -n1 | grep $PID | head -1 | awk '{print $10}'`
    #cpu%
    CPU=`top -b -n1 | grep $PID | head -1 | awk '{print $9}'`
    PORT=`cat ${APP_BASE_PATH}/eova.sh | grep -o -E 'APP_PORT=[0-9]*'| awk -F '=' '{print$2}'`
    #get tcp
    TCP=`netstat -nat | grep $PORT | grep -E "ESTABLISHED" |wc -l`
    TCP2=`netstat -nat | grep $PORT | grep -E "TIME_WAIT" |wc -l`
    #get thread
    THREAD=`ps -Lf $PID | wc -l`
	echo -n "-- Undertow [${APP_NAME}] running >> pid=$PID memery=$[M1/1024]M/$M2% cpu=$CPU% port=$PORT tcp=$TCP/$TCP2 thread=$THREAD"
    echo
}
if [[ "$COMMAND" == "start" ]]; then
	start
elif [[ "$COMMAND" == "nolog" ]]; then
    nolog
elif [[ "$COMMAND" == "stop" ]]; then
    stop
elif [[ "$COMMAND" == "release" ]]; then
	echo "------------------------------"
	echo "-- Release Start..."
	echo "------------------------------"
	
	if [[ "$C2" == "all" ]]; then
		echo "-- Release All (Please Confirm)"
	fi
	
	stop
    sleep 3
	echo "-- stop succeed"
	
	if [[ "$C2" == "all" ]]; then
		rm -rf ./lib/
		echo "-- clean lib succeed"
	fi
	
	rm -rf ./config/ ./webapp/
	echo "-- clean config,webapp succeed"
	
	unzip -o -q ${RELEASE_NAME}
	echo "-- unzip $RELEASE_NAME succeed"
	if [ ! -d "history" ]; then
        mkdir history
	fi
	mv ${RELEASE_NAME} ./history/last-release-$(date "+%Y%m%d%H%M%S").zip
	echo "-- Backup the last version succeed"
	
	echo "------------------------------"
	echo "-- Project restart ing..."
	echo "------------------------------"
	
    start
elif [[ "$COMMAND" == "restart" ]]; then
    stop
    sleep 1
    start
elif [[ "$COMMAND" == "status" ]]; then
    status
else
	echo "Usage: $0 start | stop | restart | release"
	exit 0
fi
