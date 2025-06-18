/*
 * Copyright (c) 2025, Fraunhofer AISEC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *                    $$$$$$\  $$$$$$$\   $$$$$$\
 *                   $$  __$$\ $$  __$$\ $$  __$$\
 *                   $$ /  \__|$$ |  $$ |$$ /  \__|
 *                   $$ |      $$$$$$$  |$$ |$$$$\
 *                   $$ |      $$  ____/ $$ |\_$$ |
 *                   $$ |  $$\ $$ |      $$ |  $$ |
 *                   \$$$$$   |$$ |      \$$$$$   |
 *                    \______/ \__|       \______/
 *
 */
package de.fraunhofer.aisec.cpg.frontends.openapi

import de.fraunhofer.aisec.cpg.graph.declarations.RecordDeclaration
import de.fraunhofer.aisec.cpg.graph.declarations.TranslationUnitDeclaration
import de.fraunhofer.aisec.cpg.graph.fields
import de.fraunhofer.aisec.cpg.graph.get
import de.fraunhofer.aisec.cpg.graph.methods
import de.fraunhofer.aisec.cpg.graph.records
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
        val tu =
            analyzeAndGetFirstTU(listOf(topLevel.resolve("api.yaml").toFile()), topLevel, true) {
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
        val tu =
            analyzeAndGetFirstTU(
                listOf(topLevel.resolve("petstore.yaml").toFile()),
                topLevel,
                true,
            ) {
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

    @Test
    fun testOfficialPetstoreSpec() {
        val topLevel = Path.of("src", "test", "resources")
        val tu =
            analyzeAndGetFirstTU(
                listOf(topLevel.resolve("petstore-official.json").toFile()),
                topLevel,
                true,
            ) {
                it.registerLanguage<OpenApiLanguage>()
            }
        assertIs<TranslationUnitDeclaration>(tu)
        val record = tu.records["/pet"]
        assertIs<RecordDeclaration>(record)
        assertNotNull(record.methods["addPet"])
    }
}
