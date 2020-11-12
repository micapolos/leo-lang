package leo21.token.type.compiler

import leo21.token.processor.TokenProcessor
import leo21.type.Choice
import leo21.type.type

sealed class ChoiceParent
data class TypeCompilerChoiceParent(val typeCompiler: TypeCompiler) : ChoiceParent()

fun ChoiceParent.plus(choice: Choice): TokenProcessor =
	when (this) {
		is TypeCompilerChoiceParent -> typeCompiler.process(type(choice))
	}
