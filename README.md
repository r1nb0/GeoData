# GEODATA JAVA APPLICATION
## Описание
С помощью данного приложения можно получить информацию о долготе и широте необходимого вам города, а также рассчитать расстояние между двумя городами. В основе реализации лежит архитектурный паттерн MVC (Model-View-Controller), что обеспечивает четкое разделение бизнес-логики, представления и управления веб-запросами.
## Задание 1
1. Создайте и запустите локально простейший веб/REST сервис, используя любой открытый пример с использованием Java stack: Spring (Spring Boot)/Maven/Gradle/Jersey/Spring MVC.
2. Добавьте GET ендпоинт, принимающий входные параметры в качестве queryParams в URL согласно варианту, и возвращающий любой hard-coded результат в виде JSON согласно варианту.
## Архитектура приложения
Для выполнения задания необходимы классы:

1. **GeoData**: Этот класс представляет собой сущность геоинформации о городе. Он содержит поля, такие как `id`, `nameCity`, `nameCountry`, `latitude`, `longitude`. Класс также содержит геттеры и сеттеры для доступа к данным полям.
   
2. **GeoDataRepository**: Интерфейс, который наследуется от `JpaRepository<GeoData, Integer>`. Он предоставляет методы для выполнения операций **CRUD** (Create, Read, Update, Delete) с сущностью `GeoData`. В данном приложении реализован метод `findByNameCity(String name)` который позволяет находить информацию по названию города. Данный метод не предоставляется Spring Data. Он реализован с помощью аннотации `@Query`, которая отправляет необходимый SQL запрос. Ниже представлено определение данного метода:
 ```java
    @Query("select obj from GeoData obj where obj.nameCity = :name")
    Optional<GeoData> findByNameCity(@Param("name") String name);
 ```
3. **GeoDataService**: Интерфейс, который определяет методы для работы с данными. Обычно содержит методы для выполнения бизнес-логики, связанной, в данном случае, с геоданными.

4. **GeoDataServiceImpl**: Реализация интерфейса `GeoDataService`. В этом классе содержится реализация методов для работы с данными, например получение списка геоданных c помощью `PostgreSQL`.

5. **GeoDataController**: Этот класс является  контроллером Spring MVC, который обрабатывает HTTP-запросы, связанные с геоданными.
   
6. **GeoDataApplication**: Этот класс является точкой  входа в ваше приложение Spring Boot. Он содержит метод `main`, который запускает приложение.

## Запуск
1. **Подключение к базе данных**: Для функциональности и работы проекта необходимо подключиться к базе данных PostgreSQL 16. Разверните Docker контейнер с PostgreSQL и укажите параметры подключения в файле application.properties, а именно: `url`, `username`, `password`.
   
2. **Запуск приложения**: После настройки и подключения к базе данных, запустите приложение, используя средство сборки Gradle для сборки и запуска проекта.

3. **Использование веб-интерфейса**: После успешного запуска приложения откройте веб-браузер и перейдите по адресу http://localhost:8080/api/v1/geo/distance/firstCity+secondCity, при этом, введя вместо `firstCity` и `secondCity` названия необходимых вам городов.
