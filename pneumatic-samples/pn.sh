#!/bin/sh

set -x

task=$1
shift

./gradlew $task -Pargs="$*"
