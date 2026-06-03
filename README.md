# LinkTracker

LinkTracker – Telegram-бот, который отслеживает изменения на веб-страницах и оперативно информирует пользователя о них.

# Инструкция по запуску

Для запуска требуется установленный docker и docker compose
Необходимо создать файл .env в корне проект и определить в нем 4 переменные:
- TELEGRAM_BOT_TOKEN - токен тг бота
- GITHUB_TOKEN - токен github
- STACKOVERFLOW_KEY - ключ stackoverflow
- STACKOVERFLOW_ACCESS_KEY - ключ доступа stackoverflow

Запуск из корня проекта: docker compose up

Предполагается выбор режима работы в настройках application.yaml:
- app.db-provider - провайдер базы данных : orm (spring jpa hibernate) или sql
