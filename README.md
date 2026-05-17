ZnItem
------
*A Comprehensive Custom Item Management Plugin for Minecraft Servers*

## 🗡️ Features
- **Custom Item System**: Create custom items with unique IDs, materials, rarities, and stats
- **Item Inheritance Hierarchy**: Abstract base classes for each item type (Sword, Bow, Axe, Wand, Armor, etc.)
- **Rarity System**: 8 rarity tiers (Common, Rare, Epic, Legendary, Mythic, Divine, Special, Ultra Special) with color coding
- **Reforge System**: Apply reforges to items for stat bonuses based on rarity
- **Custom Enchantment System**: Custom enchantments that can exceed vanilla level limits
- **Gem Socket System**: Socket gems into items for additional stat bonuses
- **Skill System**: Attach custom skills to items with various trigger types (right-click, left-click, throw, equip, etc.)
- **Hot Power Books**: Upgrade items with Hot Power Books for stat increases
- **Item Refactoring**: Increase item rarity through refactoring
- **Vanilla Item Support**: Automatic stat calculation for vanilla weapons and armor
- **KStats Integration**: Seamless integration with KStats for stat contribution
- **H2 Database Backup**: Automatic player inventory backup system

## 🏗️ Item Type Hierarchy
```
AbstractZnItem
├── ZnSword (剑)
├── ZnBow (弓)
├── ZnAxe (斧)
├── ZnWand (魔杖)
├── ZnHelmet (头盔)
├── ZnChestplate (胸甲)
├── ZnLeggings (护腿)
├── ZnBoots (靴子)
├── ZnAccessory (饰品)
├── ZnPickaxe (镐)
├── ZnHoe (锄头)
├── ZnFishingRod (鱼竿)
├── ZnDrill (钻头)
├── ZnShovel (锹)
├── ZnShears (剪刀)
├── ZnConsumable (消耗品)
├── ZnMaterial (材料)
├── ZnBlock (方块)
├── ZnRune (装饰品)
├── ZnPet (宠物)
├── ZnGem (宝石)
├── ZnDeployable (部署物)
├── ZnArrow (箭)
├── ZnVanilla (原版物品)
├── ZnMemento (纪念品)
└── ZnOther (其它)
```

## 💎 Rarity System
| Rarity | Color | Priority |
|--------|-------|----------|
| Common | §7 (Gray) | 0 |
| Rare | §9 (Blue) | 1 |
| Epic | §5 (Purple) | 2 |
| Legendary | §6 (Gold) | 3 |
| Mythic | §d (Pink) | 4 |
| Divine | §b (Aqua) | 5 |
| Special | §c (Red) | 6 |
| Ultra Special | §4 (Dark Red) | 7 |

## ⚔️ Reforge System
Different item types have different available reforges:
- **Sword**: Infernal (烈火), Deception (谎言), Weathered (风化)
- **Bow**: Mechanism (机关), Rigid (刚硬)

Each reforge provides stat bonuses that scale with item rarity.

## 🔮 Gem System
5 gem types with 5 quality levels:
- **Ruby** (红宝石): +Health
- **Amethyst** (紫水晶): +Defense
- **Sapphire** (蓝宝石): +Wisdom
- **Tourmaline** (碧玺): +Strength
- **Agate** (玛瑙): +Crit Damage

Quality levels: Rough (粗糙) → Flawed (瑕疵) → Fine (优良) → Flawless (无暇) → Perfect (完美)

## 🎯 Skill Trigger Types
- Right Click (右键点击)
- Left Click (左键点击)
- Throw (投掷)
- Equip (穿戴)
- Main Hand (放于主手)
- Off Hand (放于副手)
- Both Hands (放于主和副手)
- Inventory (放在背包内)

## 📜 Commands
| Command | Description | Permission |
|---------|-------------|------------|
| `/summonitem <id>` | Generate a ZnItem | `znitem.admin.summon` |
| `/summonitem <player> <id>` | Give ZnItem to player | `znitem.admin.summon` |
| `/vanillatoznitem` | Convert held vanilla item to ZnItem | `znitem.admin.vanillatoznitem` |
| `/renameitem <name>` | Rename held ZnItem | `znitem.admin.rename` |
| `/refactoritem` | Refactor held ZnItem (increase rarity) | `znitem.admin.refactor` |
| `/znench <player> <type> <level>` | Apply custom enchantment | `znitem.admin.enchant` |
| `/hotpowerbook <player> <amount>` | Set Hot Power Book count | `znitem.admin.hotpowerbook` |
| `/itemgem <action> ...` | Manage gem sockets | `znitem.admin.gem` |

## 🔌 Dependencies
- **Required**: KStats (for stat contribution)
- **Optional**: NaSkill (for combat level integration)

## 📦 NBT Data Structure
ZnItem stores all item data in Persistent Data Container:
- `znitem_id`: Item enum ID
- `znitem_uuid`: Unique item UUID
- `znitem_rename`: Custom rename
- `znitem_reforge`: Reforge type
- `znitem_refactored`: Whether refactored
- `znitem_enchants`: Custom enchantments
- `znitem_hotpowerbooks`: Hot Power Book count
- `znitem_gem_slots`: Gem socket data

## 🛠️ API Usage
```kotlin
val znItemAPI = Bukkit.getServicesManager().getRegistration(ZnItemAPI::class.java)?.provider

// Create item
val itemStack = znItemAPI?.createItemStack(ZnItemEnum.ALPHA_SWORD)

// Check if item is ZnItem
if (znItemAPI?.isZnItem(itemStack) == true) {
    // Get stats
    val stats = znItemAPI.calculateStats(itemStack, combatLevel)
}
```

## 📝 Example Item: Alpha Sword
```
§cAlpha之鱼
§7
§7伤害: §c+1.0
§7力量: §4+1
§7生命: §c+1
§7防御: §9+1
§7速度: §3+1
§7暴击几率: §d+1%
§7暴击伤害: §5+1%
§7智慧: §3+1
§7[⚛️]§7[⚛️]§7[⚛️]
§7
§cAlpha测试咸鱼...
§7
§e右键点击: 你好世界！
§7向世界发出第一声问候！
§9法力消耗: §320
§9冷却: §a1秒
§7
§e手持: 快一点！
§7高速开发中...
§7后面忘了
§7
§4❣ §c需要 1级 战斗等级
§4❣ §c需要 1级 全局等级
§7
§c特殊之剑
```
