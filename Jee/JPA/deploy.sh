#!/bin/bash
if [ -z "${TOMEE_FOR_DEP}" ]; then
    echo "@ Please set ENV-VAR TOMEE_FOR_DEP "
    echo "export TOMEE_FOR_DEP=/some/tomee/path"
    echo " "
    exit 1
fi

export WAR_FOR_DEP=./Study_webapp/target/study_webapp-0.0.1.war

if [ -z "${WEBAPP}" ]; then
    export WEBAPP="myapp"
fi

echo "@ Stopping tommeeee...."
"${TOMEE_FOR_DEP}/bin/shutdown.sh"
sleep 2

if [ -d "${TOMEE_FOR_DEP}/webapps/${WEBAPP}" ]; then
    echo "@ Deleting ${WEBAPP} dir"
    echo " "
    rm -rf "${TOMEE_FOR_DEP}/webapps/${WEBAPP}"
fi

if [ -f "${TOMEE_FOR_DEP}/webapps/${WEBAPP}.war" ]; then
    echo "@ Deleting ${WEBAPP} war"
    echo " "
    rm "${TOMEE_FOR_DEP}/webapps/${WEBAPP}.war"
fi

echo "@ Coping new war file"
echo " "
cp "${WAR_FOR_DEP}" "${TOMEE_FOR_DEP}/webapps/${WEBAPP}.war"
sleep 1
echo "@ Starting tomee"
echo " "
"${TOMEE_FOR_DEP}/bin/startup.sh"
