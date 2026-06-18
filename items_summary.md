# 饰品与装备效果整理

## 已明确效果

| 编号 | 名称 | 槽位 | 效果 | 冷却 / 备注 |
|---:|---|---|---|---|
| 2 | 先驱能核 | 徽章 | 消耗背包内木棍，可获得长按空格飞行效果；全伤害类型 +10%；免疫 `champions:paralysis` | - |
| 3 | 熔岩护心盾 | 徽章 | 佩戴后获得不死图腾效果，包含不死图腾动画；全伤害类型 +10% |冷却 3 分钟 |
| 4 | 法老项链 | 项链 | 击杀亡灵生物有 20% 概率掉落钻石；对亡灵生物伤害 +10%；生命值低于 30% 时，对 10 格内敌对生物施加 `cataclysm:stun` 5 秒；全伤害类型 +10% | - |
| 9 | 雷觅能核 | 项链 | 免疫 `champions:reflective` 的反射伤害 | - |
| 12 | 熔炉之核 | 徽章 | 消耗背包内 `cataclysm:lava_power_cell`，可持续补充饥饿值，并给予生命恢复 II、抗性提升 II；全伤害类型 +10% | - |
| 13 | 熔噬之徽 | 项链 | 佩戴后可在岩浆上行走；地狱生物造成的伤害 -10% | - |
| 25 | 灵骸之翼 | 背部 | 获得飞行效果；守护天使：生命值低于 15% 时，获得 5 秒无敌后 立刻回复 30% 生命值；全伤害类型 +10% |冷却 2 分钟 |

## 维度 / 环境掉落类

| 编号 | 名称 | 槽位 | 触发环境 | 效果 | 冷却 / 备注 |
|---:|---|---|---|---|---|
| 22 | 遗寂图腾 | 徽章 | `alex_caves_dimensions:forlorn_hollows` | 在该维度内击杀生物，有 30% 概率掉落苹果、10% 概率掉落木棍；按 K 键可获得 10 秒 `alexscaves:darkness_incarnate` | 3 分钟 |
| 18 | 糖果心 | 徽章 | `alex_caves_dimensions:candy_cavity` | 在该维度内击杀生物，有 30% 概率掉落苹果、10% 概率掉落木棍 | - |
| 16 | 深渊逆鳞 | 徽章 | `alex_caves_dimensions:abyssal_chasm` | 获得渊视效果 `alexscaves:deepsight`、游泳速度增加、水下呼吸；对海洋生物额外造成 5% 伤害；在该维度内击杀生物，有 30% 概率掉落苹果、10% 概率掉落木棍；水下射箭不影响射速和射程 | - |
| 7 | 烬灭之核 | 徽章 | `alex_caves_dimensions:primordial_caves` | 在该维度内击杀生物，有 30% 概率掉落苹果、10% 概率掉落木棍 | - |
| 未编号 | 磁力徽章 | 徽章 | `alex_caves_dimensions:magnetic_caves` | 在该维度内击杀生物，有 30% 概率掉落苹果、10% 概率掉落木棍；吸取 8 格内的掉落物和经验值 | - |
| 5 | 撼地源晶 | 徽章 | `alexscaves:irradiated` | 在该环境内击杀生物，有 30% 概率掉落苹果、10% 概率掉落木棍；免疫 `alexscaves:irradiated` 效果 | - |

## 待补充条目

| 编号 | 名称 | 槽位 | 当前信息 |
|---:|---|---|---|
| 23 | 幽匿断刃 | 未注明 | 待补充 |
| 未编号 | 湮灭徽章 | 徽章 | 待补充 |
| 15 | 沙蚀长笛 | 未注明 | 待补充 |
| 17 | 深渊之心 | 未注明 | 待补充 |
| 6 | 湫渃符文 | 未注明 | 待补充 |
| 8 | 攫灵戒 | 未注明 | 待补充 |
| 20 | 虚空手套 | 手套 | 待补充 |

## 属性 ID 对照

### 全类型属性 ID

| 属性 ID |
|---|
| `spell_power:air` |
| `spell_power:arcane` |
| `spell_power:earth` |
| `spell_power:fire` |
| `spell_power:frost` |
| `spell_power:healing` |
| `spell_power:lightning` |
| `spell_power:water` |
| `ranged_weapon:damage` |
| `minecraft:generic.attack_damage` |

### 其他属性 ID

| 类型 | 属性 ID |
|---|---|
| 冷却 | `spell_power:haste` |
| 暴击率 | `spell_power:critical_chance` |
| 暴击伤害 | `spell_power:critical_damage` |
