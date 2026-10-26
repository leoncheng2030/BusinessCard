<div align="center">
    <p align="center">
        <img src="wqs-admin-web/public/img/logo.png" height="150" alt="logo"/>
    </p>
    <h1>Wqs 基础开发框架（wqs）</h1>
    <p>基于 Spring Boot 3 + Vue 3 的插件化开发框架，插件化架构、开箱即用</p>
</div>

## 项目简介

wqs 是基于 Wqs 插件化平台的开发框架：后端以插件化方式组织，插件间通过 `wqs-plugin-api` 暴露接口解耦，可按业务需要自由组合；前端基于 Ant Design Vue 提供完整的系统管理界面；内置权限体系、支付与消息通知等平台化能力。

## 核心特性

- **插件化架构**：登录鉴权、系统管理、开发工具、代码生成、C 端、移动端、支付按插件拆分，`wqs-plugin-api` 统一定义跨插件接口
- **按钮级权限体系**：菜单（角色授权）+ 按钮/接口（`@SaCheckPermission` + 前端 `hasPerm`）两级防线，注解值即授权表接口 URL，杜绝绕过前端直调
- **支付能力**：支付宝/微信下单、退款、回调，独立 `PAY_ORDER` 流水
- **开发工具**：数据字典、文件管理（本地/MinIO/阿里 OSS/腾讯 COS）、定时任务、消息发送、操作日志、代码生成
- **开放集成**：钉钉/企业微信/飞书/WeLink、sms4j 短信、邮件（JavaMail/阿里云/腾讯云）、国密 SM2/SM3/SM4、JustAuth 第三方登录

## 技术栈

| 端 | 技术 |
|---|---|
| 后端 | Spring Boot 3.5 / JDK 17 / Sa-Token 1.44 / MyBatis-Plus 3.5 / dynamic-datasource + Druid / Redis + Redisson |
| 数据库 | MySQL 8.0.16+（框架亦适配 PG / Oracle / 达梦 / 金仓 / MSSQL，JDBC 驱动见 pom 注释） |
| 工具能力 | Knife4j 接口文档、EasyExcel/EasyPOI 导入导出、x-file-storage、sms4j 短信、微信支付/支付宝 SDK、国密 SM2/SM3/SM4 |
| 前端 | Vue 3.5 / Vite 6 / Ant Design Vue 4.2 / Pinia / vue-router / ECharts / TailwindCSS / sm-crypto |

## 项目结构

```
wqs
  |-wqs-admin-web       前端（Vue3 + AntDV4）
  |  |-src/api          各模块 API（按插件域拆分）
  |  |-src/views        各模块页面（sys/dev/gen/auth/client/mobile 等）
  |-wqs-common          基础通用模块
  |-wqs-plugin          插件包
  |  |-wqs-plugin-auth   登录鉴权插件
  |  |-wqs-plugin-sys    系统功能插件（用户/机构/角色/菜单/资源/城市）
  |  |-wqs-plugin-dev    开发工具插件（字典/文件/定时任务/消息/品牌/轮播/操作日志）
  |  |-wqs-plugin-gen    代码生成插件
  |  |-wqs-plugin-client C 端功能插件
  |  |-wqs-plugin-mobile 移动端管理插件
  |  |-wqs-plugin-pay    支付插件（支付宝/微信、PAY_ORDER 流水、退款）
  |-wqs-plugin-api      插件 API 包（与上各插件一一对应）
  |-wqs-web-app         主启动模块（vip.wqs.Application，端口 84）
```

## 快速启动

### 环境要求

| 依赖 | 版本 | 用途 |
|---|---|---|
| JDK | 17 | 后端运行环境 |
| Maven | 3.6+ | 后端依赖管理 |
| MySQL | 8.0.16+ | 主数据库 |
| Redis | 任意稳定版 | 缓存 / Sa-Token 会话 / 分布式锁 |
| Node.js | ≥ 18 | 前端构建运行 |

### 1. 初始化数据库

默认库名 `wqs`（见 `wqs-web-app/src/main/resources/application.properties`，支持 `MYSQL_HOST/MYSQL_PORT/MYSQL_DB/MYSQL_USERNAME/MYSQL_PASSWORD` 环境变量覆盖，本地默认 `root/123456`，生产环境必须覆盖）；同时准备 Redis。建库后导入仓库自带初始化脚本 `wqs-web-app/src/main/resources/wqs.sql`（平台表 + 菜单/配置/字典种子）。

### 2. 启动后端

1. 按需修改 [application.properties](wqs-web-app/src/main/resources/application.properties) 中的数据源（MySQL）、Redis 等配置；
2. 启动主类 `wqs-web-app` 模块下的 `vip.wqs.Application`（IDE 直接运行，或 `mvn -pl wqs-web-app -am spring-boot:run`）；
3. 启动成功后访问：
   - 系统接口：`http://localhost:84`
   - 接口文档（Knife4j）：`http://localhost:84/doc.html`（账号 `admin` / `123456`）

### 3. 启动前端

```
cd wqs-admin-web
npm install
npm run dev
```

- 开发地址：`http://localhost:83`（端口在 `.env.development` 配置）
- 接口地址：`VITE_API_BASEURL = http://127.0.0.1:84`（后端默认 84 端口，按需修改）
- 生产构建：`npm run build`（`.env.production` 配置生产环境）

### 4. 内置账号

框架内置超级管理员账号 `superAdmin`，初始密码以数据库初始化数据为准，登录后请在"个人中心"及时修改密码。

## 核心设计约定（摘要）

- 主键：`VARCHAR(20)` 雪花 ID（MyBatis-Plus `ASSIGN_ID` 全局生成），不依赖数据库自增
- 逻辑删除：`delete_flag = NOT_DELETE / DELETED`，业务唯一键一律 `(delete_flag, 业务键)` 复合；流水/日志表不设 `delete_flag`，永久留痕
- 金额：`DECIMAL(12,2)`，业务层 BigDecimal 运算，禁止浮点
- 权限：写接口统一 `@SaCheckPermission`，前缀与前端 `hasPerm` 及授权表接口 URL 三方一致

## 版权说明

- 本项目基于 **Wqs 3.x（Snowy 生态）** 二次开发，遵循 Apache License 2.0 协议开源；
- 请保留各源码头部的版权与作者声明，勿删除或修改。
