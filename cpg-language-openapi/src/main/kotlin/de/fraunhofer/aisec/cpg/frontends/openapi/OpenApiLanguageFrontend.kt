package de.fraunhofer.aisec.cpg.frontends.openapi

import de.fraunhofer.aisec.cpg.TranslationContext
import de.fraunhofer.aisec.cpg.frontends.Language
import de.fraunhofer.aisec.cpg.frontends.LanguageFrontend
import de.fraunhofer.aisec.cpg.frontends.TranslationException
import de.fraunhofer.aisec.cpg.graph.*
import de.fraunhofer.aisec.cpg.graph.declarations.FieldDeclaration
import de.fraunhofer.aisec.cpg.graph.declarations.RecordDeclaration
import de.fraunhofer.aisec.cpg.graph.declarations.TranslationUnitDeclaration
import de.fraunhofer.aisec.cpg.graph.types.*
import de.fraunhofer.aisec.cpg.sarif.PhysicalLocation
import java.io.File
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.parser.core.models.ParseOptions
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.Parameter

/**
 * A minimal frontend parsing OpenAPI specifications. Endpoints are modeled as
 * [RecordDeclaration]s, operations as [MethodDeclaration]s and parameters as
 * [FieldDeclaration]s of the respective record.
 */
class OpenApiLanguageFrontend(ctx: TranslationContext, language: Language<OpenApiLanguageFrontend>) :
    LanguageFrontend<Any, Any?>(ctx, language) {

    override fun parse(file: File): TranslationUnitDeclaration {
        val parser = OpenAPIV3Parser()
        val options = ParseOptions()
        options.isResolve = true
        val api = parser.read(file.absolutePath, null, options)
            ?: throw TranslationException("Could not parse OpenAPI file")

        val tu = newTranslationUnitDeclaration(name = file.name, rawNode = api)
        scopeManager.resetToGlobal(tu)

        api.paths?.forEach { (path, item) ->
            val record = newRecordDeclaration(name = path, kind = "endpoint", rawNode = item)
            scopeManager.enterScope(record)
            item.readOperationsMap().forEach { (method, op) ->
                val methodDecl = newMethodDeclaration(op.operationId ?: method.name.lowercase(), rawNode = op)
                op.parameters?.forEach { param ->
                    val field = newFieldDeclaration(name = param.name, rawNode = param)
                    param.schema?.let { field.type = typeOf(it) }
                    record.addField(field)
                }
                record.addMethod(methodDecl)
            }
            scopeManager.leaveScope(record)
            scopeManager.addDeclaration(record)
            tu.addDeclaration(record)
        }
        return tu
    }

    override fun typeOf(type: Any?): Type {
        return when (type) {
            is Schema<*> -> {
                val t = type.type ?: return UnknownType.getUnknownType(language)
                language.builtInTypes[t] ?: UnknownType.getUnknownType(language)
            }
            is Parameter -> {
                type.schema?.let { return typeOf(it) }
                UnknownType.getUnknownType(language)
            }
            else -> UnknownType.getUnknownType(language)
        }
    }

    override fun codeOf(astNode: Any): String? {
        return null
    }

    override fun locationOf(astNode: Any): PhysicalLocation? {
        return null
    }

    override fun setComment(node: Node, astNode: Any) {
        // no comments available
    }
}
