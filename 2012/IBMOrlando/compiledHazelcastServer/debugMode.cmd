start javaw -Xmx512m -Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n -Dlog.levels.overall=ALL -jar %~dp0HazelcastInstanceContainer.jar
