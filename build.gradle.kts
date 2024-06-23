import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom") version "1.7-SNAPSHOT"
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
}

version = property("mod_version").toString()
group = property("maven_group").toString()

base {
    archivesName.set(property("archives_base_name").toString())
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
}

sourceSets {
    create("testmod") {
        compileClasspath += main.get().compileClasspath
        runtimeClasspath += main.get().runtimeClasspath
    }
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create("monkeyconfig") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets["client"])
        }
    }

    runs {
        create("testmodClient") {
            client()
            name = "Testmod Client"
            source(sourceSets["testmod"])
        }

        create("testmodServer") {
            server()
            name = "Testmod Server"
            source(sourceSets["testmod"])
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")

    include(implementation("com.electronwill.night-config:core:${property("night_config_version")}")!!)
    include(implementation("com.electronwill.night-config:toml:${property("night_config_version")}")!!)
    include(implementation("com.electronwill.night-config:json:${property("night_config_version")}")!!)
    include(implementation("com.electronwill.night-config:yaml:${property("night_config_version")}")!!)
    include(implementation("com.electronwill.night-config:hocon:${property("night_config_version")}")!!)

    "testmodImplementation"(sourceSets.main.get().output)
    "testmodImplementation"("org.junit.jupiter:junit-jupiter-api:5.8.2")
    "testmodRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand(mapOf("version" to project.version))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 17
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Jar> {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

// configure the maven publication
publishing {
    repositories {
        maven("https://maven.enjarai.dev/releases") {
            name = "enjaraiMaven"
            credentials(PasswordCredentials::class.java)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            groupId = project.group.toString()
            artifactId = base.archivesName.get()
            version = project.version.toString()
            from(components["java"])
        }
    }
}