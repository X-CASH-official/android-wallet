#!/usr/bin/env bash

set -e

source script/env.sh

TARGET_DIR=$EXTERNAL_LIBS_ROOT/boost

version=1_66_0
dot_version=1.66.0

cd $EXTERNAL_LIBS_BUILD_ROOT/boost_${version}

if [ ! -f "b2" ]; then
  ./bootstrap.sh
fi

args="--build-type=minimal link=static runtime-link=static --with-chrono \
--with-date_time --with-filesystem --with-program_options --with-regex \
--with-serialization --with-system --with-thread \
--includedir=$TARGET_DIR/include \
toolset=clang threading=multi threadapi=pthread target-os=android"

if [ ! -d "$TARGET_DIR/arm" ]; then
  PATH=$NDK_TOOL_DIR/arm/arm-linux-androideabi/bin:$NDK_TOOL_DIR/arm/bin:$PATH \
      ./b2 --build-dir=android-arm --prefix=$TARGET_DIR/arm $args \
      install
  ln -sf ../include $TARGET_DIR/arm
fi

if [ ! -d "$TARGET_DIR/arm64" ]; then
  PATH=$NDK_TOOL_DIR/arm64/aarch64-linux-androideabi/bin:$NDK_TOOL_DIR/arm64/bin:$PATH \
      ./b2 --build-dir=android-arm64 --prefix=$TARGET_DIR/arm64 $args \
      install
  ln -sf ../include $TARGET_DIR/arm64
fi
