#!/bin/bash

java_file="Model"

if [ ! -z "$1" ]; then
  java_file="$1"
fi

main_class="io.github.sashirestela.openai.demo.${java_file}Demo"

command="mvn exec:java -Dexec.mainClass=${main_class}"

echo $command

eval $command