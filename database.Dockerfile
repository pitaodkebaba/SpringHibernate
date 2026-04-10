# Używamy pływającego tagu (9 lub najnowszego 9.6), który pobiera najnowsze łatki
FROM mysql:9

# Aktualizujemy wewnętrzny system Oracle Linux, aby zaciągnąć najnowsze biblioteki
RUN microdnf update -y && microdnf clean all

COPY ./database_init.sql /docker-entrypoint-initdb.d/database_init.sql

EXPOSE 3306

CMD ["mysqld"]