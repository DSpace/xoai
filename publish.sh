#!/bin/bash

echo "Script for Mac OS X"

mvn package
VERSION=`xpath pom.xml "/project/version/text()"`



cp pom.xml target/xoai-$VERSION.pom

rm -rf target/*.asc

gpg2 --armor --sign --default-key "Lyncode <general@lyncode.com>" target/xoai-$VERSION.jar
gpg2 --armor --sign --default-key "Lyncode <general@lyncode.com>" target/xoai-$VERSION-javadoc.jar
gpg2 --armor --sign --default-key "Lyncode <general@lyncode.com>" target/xoai-$VERSION-sources.jar
gpg2 --armor --sign --default-key "Lyncode <general@lyncode.com>" target/xoai-$VERSION.pom

jar -cvf target/xoai.jar target/xoai-$VERSION.jar target/xoai-$VERSION-javadoc.jar target/xoai-$VERSION-sources.jar target/xoai-$VERSION.jar.asc target/xoai-$VERSION-javadoc.jar.asc target/xoai-$VERSION-sources.jar.asc target/xoai-$VERSION.pom target/xoai-$VERSION.pom.asc

