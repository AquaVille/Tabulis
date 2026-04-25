plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.lombok)
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://oss.sonatype.org/content/repositories/central")
}

dependencies {
    // HikariCP
    api(libs.hikaricp)

    // Utility libraries
    compileOnly(libs.guava)
    compileOnly(libs.annotations)

    // Jabel - older Java support
    annotationProcessor(rootProject.libs.jabel)
    compileOnly(rootProject.libs.jabel)
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "16"
        options.release.set(8)
        options.encoding = Charsets.UTF_8.name()

        val toolchainService = project.extensions.getByType(JavaToolchainService::class.java)
        javaCompiler.set(
            toolchainService.compilerFor {
                languageVersion.set(JavaLanguageVersion.of(16))
            }
        )
    }
}