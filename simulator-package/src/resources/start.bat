@ECHO OFF

SET SIM_CLASS_PATH=config\;lib\*;bin\*;%GROOVY_HOME%\lib\*;

SET REMOTE_DEBUG=
REM SET REMOTE_DEBUG=-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=y

@java -classpath "%SIM_CLASS_PATH%" ^
      %REMOTE_DEBUG% ^
      com.codebullets.external.party.simulator.Program