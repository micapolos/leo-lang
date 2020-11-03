package leo21.dictionary

import leo21.typed.ArrowTyped
import leo21.typed.Typed

sealed class Definition
data class FunctionDefinition(val arrowTyped: ArrowTyped) : Definition()
data class ConstantDefinition(val typed: Typed) : Definition()
