# 药品管理系统

基于 **Spring Boot 3 + Java 21** 与 **Vue 3 + TypeScript + Vite** 的前后端分离药品管理项目。

## 功能

- 药品档案：新增、查询、编辑、删除药品
- 库存管理：入库、出库、盘点调整，自动记录库存流水
- 风险提示：低库存与 30 天内到期药品预警
- 概览看板：药品数量、库存数量、库存价值和预警数量

## 本地启动

### 后端

```bash
cd backend
mvn spring-boot:run
```

后端地址为 `http://localhost:8080`，接口文档位于 `http://localhost:8080/swagger-ui/index.html`。

### 前端

```bash
cd frontend
npm install
npm run dev
```

访问 Vite 提示的地址（默认 `http://localhost:5173`）。开发环境下 `/api` 会自动代理至后端。

## MySQL 部署配置

生产环境通过环境变量启用 MySQL：

```bash
SPRING_PROFILES_ACTIVE=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_NAME=medicine_management
DB_USERNAME=medicine_app
DB_PASSWORD=change-me
```

首次部署前创建数据库：

```sql
CREATE DATABASE medicine_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## Docker 部署

```bash
cp .env.example .env
# 修改 .env 中的数据库密码
docker compose up -d --build
```

默认通过服务器的 `8088` 端口访问。Docker 部署使用持久化 H2 数据卷，适合轻量服务器；生产环境可按上方配置切换至 MySQL。
