#!/usr/bin/env bash

set -e

source script/env.sh

cd $EXTERNAL_LIBS_BUILD_ROOT

url="https://github.com/snakewaypasser/X-CASH_DPOPS.git"

if [ ! -d "X-CASH_DPOPS" ]; then
  git clone ${url}
  cd X-CASH_DPOPS
#  git checkout tag
  git submodule update --recursive --init
else
  cd X-CASH_DPOPS
#  git checkout tag
  git pull
  git submodule update --recursive --init
fi
