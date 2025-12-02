@echo off
echo Compiling Java files...

javac -cp "C:\apache-tomcat-10.1.48\lib\jakarta.servlet-api-6.0.0.jar;C:\apache-tomcat-10.1.48\webapps\tess\WEB-INF\lib\mysql-connector-j-9.0.0.jar;C:\apache-tomcat-10.1.48\webapps\tess\WEB-INF\lib\json-20240303.jar;." -d ..\WEB-INF\classes servlets\*.java utils\*.java

echo Done.
pause
