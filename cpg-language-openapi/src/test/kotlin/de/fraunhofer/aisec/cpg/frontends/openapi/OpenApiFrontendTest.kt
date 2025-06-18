package de.fraunhofer.aisec.cpg.frontends.openapi

import de.fraunhofer.aisec.cpg.graph.declarations.RecordDeclaration
import de.fraunhofer.aisec.cpg.graph.declarations.TranslationUnitDeclaration
import de.fraunhofer.aisec.cpg.graph.get
import de.fraunhofer.aisec.cpg.test.BaseTest
import de.fraunhofer.aisec.cpg.test.analyzeAndGetFirstTU
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class OpenApiFrontendTest : BaseTest() {
    @Test
    fun testSimpleSpec() {
        val topLevel = Path.of("src", "test", "resources")
        val tu = analyzeAndGetFirstTU(listOf(topLevel.resolve("api.yaml").toFile()), topLevel, true) {
            it.registerLanguage<OpenApiLanguage>()
        }
        assertIs<TranslationUnitDeclaration>(tu)
        val record = tu.records["/hello"]
        assertIs<RecordDeclaration>(record)
        assertNotNull(record.methods["getHello"])
        assertEquals(1, record.fields.size)
    }

    @Test
    fun testPetstoreSpec() {
        val topLevel = Path.of("src", "test", "resources")
        val tu = analyzeAndGetFirstTU(listOf(topLevel.resolve("petstore.yaml").toFile()), topLevel, true) {
            it.registerLanguage<OpenApiLanguage>()
        }
        assertIs<TranslationUnitDeclaration>(tu)
        val record = tu.records["/pets"]
        assertIs<RecordDeclaration>(record)
        val method = record.methods["listPets"]
        assertNotNull(method)
        val field = record.fields["limit"]
        assertNotNull(field)
        assertEquals("integer", field.type.name.toString())
    }
}
