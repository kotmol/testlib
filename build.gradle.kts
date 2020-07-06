
// https://docs.gradle.org/current/userguide/kotlin_dsl.html

// template https://github.com/dgraciac/build-gradle-kts/blob/master/build.gradle.kts

plugins {
    kotlin("jvm") version "1.3.72"
    `maven-publish`

}

allprojects {
    apply(plugin = "kotlin")
    java {
        withSourcesJar()
        withJavadocJar()
    }
    tasks.withType<Test> {
        testLogging {
            showStandardStreams = false
            events("skipped", "failed")
            showExceptions = true
            showCauses = true
            showStackTraces = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
        afterSuite(printTestResult)
    }
}

val printTestResult: KotlinClosure2<TestDescriptor, TestResult, Void>
    get() = KotlinClosure2({ desc, result ->

        if (desc.parent == null) { // will match the outermost suite
            println("------")
            println(
                    "Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} " +
                            "successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)"
            )
            println(
                    "Tests took: ${result.endTime - result.startTime} ms."
            )
            println("------")
        }
        null
    })

allprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}


allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        "implementation"(platform(kotlin("bom")))
        "implementation"(kotlin("stdlib-jdk8"))
        "implementation"(kotlin("reflect"))
        "implementation"("javax.inject:javax.inject:1")

        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.6.2")
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.6.2")
        "testImplementation"("org.junit.jupiter:junit-jupiter-params:5.6.2")
        "testImplementation"("io.mockk:mockk:1.10.0")
        "testImplementation"("org.assertj:assertj-core:3.16.0")
        "testImplementation"("com.github.tomakehurst:wiremock-jre8:2.26.3")
    }
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "testlib"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("testlib")
                description.set("A test library")
                url.set("https://www.kotmol.com")
//                properties.set(mapOf(
//                        "myProp" to "value",
//                        "prop.with.dots" to "anotherValue"
//                ))
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("jimandreas")
                        name.set("Jim Andreas")
                        email.set("jim@kotmol.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://example.com/my-library.git")
                    developerConnection.set("scm:git:ssh://example.com/my-library.git")
                    url.set("http://example.com/my-library/")
                }
            }
        }
    }