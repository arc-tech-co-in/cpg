plugins { id("cpg.frontend-conventions") }

mavenPublishing {
    pom {
        name.set("Code Property Graph - OpenAPI Frontend")
        description.set("An OpenAPI specification frontend for the CPG")
    }
}

dependencies {
    implementation(libs.swagger.parser)

    testImplementation(projects.cpgAnalysis)
}
