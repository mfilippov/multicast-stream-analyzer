@file:Suppress("GradlePackageUpdate")

plugins {
    java
    application
    id("me.filippov.gradle.jvm.wrapper") version "0.11.0"
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("me.filippov.analyzer.App")
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-all:1.10.19")
    implementation("org.eclipse.jetty:jetty-http:9.4.48.v20220622")
    implementation("org.eclipse.jetty:jetty-io:9.4.48.v20220622")
    implementation("org.eclipse.jetty:jetty-client:9.4.48.v20220622")
    implementation("org.eclipse.jetty:jetty-security:9.4.48.v20220622")
    implementation("org.eclipse.jetty:jetty-server:9.4.48.v20220622")
    implementation("org.eclipse.jetty:jetty-webapp:9.4.48.v20220622")
    implementation("com.sparkjava:spark-core:2.9.3")
    implementation("org.slf4j:slf4j-simple:1.7.36")
}