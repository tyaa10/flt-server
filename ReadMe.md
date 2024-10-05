# Прототип серверной составляющей приложения FLT

Прототип серверной составляющей приложения FLT (Foreign Language Training) на основе SpringBoot

## Окружение

- OS: MS Windows >= 10 / Linux >= 4
- JDK: Open JDK / Oracle JDK >= 21
- RDBMS: Postgres >= 16

## Настройка

- скопировать файл **sample.env**, переименовать в **local.env**, заменить значения-образцы реальными данными для подключения к БД

## Запуск

- на ОС-хосте:

**./mvnw spring-boot:run**

- запуск на ОС-хосте:

## Тестирование

- модульное полное:

**./mvnw clean verify**

- модульное частичное, например, с указанием класса теста _AuthServiceTest_:

**./mvnw clean verify** **-Dtest="**_AuthServiceTest_**"** (bash)

**./mvnw clean verify** **-Dtest=**_AuthServiceTest_ (cmd)

- интеграционное с запуском всех групп и кейсов в классе теста _AuthControllerEndpointsTest_:

**./mvnw clean verify** **-Dtest="**_AuthControllerEndpointsTest_**"** (bash)

**./mvnw clean verify** **-Dtest=**_AuthControllerEndpointsTest_ (bash)

- интеграционное с запуском всех кейсов в группе _ChangeUserRoleTestCases_ в классе теста _AuthControllerEndpointsTest_:

**./mvnw clean verify** **-Dtest="**_AuthControllerEndpointsTest_**\\$**_ChangeUserRoleTestCases_**"** (bash)

**./mvnw clean verify** **-Dtest=**_AuthControllerEndpointsTest_**\$**_ChangeUserRoleTestCases_ (cmd)

- интеграционное с запуском кейса _givenAdminUserAuthenticated_whenChangeUserRole_thenOk_ в группе _ChangeUserRoleTestCases_ в классе теста _AuthControllerEndpointsTest_:

**./mvnw clean verify** **-Dtest="**_AuthControllerEndpointsTest_**\\$**_ChangeUserRoleTestCases_**#**_givenAdminUserAuthenticated_whenChangeUserRole_thenOk_**"** (bash)

**./mvnw clean verify** **-Dtest=**_AuthControllerEndpointsTest_**\$**_ChangeUserRoleTestCases_**#**_givenAdminUserAuthenticated_whenChangeUserRole_thenOk_ (cmd)

## Отчёты

- по модульным тестам:

1. выполнить

**./mvnw surefire-report:report**

2. открыть для просмотра в браузере

_\<корневой_каталог_проекта\>_**/target/reports/surefire.html**

- по всему проекту в целом:

1. выполнить

**./mvnw site**

2. открыть для просмотра в браузере

_\<корневой_каталог_проекта\>_**/target/site/index.html**