@echo off
echo === DELETE CLASS LAMA ===
rmdir /S /Q "..\WEB-INF\classes\servlets"
rmdir /S /Q "..\WEB-INF\classes\utils"
mkdir "..\WEB-INF\classes\servlets"
mkdir "..\WEB-INF\classes\utils"

echo === COMPILE ULANG ===
javac -cp "C:\apache-tomcat-10.1.48\lib\jakarta.servlet-api-6.0.0.jar;C:\apache-tomcat-10.1.48\webapps\tess\WEB-INF\lib\mysql-connector-j-9.0.0.jar;C:\apache-tomcat-10.1.48\webapps\tess\WEB-INF\lib\json-20240303.jar;." -d ..\WEB-INF\classes servlets\*.java utils\*.java

echo === LIST OUTPUT ===
dir ..\WEB-INF\classes\servlets
dir ..\WEB-INF\classes\utils

echo DONE
pause
