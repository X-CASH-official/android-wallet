#!/usr/bin/env bash

set -e

source script/env.sh

cd $EXTERNAL_LIBS_BUILD_ROOT

url="https://github.com/snakewaypasser/X-CASH.git"

if [ ! -d "X-CASH" ]; then
  git clone ${url}
  cd X-CASH
#  git checkout tag
  git submodule update --recursive --init
else
  cd X-CASH
#  git checkout tag
  git pull
  git submodule update --recursive --init
fi
