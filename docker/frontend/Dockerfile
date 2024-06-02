FROM node:20 AS build-stage
WORKDIR /app
COPY ../frontend/package*.json .
RUN npm install
COPY ../frontend .
RUN npm run build

FROM nginx:alpine
COPY ../frontend/nginx.conf /etc/nginx/nginx.conf
COPY --from=build-stage /app/dist/hac_fronted/browser /usr/share/nginx/html

EXPOSE 80