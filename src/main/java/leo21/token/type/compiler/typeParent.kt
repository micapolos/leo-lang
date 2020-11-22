package leo21.token.type.compiler

import leo13.onlyOrNull
import leo21.definition.typeDefinition
import leo21.token.body.DefineCompiler
import leo21.token.body.FunctionCompiler
import leo21.token.body.plus
import leo21.token.processor.ArrowCompilerProcessor
import leo21.token.processor.ChoiceCompilerProcessor
import leo21.token.processor.Processor
import leo21.token.processor.processor
import leo21.type.Type
import leo21.type.plus

sealed class TypeParent
data class TypeNameTypeParent(val typeCompiler: TypeCompiler, val name: String) : TypeParent()
data class ChoiceNameTypeParent(val choiceCompiler: ChoiceCompiler, val name: String) : TypeParent()
data class ArrowNameTypeParent(val arrowCompiler: ArrowCompiler, val lhs: Type, val name: String) : TypeParent()
data class ArrowDoingTypeParent(val arrowCompiler: ArrowCompiler, val lhs: Type) : TypeParent()
data class RecursiveTypeParent(val typeCompiler: TypeCompiler) : TypeParent()
data class FunctionCompilerTypeParent(val functionCompiler: FunctionCompiler) : TypeParent()
data class DefineCompilerTypeParent(val defineCompiler: DefineCompiler) : TypeParent()

fun TypeParent.plus(type: Type): Processor =
	when (this) {
		is TypeNameTypeParent -> typeCompiler.plus(name, type)
		is ChoiceNameTypeParent -> ChoiceCompilerProcessor(choiceCompiler.plus(name, type))
		is ArrowDoingTypeParent -> ArrowCompilerProcessor(arrowCompiler.plusDoing(lhs, type))
		is ArrowNameTypeParent -> ArrowCompilerProcessor(arrowCompiler.set(lhs.plus(name compiledLineTo type)))
		is RecursiveTypeParent -> typeCompiler.plusRecursive(type)
		is FunctionCompilerTypeParent -> functionCompiler.plus(type)
		is DefineCompilerTypeParent -> defineCompiler.plus(typeDefinition(type.lineStack.onlyOrNull!!)).processor
	}
