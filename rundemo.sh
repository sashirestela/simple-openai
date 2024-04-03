#!/bin/bash

service="Model"

log_level_param="-Dorg.slf4j.simpleLogger.defaultLogLevel"
log_level_value="off"

log_file_param="-Dorg.slf4j.simpleLogger.logFile"
log_file_value="demo.log"

log_options=""

if [ ! -z "$1" ]; then
  service=$(echo "$1" | awk '{ print toupper(substr($0, 1, 1)) substr($0, 2) }')
fi

if [ ! -z "$2" ]; then
  log_level_value="$2"
fi

if [ "$log_level_value" != "off" ]; then
  log_options="${log_level_param}=${log_level_value}"
  #log_options="${log_level_param}=${log_level_value} ${log_file_param}=${log_file_value}"
fi

main_class="io.github.sashirestela.openai.demo.${service}ServiceDemo"

command="mvn exec:java -Dexec.mainClass=${main_class} ${log_options}"
echo $command

eval $command
