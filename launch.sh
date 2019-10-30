#!/bin/sh

./gradlew clean test

docker-compose build webapp

docker-compose up -d

./gradlew run