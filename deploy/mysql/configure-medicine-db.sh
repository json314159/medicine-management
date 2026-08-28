#!/usr/bin/env bash
set -euo pipefail

container=1Panel-mysql-EO5A
project_dir=/opt/medicine-management
app_password=$(openssl rand -hex 24)

docker exec -i "$container" sh -c \
  'MYSQL_PWD="$MYSQL_ROOT_PASSWORD" mysql -uroot' <<SQL
CREATE DATABASE IF NOT EXISTS medicine_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'medicine_app'@'%' IDENTIFIED BY '$app_password';
ALTER USER 'medicine_app'@'%' IDENTIFIED BY '$app_password';
GRANT ALL PRIVILEGES ON medicine_management.* TO 'medicine_app'@'%';
FLUSH PRIVILEGES;
SQL

env_tmp=$(mktemp)
grep -v '^MYSQL_PASSWORD=' "$project_dir/.env" > "$env_tmp"
printf 'MYSQL_PASSWORD=%s\n' "$app_password" >> "$env_tmp"
chmod 0600 "$env_tmp"
mv "$env_tmp" "$project_dir/.env"

cd "$project_dir"
docker compose up -d --force-recreate backend frontend
