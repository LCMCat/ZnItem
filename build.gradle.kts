plugins {
    kotlin("jvm") version "2.2.0-Beta2"
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "tech.ccat.znitem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://jitpack.io")
    maven("https://repo.lucko.me/") {
        name = "luckperms"
    }
    maven("https://libraries.minecraft.net") {
        name = "mojang"
    }
}

dependencies {
    compileOnly(files("lib/kstats-1.0-SNAPSHOT.jar"))
    compileOnly(files("lib/NaSkill-1.0-SNAPSHOT.jar"))
    compileOnly(files("lib/CaLevel-1.0-SNAPSHOT.jar"))
    compileOnly("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.mojang:authlib:1.5.25")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks {
    runServer {
        minecraftVersion("1.21")
    }
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
    minimize {
        exclude(dependency("com.h2database:h2:.*"))
    }

    relocate("kotlin", "tech.ccat.znitem.shaded.kotlin")
    relocate("org.h2", "tech.ccat.znitem.shaded.h2")
    exclude("META-INF/maven/**")
    exclude("META-INF/versions/**")

    mergeServiceFiles()
}
