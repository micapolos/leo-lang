package leo21.token.type.compiler

import leo21.token.compiler.FunctionTypeCompiler
import leo21.token.compiler.process
import leo21.token.processor.TokenProcessor
import leo21.type.Type

sealed class TypeSibling
data class FunctionTypeCompilerTypeSibling(val functionTypeCompiler: FunctionTypeCompiler) : TypeSibling()

fun TypeSibling.process(type: Type): TokenProcessor =
	when (this) {
		is FunctionTypeCompilerTypeSibling -> functionTypeCompiler.process(type)
	}