@echo off
IF "-z" "%TOMEE_FOR_DEP%" (
  echo "@ Please set ENV-VAR TOMEE_FOR_DEP "
  echo "export TOMEE_FOR_DEP=\some\tomee\path"
  echo " "
  exit "1"
)

IF "-z" "%WAR_FOR_DEP%" (
  echo "@ Please set ENV-VAR WAR_FOR_DEP "
  echo "export WAR_FOR_DEP=\some\war\path"
  echo " "
  exit "1"
)

IF "-z" "%WEBAPP%" (
  set "WEBAPP=myapp"
)

echo "@ Stopping tommeeee...."
"${TOMEE_FOR_DEP}\\bin\\shutdown.sh"
sleep "2"

IF "-d" "%TOMEE_FOR_DEP%\\webapps\\%WEBAPP%" (
  echo "@ Deleting %WEBAPP% dir"
  echo " "
  DEL \\S "%TOMEE_FOR_DEP%\\webapps\\%WEBAPP%"
)

IF "-f" "%TOMEE_FOR_DEP%\\webapps\\%WEBAPP%.war" (
  echo "@ Deleting %WEBAPP% war"
  echo " "
  DEL  "%TOMEE_FOR_DEP%\\webapps\\%WEBAPP%.war"
)

echo "@ Coping new war file"
echo " "
COPY  "%WAR_FOR_DEP%" "%TOMEE_FOR_DEP%\\webapps\\%WEBAPP%.war"
sleep "1"
echo "@ Starting tomee"
echo " "
"%TOMEE_FOR_DEP%\\bin\\startup.bat"
