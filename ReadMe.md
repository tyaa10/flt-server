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

- полное:

**./mvnw clean verify -D"surefire.includeJUnit5Engines=junit-jupiter,cucumber"**

- с запуском только класса теста _AuthServiceTest_:

**./mvnw clean verify** **-Dtest="**_AuthServiceTest_**"** **-D"surefire.includeJUnit5Engines=junit-jupiter"** (bash)

**./mvnw clean verify** **-Dtest=**_AuthServiceTest_ **-D"surefire.includeJUnit5Engines=junit-jupiter"** (cmd)

- с запуском только классов тестов с суффиксом _UnitTest_ (модульные тесты):

**./mvnw clean verify** **-Dtest="**_\*UnitTest_**"** **-D"surefire.includeJUnit5Engines=junit-jupiter"** (bash)

**./mvnw clean verify** **-Dtest=**_\*UnitTest_ **-D"surefire.includeJUnit5Engines=junit-jupiter"** (cmd)

- с запуском всех групп и кейсов в классе теста _AuthControllerEndpointsTest_:

**./mvnw clean verify** **-Dtest="**_AuthControllerEndpointsTest_**"** **-D"surefire.includeJUnit5Engines=junit-jupiter"** (bash)

**./mvnw clean verify** **-Dtest=**_AuthControllerEndpointsTest_ **-D"surefire.includeJUnit5Engines=junit-jupiter"** (bash)

- интеграционное с запуском всех кейсов в группе _ChangeUserRoleTestCases_ в классе теста _AuthControllerEndpointsTest_:

**./mvnw clean verify** **-Dtest="**_AuthControllerEndpointsTest_**\\$**_ChangeUserRoleTestCases_**"** **-D"surefire.includeJUnit5Engines=junit-jupiter"** (bash)

**./mvnw clean verify** **-Dtest=**_AuthControllerEndpointsTest_**\$**_ChangeUserRoleTestCases_ **-D"surefire.includeJUnit5Engines=junit-jupiter"** (cmd)

- интеграционное с запуском кейса _givenAdminUserAuthenticated_whenChangeUserRole_thenOk_ в группе _ChangeUserRoleTestCases_ в классе теста _AuthControllerEndpointsTest_:

**./mvnw clean verify** **-Dtest="**_AuthControllerEndpointsTest_**\\$**_ChangeUserRoleTestCases_**#**_givenAdminUserAuthenticated_whenChangeUserRole_thenOk_**"** **-D"surefire.includeJUnit5Engines=junit-jupiter"** (bash)

**./mvnw clean verify** **-Dtest=**_AuthControllerEndpointsTest_**\$**_ChangeUserRoleTestCases_**#**_givenAdminUserAuthenticated_whenChangeUserRole_thenOk_ **-D"surefire.includeJUnit5Engines=junit-jupiter"** (cmd)

- системное WEB-панели администрирования (требует предварительно запущенной вручную системы):

**./mvnw clean verify** **-Dtest**=_CucumberTest_ **-D"surefire.includeJUnit5Engines=cucumber"**

## Отчёты

- по тестам:

1. выполнить

**./mvnw surefire-report:report-only -D"surefire.includeJUnit5Engines=junit-jupiter,cucumber"**

2. открыть для просмотра в браузере

_\<корневой_каталог_проекта\>_**/target/reports/surefire.html**

- по всему проекту в целом:

1. выполнить

**./mvnw site**

2. открыть для просмотра в браузере

_\<корневой_каталог_проекта\>_**/target/site/index.html -D"surefire.includeJUnit5Engines=junit-jupiter,cucumber"**