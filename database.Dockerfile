FROM mysql:latest

COPY ./database_init.sql /docker-entrypoint-initdb.d/database_init.sql

EXPOSE 3306

CMD ["mysqld"]