#!/usr/bin/env bash

set -e

source script/env.sh

cd $EXTERNAL_LIBS_BUILD_ROOT

url="https://github.com/X-CASH-official/xcash-core.git"

if [ ! -d "xcash-core" ]; then
  git clone --branch dpops-test ${url}
  cd xcash-core
  git submodule update --recursive --init
else
  cd xcash-core
  git checkout dpops-test
  git pull
  git submodule update --recursive --init
fi
