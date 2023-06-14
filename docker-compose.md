version: "3.3"

services:
  database:
    container_name: db
    image: mysql:8.0
    restart: always
    volumes:
      - ./mysqldata:/var/lib/mysql
    environment:
      - MYSQL_ROOT_PASSWORD=official1234
      - MYSQL_DATABASE=official
    ports:
      - "3306:3306"

  redis:
    image: redis
    container_name: redis
    restart: always
    ports:
      - "6379:6379"

  app:
    container_name: official-admin-test-app
    image: official-test-server:admin
    expose:
      - 53820
    ports:
      - "53820:8080"
    depends_on:
      - database
      - redis
    links:
      - database
      - redis
