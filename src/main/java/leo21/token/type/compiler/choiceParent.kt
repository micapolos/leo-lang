package leo21.token.type.compiler

import leo21.token.processor.Processor
import leo21.type.Choice
import leo21.type.line

sealed class ChoiceParent
data class TypeCompilerChoiceParent(val typeCompiler: TypeCompiler) : ChoiceParent()

fun ChoiceParent.plus(choice: Choice): Processor =
	when (this) {
		is TypeCompilerChoiceParent -> typeCompiler.plus(line(choice))
	}
