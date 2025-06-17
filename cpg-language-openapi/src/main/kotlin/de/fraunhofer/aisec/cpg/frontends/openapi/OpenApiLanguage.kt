package de.fraunhofer.aisec.cpg.frontends.openapi

import de.fraunhofer.aisec.cpg.frontends.Language
import de.fraunhofer.aisec.cpg.graph.types.StringType
import de.fraunhofer.aisec.cpg.graph.types.Type
import kotlin.reflect.KClass

/** A very small language representing OpenAPI specifications. */
class OpenApiLanguage : Language<OpenApiLanguageFrontend>() {
    override val fileExtensions = listOf("yaml", "yml", "json")
    override val namespaceDelimiter: String = "/"

    @Transient override val frontend: KClass<out OpenApiLanguageFrontend> = OpenApiLanguageFrontend::class

    override val builtInTypes: Map<String, Type> = mapOf("string" to StringType("string", language = this))

    override val compoundAssignmentOperators: Set<String> = emptySet()
}
