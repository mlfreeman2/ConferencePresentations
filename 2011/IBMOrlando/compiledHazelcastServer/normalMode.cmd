@echo off
start javaw -Xmx512m -Dlog.levels.overall=WARNING -Dhzc.configs=%~dp0\configs -jar %~dp0HazelcastInstanceContainer.jar
