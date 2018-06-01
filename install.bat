@echo off

set TOMCAT_SERVER_START_FILE=C:\DEV\Enviroment\apache-tomcat-8.0.32\bin

pushd "%TOMCAT_SERVER_START_FILE%"

rem service.bat remove apacheTomcat-soccer-crawler

service.bat install apacheTomcat-soccer-crawler

popd

pause