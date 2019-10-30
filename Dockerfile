### STAGE 1: Build ###
FROM node:12.7-alpine AS build

COPY ./webapp ./tournament_ranking_app

WORKDIR /tournament_ranking_app

RUN npm i && npm run build

### STAGE 2: Run ###
FROM nginx:1.17.1-alpine

COPY --from=build /tournament_ranking_app/dist/webapp /usr/share/nginx/html