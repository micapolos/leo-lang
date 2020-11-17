package leo21.token.type.compiler

import leo21.token.processor.Processor
import leo21.type.Arrow
import leo21.type.line

sealed class ArrowParent
data class TypeCompilerArrowParent(val typeCompiler: TypeCompiler) : ArrowParent()

fun ArrowParent.plus(arrow: Arrow): Processor =
	when (this) {
		is TypeCompilerArrowParent -> typeCompiler.plus(line(arrow))
	}