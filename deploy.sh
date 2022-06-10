# 重新编译
mvn clean install -DskipTests
# 杀进程
ps -ef | grep 'bad-pencil.*jar' | grep -v grep | awk '{print $2}' | xargs kill -9
# 清空日志
echo "" > nohup.out
# 新起进程
nohup java -jar target/bad-pencil-0.0.1-SNAPSHOT.jar &
# 查看日志
tail -f nohup.out