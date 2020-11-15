package leo21.token.type.compiler

import leo21.token.body.FunctionCompiler
import leo21.token.body.plus
import leo21.token.processor.ArrowCompilerTokenProcessor
import leo21.token.processor.ChoiceCompilerTokenProcessor
import leo21.token.processor.TokenProcessor
import leo21.type.Type
import leo21.type.plus
import leo21.type.recursive
import leo21.type.type

sealed class TypeParent
data class TypeNameTypeParent(val typeCompiler: TypeCompiler, val name: String) : TypeParent()
data class ChoiceNameTypeParent(val choiceCompiler: ChoiceCompiler, val name: String) : TypeParent()
data class ArrowNameTypeParent(val arrowCompiler: ArrowCompiler, val lhs: Type, val name: String) : TypeParent()
data class ArrowDoingTypeParent(val arrowCompiler: ArrowCompiler, val lhs: Type) : TypeParent()
data class RecursiveTypeParent(val typeCompiler: TypeCompiler) : TypeParent()
data class FunctionCompilerTypeParent(val functionCompiler: FunctionCompiler) : TypeParent()

fun TypeParent.plus(type: Type): TokenProcessor =
	when (this) {
		is TypeNameTypeParent -> typeCompiler.plus(name, type)
		is ChoiceNameTypeParent -> ChoiceCompilerTokenProcessor(choiceCompiler.plus(name, type))
		is ArrowDoingTypeParent -> ArrowCompilerTokenProcessor(arrowCompiler.plusDoing(lhs, type))
		is ArrowNameTypeParent -> ArrowCompilerTokenProcessor(arrowCompiler.set(lhs.plus(name compiledLineTo type)))
		is RecursiveTypeParent -> typeCompiler.process(type(recursive(type)))
		is FunctionCompilerTypeParent -> functionCompiler.plus(type)
	}
