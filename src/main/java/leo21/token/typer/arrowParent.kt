package leo21.token.typer

import leo21.token.processor.TokenProcessor
import leo21.type.Arrow

sealed class ArrowParent
data class TypeCompilerArrowParent(val typeCompiler: TokenTypeCompiler) : ArrowParent()

fun ArrowParent.plus(arrow: Arrow): TokenProcessor =
	when (this) {
		is TypeCompilerArrowParent -> TODO()
	}