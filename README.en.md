# AnvilLib [中文](README.md) | **English**

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.2-green.svg)](https://minecraft.net/)
[![Maven Central](https://img.shields.io/maven-central/v/dev.anvilcraft.lib/anvillib-neoforge-1.21.2)](https://central.sonatype.com/search?q=anvillib)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.x-orange.svg)](https://neoforged.net/)
[![License](https://img.shields.io/badge/License-MIT%20License-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)

**AnvilLib** is a NeoForge mod library developed by [Anvil Dev](https://github.com/Anvil-Dev), providing Minecraft mod developers with a series of practical tools and frameworks.

## Features

AnvilLib adopts a modular design and includes the following functional modules:

| Module                     | Description            |
|---------------------------|---------------|
| **Config**                | Annotation-based configuration system     |
| **Integration**           | Mod compatibility integration framework     |
| **Recipe**                | In-world recipe system       |
| **Moveable Entity Block** | Support for block entities movable by pistons |
| **Registrum**             | Simplified registration system       |

## Module Introduction

### Config Module

Provides an annotation-based configuration management system to simplify the definition and management of mod configurations.

**Key Features:**

- Define configuration classes using `@Config` annotation
- Add configuration comments with `@Comment`
- Define numerical ranges with `@BoundedDiscrete`
- Create nested configurations with `@CollapsibleObject`
- Automatically generate client configuration GUI

**Usage Example:**

```java
@Config(name = "my_mod", typebirdsprite_ModConfig.Type.COMMON)
public class MyModConfig {
    @Comment("Enable debug mode")
    public boolean debugMode = false;

    @Comment("Maximum count")
    @BoundedDiscrete(min = 1, max = 100)
    public int maxCount = 10;
}

// Register configuration
MyModConfig config = ConfigManager.register("my_mod", MyModConfig::new);
```

### Integration Module

Provides a framework for mod integrations, supporting automatic loading of integration code based on the presence of other mods.

**Key Features:**

- Declare integration classes with `@Integration` annotation
- Support for version range matching
- Support for different runtime environments (CLIENT / DEDICATED_SERVER / DATA)

**Usage Example:**

```java
@Integration(value = "jei", version = "[19.0,)")
public class JEIIntegration {
    public void init() {
        // JEI integration logic
    }
}
```

### Recipe Module

Provides an in-world recipe system, allowing recipes to be executed in the world (rather than in crafting tables).

**Key Features:**

- Supports custom recipe triggers (Trigger)
- Supports recipe predicates (Predicate) for conditional checks
- Supports multiple recipe outcomes (Outcome)
- Built-in priority system
- Full datapack support

**Recipe Components:**

- **Trigger**: Conditions to trigger the recipe (e.g., item dropping, explosions)
- **Predicate**: Recipe matching conditions
- **Outcome**: Recipe execution results (e.g., spawning items, setting blocks)

### Moveable Entity Block Module

Allows blocks with block entities to be pushed by pistons while preserving their data.

**Usage Example:**

```java
public class MyBlock extends Block implements IMoveableEntityBlock {
    @Override
    public CompoundTag clearData(Level level, BlockPos pos) {
        // Return block entity data to preserve
        BlockEntity be = level.getBlockEntity(pos);
        return be != null ? be.saveWithoutMetadata(level.registryAccess()) : new CompoundTag();
    }

    @Override
    public void setData(Level level, BlockPos pos, CompoundTag nbt) {
        // Restore block entity data at new position
        BlockEntity be = level.getBlockEntity(pos);
        if (be != null) {
            be.loadAdditional(nbt, level.registryAccess());
        }
    }
}
```

### Registrum Module

A registration system based on [Registrate](https://github.com/IThundxr/Registrate), simplifying the registration process for items, blocks, entities, etc.

**Key Features:**

- Chain-style API design
- Automatic language file generation
- Automatic datapack generation
- Support for various builders

**Usage Example:**

```java
public static final Registrum REGISTRUM = Registrum.create("my_mod");

public static final RegistryEntry<Item> MY_ITEM = REGISTRUM
    .item("my_item", Item::new)
    .properties(p -> p.stacksTo(16))
    .register();
```

## Dependency Integration

### Gradle (Groovy DSL)

```groovy
repositories {
    mavenCentral() // This project is already uploaded to Maven Central
}

dependencies {
    // Full library
    implementation "dev.anvilcraft.lib:anvillib-neoforge-1.21.2:2.0.0"

    // Or import individual modules as needed
    implementation "dev.anvilcraft.lib:anvillib-config-neoforge-1.21.2:2.0.0"
    implementation "dev.anvilcraft.lib:anvillib-integration-neoforge-1.21.2:2.0.0"
    implementation "dev.anvilcraft.lib:anvillib-recipe-neoforge-1.21.2:2.0.0"
    implementation "dev.anvilcraft.lib:anvillib-moveable-entity-block-neoforge-1.21.2:2.0.0"
    implementation "dev.anvilcraft.lib:anvillib-registrum-neoforge-1.21.2:2.0.0"
}
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral() // This project is already uploaded to Maven Central
}

dependencies {
    implementation("dev.anvilcraft.lib:anvillib-neoforge-1.21.2:2.0.0")
}
```

## Building the Project

```bash
# Clone repository
git clone https://github.com/Anvil-Dev/AnvilLib.git
cd AnvilLib

# Build
./gradlew build
```

## Requirements

- Java 21+
- Minecraft 1.21.2
- NeoForge 21.1.x

## License

This project is licensed under the [MIT License](https://www.opensource.org/licenses/MIT).

Part of the Registrum module code is based on [Registrate](https://github.com/IThundxr/Registrate) and follows the Mozilla Public License 2.0.

## Author

- **Gugle** - Main developer

## Links

- [GitHub Repository](https://github.com/Anvil-Dev/AnvilLib)
- [Issue Tracking](https://github.com/Anvil-Dev/AnvilLib/issues)
