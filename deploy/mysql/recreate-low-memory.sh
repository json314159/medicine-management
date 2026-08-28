#!/usr/bin/env bash
set -euo pipefail

compose_source=${1:?compose source is required}
config_source=${2:?config source is required}
mysql_dir=/opt/1panel/apps/mysql/mysql
container=1Panel-mysql-EO5A
backup_dir=/opt/mysql-backups
timestamp=$(date +%Y%m%d-%H%M%S)
database_backup="$backup_dir/project_dev-$timestamp.sql.gz"

mkdir -p "$backup_dir"

docker exec "$container" sh -c \
  'MYSQL_PWD="$MYSQL_ROOT_PASSWORD" mysqldump -uroot --single-transaction --routines --events --triggers --hex-blob --no-tablespaces project_dev' \
  | gzip -1 > "$database_backup"
gzip -t "$database_backup"
test -s "$database_backup"

cp "$mysql_dir/docker-compose.yml" "$backup_dir/docker-compose-$timestamp.yml"
cp "$mysql_dir/conf/my.cnf" "$backup_dir/my-$timestamp.cnf"

cd "$mysql_dir"
docker compose down
install -m 0644 "$compose_source" "$mysql_dir/docker-compose.yml"
install -m 0644 "$config_source" "$mysql_dir/conf/my.cnf"
docker compose up -d

for attempt in {1..45}; do
  if docker exec "$container" sh -c \
    'MYSQL_PWD="$MYSQL_ROOT_PASSWORD" mysqladmin -uroot ping --silent' >/dev/null 2>&1; then
    break
  fi
  if [[ $attempt -eq 45 ]]; then
    docker logs --tail=100 "$container"
    exit 1
  fi
  sleep 2
done

docker exec "$container" sh -c \
  'MYSQL_PWD="$MYSQL_ROOT_PASSWORD" mysql -uroot -N -e "SHOW DATABASES LIKE '\''project_dev'\''"' \
  | grep -Fx project_dev >/dev/null

printf 'backup=%s\n' "$database_backup"
docker inspect "$container" --format 'memory={{.HostConfig.Memory}} swap={{.HostConfig.MemorySwap}} status={{.State.Status}} health={{if .State.Health}}{{.State.Health.Status}}{{else}}none{{end}}'
