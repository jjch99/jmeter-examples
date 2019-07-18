#! /bin/bash

BASEDIR=$(cd "$(dirname "$0")"; pwd)
cd "${BASEDIR}"

WORKDIR=$(cd "${BASEDIR}/.."; pwd)
cd "${WORKDIR}"

JMETER=apache-jmeter-4.0

if [ ! -d "${JMETER}" ]; then
    if [ ! -f "${JMETER}.zip" ]; then
        # download it
        wget https://archive.apache.org/dist/jmeter/binaries/${JMETER}.zip
    fi
    unzip ${JMETER}.zip
fi

JMETER_HOME=$WORKDIR/$JMETER

# check it again
if [ ! -d "${JMETER_HOME}/lib/" ]; then
    echo
    echo "Invalid JMETER_HOME: ${JMETER_HOME}"
    exit 1;
fi

cd "${BASEDIR}"
mvn -U clean package -DskipTests -e

if [ $? = 0 ]; then
    echo "maven build success."
    # copy to jmeter lib/ext/
    #cp -f target/*.jar ${JMETER_HOME}/lib/ext/
    #cp -f target/lib/*.jar ${JMETER_HOME}/lib/ext/
else
    echo "maven build failed."
    exit 1;
fi

cd "${WORKDIR}"
# add build & dependencies to jmeter search_paths,
# it can be automatically finded by jmeter
export JMETER_OPTS="-Dsearch_paths=${BASEDIR}/target"
# Uncomment next line if you want to display with Chinese UI, default is English.
#export JMETER_LANGUAGE="-Duser.language=zh -Duser.region=CN"

# start jmeter, default GUI mode
sh ${JMETER_HOME}/bin/jmeter
