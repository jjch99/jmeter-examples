#! /bin/bash

# change it to your jmeter path
JMETER_HOME=$HOME/apache-jmeter-4.0.1

if [ ! -d "${JMETER_HOME}/lib/ext/" ]; then
    echo
    echo "Invalid JMETER_HOME: ${JMETER_HOME}"
    exit 1;
fi

mvn -U clean package -DskipTests -e

if [ $? = 0 ]; then
    cp -f target/*.jar ${JMETER_HOME}/lib/ext/
    cp -f target/lib/*.jar ${JMETER_HOME}/lib/ext/
fi

sh ${JMETER_HOME}/bin/jmeter
