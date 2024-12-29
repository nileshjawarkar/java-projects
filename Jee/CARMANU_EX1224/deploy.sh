mvn clean install
echo ""
echo "## Maven build - DONE"
echo ""

CATALINA_HOME="/home/nilesh/Downloads/apache-tomee-plume-9.1.3"
APP_NAME="prj_app-0.0.1"
APP_LOC="./prj_war/target"

# Stop tomee if runnung
${CATALINA_HOME}/bin/shutdown.sh
echo ""
echo "## WebServer shutdown - DONE"
echo ""

# Reset catlina logs
echo "" > ${CATALINA_HOME}/logs/catalina.out

# Remove old version of app, if present
rm -rf ${CATALINA_HOME}/webapps/${APP_NAME}
rm -rf ${CATALINA_HOME}/webapps/${APP_NAME}.war
sleep 1

# Copy new version
cp -f ${APP_LOC}/${APP_NAME}.war ${CATALINA_HOME}/webapps
sleep 1
echo ""
echo "## Copy WAR to webServer - DONE"
echo ""

# Start tomee
${CATALINA_HOME}/bin/startup.sh
echo ""
echo "## WebServer start - DONE"
echo ""
