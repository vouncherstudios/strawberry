plugins {
    id("net.kyori.indra") version Versions.INDRA
    id("com.gradle.plugin-publish") version Versions.GRADLE_PUBLISH
    id("net.kyori.indra.publishing.gradle-plugin") version Versions.INDRA
    id("net.kyori.indra.licenser.spotless") version Versions.INDRA
}

group = "com.vouncherstudios"
version = "1.0.1"
description = "A gradle plugin to apply common organization build settings."

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    implementation("net.kyori:indra-common:${Versions.INDRA}")
    implementation("com.github.johnrengelman:shadow:${Versions.SHADOW}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${Versions.JACKSON}")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${Versions.JACKSON}")
}

indra {
    javaVersions {
        target(11)
    }

    github("vouncherstudios", "strawberry")
    mitLicense()

    configurePublications {
        pom {
            organization {
                name.set("Vouncher Studios")
                url.set("https://github.com/vouncherstudios")
            }
            developers {
                developer {
                    id.set("WitchBoo")
                    name.set("Luís Mendes")
                    email.set("soconfirmo@hotmail.com")
                    timezone.set("America/Sao_Paulo")
                }
            }
        }
    }
}

indraPluginPublishing {
    website("https://github.com/vouncherstudios/strawberry")
    plugin(
        project.name,
        "com.vouncherstudios.strawberry.StrawberryPlugin",
        "Strawberry",
        project.description,
        listOf("java", "minecraft", "boilerplate")
    )
}

indraSpotlessLicenser {
    licenseHeaderFile(file("LICENSE"))
    newLine(true)
}