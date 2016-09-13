#!/usr/bin/env bash

rm bundle.jar
rm -rf target/bundle
mkdir -p target/bundle
find . -path "*/target/detangler-*" -print0 | xargs -I % -0 cp % target/bundle/
jar -cfv bundle.jar -C target/bundle target/bundle/*
