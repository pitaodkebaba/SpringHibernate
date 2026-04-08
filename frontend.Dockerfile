# ETAP 1: Budowanie (Build)
# Używamy konkretnej, zamrożonej wersji Node na Alpine Linux
FROM node:current-alpine3.22 AS build

# Ustawiamy katalog roboczy
WORKDIR /app

# Kopiujemy tylko pliki pakietów, by optymalizować cache dockera
COPY package*.json ./

# Czysta instalacja zależności (odrzuca pakiety nieopisane w package-lock.json)
RUN npm ci

# Kopiujemy resztę kodu i budujemy
COPY . .
RUN npm run build

# ETAP 2: Serwowanie (Production)
# Używamy oficjalnego obrazu Nginx w wersji UNPRIVILEGED (działa bez roota!)
FROM nginxinc/nginx-unprivileged:alpine-perl

# Kopiujemy skompilowane pliki z etapu 1
# W Nginx unprivileged domyślny katalog to /usr/share/nginx/html
COPY --from=build /app/dist /usr/share/nginx/html

# Wystawiamy port 8080 (obraz unprivileged nie może używać portów poniżej 1024, np. 80)
EXPOSE 8080

# Uruchamiamy Nginx
CMD ["nginx", "-g", "daemon off;"]