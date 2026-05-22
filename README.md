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

Предполагается выбор режима работы в настройках docker compose:
1) Провайдер базы данных MESSAGE_SENDER : orm (spring jpa hibernate) или sql
2) Способ коммуникации Bot и Scrapper : mq (Kafka) или http (HttpClient)
