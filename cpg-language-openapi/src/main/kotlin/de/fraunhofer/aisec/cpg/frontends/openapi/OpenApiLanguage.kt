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

import de.fraunhofer.aisec.cpg.frontends.Language
import de.fraunhofer.aisec.cpg.graph.types.*
import kotlin.reflect.KClass

/** A very small language representing OpenAPI specifications. */
class OpenApiLanguage : Language<OpenApiLanguageFrontend>() {
    override val fileExtensions = listOf("yaml", "yml", "json")
    override val namespaceDelimiter: String = "/"

    @Transient
    override val frontend: KClass<out OpenApiLanguageFrontend> = OpenApiLanguageFrontend::class

    override val builtInTypes: Map<String, Type> =
        mapOf(
            "string" to StringType("string", language = this),
            "integer" to IntegerType("integer", 32, this, NumericType.Modifier.SIGNED),
            "number" to FloatingPointType("number", 64, this, NumericType.Modifier.SIGNED),
            "boolean" to BooleanType("boolean", language = this),
        )

    override val compoundAssignmentOperators: Set<String> = emptySet()
}
