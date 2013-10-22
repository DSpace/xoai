#!/bin/bash

echo "Script for Mac OS X"
rm -rf target/*.asc
rm -rf target/*.jar
rm -f target/$ARTIFACT.jar


VERSION=`xpath pom.xml "/project/version/text()"`
ARTIFACT=`xpath pom.xml "/project/artifactId/text()"`
USER="Lyncode <general@lyncode.com>"


cp pom.xml target/$ARTIFACT-$VERSION.pom
gpg2 -ab --default-key "$USER" target/$ARTIFACT-$VERSION.pom

mvn package

for i in `ls target/ | grep .jar`
do
	gpg2 -ab --default-key "$USER" target/$i
done


LIBS=`ls target/ | grep .jar | xargs`
echo $LIBS
jar -cvf target/$ARTIFACT.jar target/$ARTIFACT-$VERSION.pom $LIBS
