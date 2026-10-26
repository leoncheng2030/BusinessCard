# AGENTS.md —— wqs 基础框架项目章程

> 所有 AI 助手在本仓库工作的第一入口。配套：`.qoder/framework-config.json`（框架/业务统计口径）、`.qoder/skills/`（按需知识与模板）。
> 最后更新：2026-10-26

## 1. 项目定位

wqs 是基于 Wqs（Snowy 3.x 生态）插件化平台的**基础开发框架**：平台侧提供鉴权/系统管理/开发工具/代码生成/支付/开放集成等能力，业务侧可按需新建插件扩展。

本项目已剥离全部 IoT 业务模块，仅保留基础框架（wqs-common + 平台插件 + 前端）。

## 2. 技术栈与端口

| 项 | 内容 |
|---|---|
| 后端 | JDK 17 / Spring Boot 3.5 / MyBatis-Plus 3.5 / Sa-Token 1.44 / dynamic-datasource + Druid |
| 数据 | MySQL 8（库名 `wqs`，可由 `MYSQL_DB` 环境变量覆盖）、Redis + Redisson |
| 前端 | Vue 3.5 / Vite 6 / Ant Design Vue 4.2 / Pinia / **JavaScript（非 TS）** |
| 端口 | 后端 **84**（`vip.wqs.Application`，wqs-web-app）、前端 **83**（`wqs-admin-web/.env.development`）、Knife4j `http://localhost:84/doc.html`（admin/123456） |

## 3. 模块拓扑与职责边界

| 模块 | 职责 | 改动策略 |
|---|---|---|
| `wqs-common` | 统一返回/异常/实体基类/缓存/国密/工具类 | 谨慎，属底座 |
| `wqs-plugin/`（sys、auth、dev、gen、client、mobile、pay） | 平台插件：系统管理、鉴权、开发工具、代码生成、C 端、移动端、支付 | 不改，除非用户明确要求扩展平台 |
| `wqs-plugin-api/*` | 跨插件接口层（`*Api` + 插件内 `provider` 实现） | 跨插件调用必须走这里 |
| `wqs-web-app` | 唯一启动模块、全局配置、路由白名单 | 配置类改动谨慎 |
| `wqs-admin-web` | 前端工程 | 业务页面按需建 `src/views/{域}` |

依赖规则：插件之间不互相依赖；跨插件通信必须走 `wqs-plugin-api` 接口 + 对方 `provider/` 实现。

## 4. 后端代码规范（均以现有代码为准）

**包结构**：`vip.wqs.{插件}.modular.{域}.{controller|entity|mapper|mapper.mapping|param|service|service.impl|enums|listener|provider|job}`

- 注入：`@Resource`（jakarta.annotation），不用 `@Autowired`、不用构造器注入
- Entity：`@Getter @Setter @TableName("xxx")`，主键 `String id` + `@TableId`，`extends CommonEntity`，字段带 `@Schema(description=...)`，敏感字段 `@JsonIgnore`
- Service：`interface XxxService extends IService<Xxx>`
- ServiceImpl：`extends ServiceImpl<XxxMapper, Xxx> implements XxxService`，`@Slf4j @Service`
- Param：`@Getter @Setter` + `@Schema`；分页 Param 固定含 `current/size/sortField/sortOrder/searchKey` + 业务筛选条件
- Controller：`@Tag(name = "…")` + `@RestController` + `@Validated`，**无类级 `@RequestMapping`**
  - URL 动词式写在方法上：`/{域}/{kebab动作}/{action}`，如 `/sys/user/page`、`/sys/user/add`
  - 写接口：`@CommonLog("中文描述")` + `@SaCheckPermission("/sys/user/add")`，**注解值必须等于 URL**
  - 返回统一 `CommonResult<T>`
  - 入参：`@RequestBody @Valid`；批量删除用 `@NotEmpty` 校验集合
- 查询：`QueryWrapper/LambdaQueryWrapper` 条件判空，拼接前调用 `checkSqlInjection()`
- 事务：写方法 `@Transactional(rollbackFor = Exception.class)`
- 异常：抛 `CommonException("中文消息")`，不抛 RuntimeException
- 转换：Hutool `BeanUtil`，不用 MapStruct
- 注释：类与公有方法写中文 Javadoc，含 `@author wqs` + `@date yyyy/MM/dd HH:mm`
- 版权头：**本项目不要求 Apache 版权头**，历史代码均无，不要补写

## 5. 数据模型约定

- 表名：小写 snake_case；平台表沿用 `sys_` / `dev_` 等前缀，新业务表自定小写前缀
- 主键：`VARCHAR(20)` String 雪花 ID（MyBatis-Plus `ASSIGN_ID`），不用自增
- 逻辑删除：`delete_flag`（`CommonEntity` + `@TableLogic` 自动生效），**不手写 delete_flag 条件**
- 审计字段：`create_time/create_user/update_time/update_user`（`CommonEntity` 自动填充，不用手写）
- 金额：`DECIMAL(12,2)` + `BigDecimal`，禁止浮点
- 时间：`Date` + `@Schema`
- 口令/密钥等敏感字段加密存储，禁止明文

## 6. 前端规范

- 目录：`src/api/{域}/xxxApi.js` + `src/views/{域}/{index,form,detail}.vue`
- api 封装：`import { baseRequest } from '@/utils/request'` → `const request = (url, ...arg) => baseRequest('/{域}/xxx/' + url, ...arg)` → `export default { ... }`
- 页面：Ant Design Vue 4 组件；缩进用 Tab；列表页 `index.vue` 用 `loadData()` 刷新，表单页 `form.vue` 通过 `defineExpose` 暴露 `onOpen(record)`
- 表格：`s-table` 与 `a-table` 两种写法并存，**沿用同域现有写法**
- 按钮权限：`hasPerm('sysUserAdd')` —— 驼峰按钮码，与菜单 SQL 的 BUTTON code 一致
- JS 非 TS；不用 Element Plus；注释保持中文、单行

## 7. 常用范式参考（写代码前必读）

| 场景 | 范本 |
|---|---|
| 后端六件套 | `wqs-plugin/wqs-plugin-sys/src/main/java/vip/wqs/sys/modular/user/` |
| 前端三件 | `wqs-admin-web/src/views/sys/` + `src/api/sys/` |
| 只读+分页最简例 | `wqs-plugin/wqs-plugin-dev` 各日志查询域 |

## 8. 禁止事项

1. 表名大写
2. 手写 `delete_flag` 条件、手写审计字段赋值
3. 明文存储口令；硬编码数据库口令
4. 前端写 TypeScript 或使用 Element Plus
5. 新增写接口不带 `@SaCheckPermission` / `@CommonLog`，或权限注解值与 URL 不一致
6. 跨插件直接 import 对方 entity/mapper（必须走 `*-api`）

## 9. 环境与验证

```powershell
# 后端（84 端口）
mvn -pl wqs-web-app -am spring-boot:run          # 或 IDE 运行 vip.wqs.Application
# 前端（83 端口）
cd wqs-admin-web; npm install; npm run dev
# 接口文档
# http://localhost:84/doc.html  (admin / 123456)
```

- 数据库：MySQL 库 `wqs`（配置见 `wqs-web-app/src/main/resources/application.properties` 的 `spring.datasource.dynamic.datasource.master`，支持 `MYSQL_HOST/MYSQL_PORT/MYSQL_DB/MYSQL_USERNAME/MYSQL_PASSWORD` 环境变量覆盖，生产环境必须覆盖默认口令）；建库/种子：`wqs-web-app/src/main/resources/wqs.sql`（平台 37 张表 + 菜单/配置/字典种子 + superAdmin/bizAdmin）
- Redis：默认 `127.0.0.1:6379`
- 超级管理员：`superAdmin`（种子密码为 SM3 哈希，无法反推；`DEV_CONFIG` 的 `SNOWY_SYS_DEFAULT_PASSWORD_FOR_B`=`Wqs@2026!` 只是新建 B 端用户的默认密码）
- git 操作前先自检：`git rev-parse --is-inside-work-tree`，不可用时降级为文件 mtime 判断，不中断流程

## 10. AI 协作约定

1. 开工前读本文件与 `.qoder/framework-config.json`
2. 新增/修改功能优先复用同域既有范式（先 Read 范本，再动手）
3. `.qoder/skills/` 多为平台通用规范，**与现有代码冲突时以现有代码为准**，并在回复中提示用户更新技能
4. 改数据库结构、改平台插件、执行破坏性命令前必须先征得用户确认
5. 完成后给出可验证步骤（启动 → 登录 → 页面操作 → Knife4j 调用）
