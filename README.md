# Лабораторная работа 3 — Серверные классы (Сервлеты)

**Вариант 1** — Сервлет, возвращающий PowerPoint с автоматическим открытием на клиенте.

## Структура

- `server/` — серверная часть (деплоится в Tomcat)
- `client/` — Java AWT клиент

## Требования

- JDK 17+ (проверено на JDK 24)
- Apache Tomcat 10.1+ (Jakarta Servlet API)

## Запуск сервера

1. Скачать Tomcat 10.1: https://tomcat.apache.org/download-10.cgi
2. Скопировать папку `server/` в `<tomcat>/webapps/lab_servlet/`
3. Скомпилировать сервлеты:

```
cd <tomcat>/webapps/lab_servlet/WEB-INF/classes
javac -cp "../lib/*;../../../../lib/servlet-api.jar" MyServlet.java PptServlet.java InitDB.java
```

4. Запустить Tomcat: `<tomcat>/bin/startup.bat`
5. Открыть http://localhost:8080/lab_servlet/

## Запуск клиента

Открыть `client/` как проект в VS Code (с расширением Java) и запустить `DictClient.java`.

Или из командной строки:
```
cd client
javac -d out src/client/DictClient.java
java -cp out client.DictClient
```
