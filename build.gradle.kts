import org.gradle.testing.jacoco.tasks.JacocoReport

repositories {
    mavenCentral()
}

plugins {
    jacoco
}


tasks.register<JacocoReport>("jacocoRootReport") {

    dependsOn(subprojects.map { it.tasks.named("test") })

    val executionDataFiles = subprojects.map {
        it.layout.buildDirectory.file("jacoco/test.exec")
    }

    executionData.setFrom(executionDataFiles)

    val sourceSets = subprojects.mapNotNull {
        it.extensions.findByName("sourceSets") as? org.gradle.api.tasks.SourceSetContainer
    }

    val mainSourceSets = sourceSets.map { it["main"] }

    sourceDirectories.setFrom(mainSourceSets.map { it.allSource.srcDirs })
    classDirectories.setFrom(mainSourceSets.map { it.output })

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }
}
