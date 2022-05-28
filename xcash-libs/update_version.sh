#!/bin/bash
LIB_DIR=xcash-libs/monero/lib/arm64-v8a
LIB_FILE=libversion.a

VERSION=`strings $LIB_DIR/$LIB_FILE | grep -P [0-9]+\.[0-9]+\.[0-9]+-release | tr -d '\-release'`

sed "s/%VERSION%/$VERSION/g" xcash-libs/version_template.xml > app/src/main/res/values/version.xml
