# JEI SlashBlade

为 [SlashBlade: Resharped](https://www.curseforge.com/minecraft/mc-mods/slashblade-resharped) 提供 **JEI** 与 **EMI**
配方查询支持的附属模组。

把拔刀剑的「特殊斩击（SA）」「特殊效果（SE）」「生物击杀掉落」「拔刀特殊描述」四类信息，以配方页面的形式展示在 JEI / EMI 中，并支持数据包自定义。

- **Minecraft**：1.20.1
- **加载器**：Forge 47.x（ModDevGradle Legacy）
- **Java**：17
- **当前版本**：1.0.7
- **模组 ID**：`jei_slashblade`

> **关于 1.21.1**
> 本修改基于原模组 1.20.1 版本，1.21.1 的拔刀剑本体已内置相关支持，因此本模组不做 1.21.1 适配，望谅解。

---

## 功能

| 分类                       | JEI | EMI | 说明                             |
|--------------------------|:---:|:---:|--------------------------------|
| **SA** — Special Arts    |  ✅  |  ✅  | 展示每种特殊斩击，以及拥有该 SA 的拔刀剑         |
| **SE** — Special Effects |  ✅  |  ✅  | 展示每种特殊效果、可否复制/删除，以及拥有该 SE 的拔刀剑 |
| **Drop** — 击杀掉落          |  ✅  |  ✅  | 展示生物击杀掉落某把拔刀剑的概率，附实体 3D 渲染     |
| **BladeDesc** — 拔刀特殊描述   |  ✅  |  ✅  | 为特定拔刀展示额外的说明文本（可滚动）            |

## 环境依赖

| 依赖                    | 版本            | 说明          |
|-----------------------|---------------|-------------|
| Minecraft             | 1.20.1        |             |
| Forge                 | 47.3.29       | 运行时 `[47,)` |
| SlashBlade: Resharped | 1.9.64        | **必需**      |
| JEI                   | 15.74.0.228   | 可选，二者装其一即可  |
| EMI                   | 1.1.24+1.20.1 | 可选          |

JEI 与 EMI 同时安装也没有问题：由于本模组自带原生 EMI 插件（`@EmiEntrypoint`），EMI 的 JEMI 兼容层会**跳过**本模组的 JEI 插件（
`JemiUtil.getHandledMods()` 收集带 `@EmiEntrypoint` 的模组 ID，`PluginCallerMixin` 据此过滤），不会出现两套配方重复注册。此时界面由
EMI 呈现。

## 配置

客户端配置位于 `config/jei_slashblade-client.toml`：

| 键                                      | 默认值           | 说明                    |
|----------------------------------------|---------------|-----------------------|
| `filter_drop`                          | `true`        | 仅显示「生物已注册且拔刀已注册」的掉落配方 |
| `entity_rotation_speed`                | `0.0125`      | 实体旋转速度，设为 `0` 关闭旋转    |
| `sa_recipe_width` / `sa_recipe_height` | `128` / `100` | **仅 JEI 生效**          |
| `se_recipe_width` / `se_recipe_height` | `176` / `120` | **仅 JEI 生效**          |

> EMI 侧的宽高目前是硬编码的（SA 160×86、SE 160×96、Drop 130×48、Desc 160×86），因此上述 `*_recipe_width/height` 对 EMI 不生效。

## 数据驱动

资源路径：`assets/<你的命名空间>/...`，随资源包重载生效。

### 拔刀特殊描述 `blade_desc`

存放于 `assets/<namespace>/blade_desc/*.json`：

```json
{
  "bladeName": "slashblade:koseki",
  "description": "slashblade.koseki.desc"
}
```

- `bladeName`：拔刀剑定义（`SlashBladeDefinition`）的注册名，必须已存在。
- `description`：语言文件键名，会经 `Component.translatable` 翻译。

本模组已内置 `slashblade:koseki` 的示例。

### 实体渲染参数 `show_entity`

存放于 `assets/<namespace>/show_entity/*.json`，用于调整 Drop 分类中实体的渲染尺寸：

```json
{
  "entityType": "example:ciallo",
  "scale": 5,
  "yOffset": 0
}
```

- `entityType`：实体注册名。
- `scale`：渲染缩放（整数）。
- `yOffset`：纵向偏移（浮点，可选，默认 `0`）。

不提供该文件时会自动按实体碰撞箱计算缩放。

## 致谢

- **原作者**：`_yi_ran_` — JEI 支持、数据驱动与整体实现。
- **EMI 兼容**：青衣_璃（[RLlufee](https://github.com/RLlufee)），提交 `cd55266`「emi的兼容 来自RLlufee」，包含 `compat/emi/`
  下全部 6 个类。
- **SlashBlade: Resharped**：flammpfeil 及 Resharped 维护者。
- 滚动框的设计思路参考了 EMI 与 JEI 的 GUI 源码，感谢开源。

## 许可

All Rights Reserved，Copyright (c) 2026 `_yi_ran_`。
基于 NeoForged MDK 模板的部分遵循 [TEMPLATE_LICENSE.txt](TEMPLATE_LICENSE.txt)（MIT）。
