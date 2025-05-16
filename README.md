# Extincts-Web
Extincts-Web - это Веб-приложение, являющее собой виртуальную выставку

## Стэк
- Язык программирования: Java 21
- Система сборки: Maven
- Система контроля версий: Git
- ORM: JPA, Hibernate
- Фреймворки: Spring Boot 3.4.3
- Тестирование: Spring Test, Junit, Mockito
- СУБД: PostgreSQL, H2(Используется для тестирования)
- Миграции: Liquibase
- Веб-слой: Spring MVC, REST API, WebSocket
- Безопасность: Spring Security, JWT
- Хранилище Медиа-файлов: MinIO
- Кэширование: Redis
- Утилиты: Lombok, Apache Common Lang3, imgScalr
- Документация API: SpringDoc OpenAPI 2.8.6
- Сборка и запуск: Spring Boot Maven Plugin, Docker
- Логирование: Logback

## Запуск
Запуск контейнеров происходит командой
```
docker-compose up -d
```
Запуск приложение с локальной машины происходит командами
```
docker-compose up -d postgres-db
docker-compose up -d minio-s3
docker-compose up -d redis
mvn spring-boot:run
```

