#! /bin/bash

type mvn >/dev/null 2>&1
if [ $? = 0 ]; then
    _MVN=mvn
else
    _MVN=./mvnw
fi
${_MVN} -U clean package -DskipTests -e
