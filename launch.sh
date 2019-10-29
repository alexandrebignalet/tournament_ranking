#!/bin/sh

./gradlew clean test

docker-compose up -d

./gradlew run