start java -Xmx512m -Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n -Dhzc.configs=%~dp0\configs -Dlog.levels.overall=ALL -jar HazelcastServer.jar
