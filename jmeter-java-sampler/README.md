在IDE或终端命令行下运行`run-jmeter.sh`将自动下载jmeter，执行打包，然后启动jmeter，可以创建测试计划进行测试。

如果要交给其他人测试，可以执行`mvn clean package`，然后将`target`目录下的jar包交付给测试人员，
测试人员将jar文件放到jmeter的`lib/ext`目录下，然后启动jmeter进行测试。

本示例中已通过`maven-shade-plugin`将测试程序和依赖项打成一个`fat-jar`，方便交付、分发。
