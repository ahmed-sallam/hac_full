#FROM node:20-bookworm-slim AS build
FROM node:20.11.1-alpine3.19 AS build
WORKDIR /app
COPY . .
FROM nginx:alpine
#FROM nginx:1.21
COPY --from=build /app/dist/hac_fronted/browser /usr/share/nginx/html
EXPOSE 80