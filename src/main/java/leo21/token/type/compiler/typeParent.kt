package leo21.token.type.compiler

import leo13.onlyOrNull
import leo14.ScriptLine
import leo14.Scriptable
import leo14.anyReflectScriptLine
import leo14.begin
import leo14.lineTo
import leo14.reflectScriptLine
import leo14.script
import leo14.scriptLine
import leo14.untyped.typed.script
import leo21.definition.typeDefinition
import leo21.token.define.DefineCompiler
import leo21.token.body.FunctionCompiler
import leo21.token.body.RepeatCompiler
import leo21.token.body.plus
import leo21.token.body.plusDoing
import leo21.token.body.plusTo
import leo21.token.body.set
import leo21.token.define.plus
import leo21.token.processor.ArrowCompilerProcessor
import leo21.token.processor.ChoiceCompilerProcessor
import leo21.token.processor.Processor
import leo21.token.processor.processor
import leo21.type.Type
import leo21.type.plus

sealed class TypeParent : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "parent" lineTo script(anyReflectScriptLine)
}

data class TypeNameTypeParent(val typeCompiler: TypeCompiler, val name: String) : TypeParent()
data class ChoiceNameTypeParent(val choiceCompiler: ChoiceCompiler, val name: String) : TypeParent()
data class ArrowNameTypeParent(val arrowCompiler: ArrowCompiler, val lhs: Type, val name: String) : TypeParent()
data class ArrowDoingTypeParent(val arrowCompiler: ArrowCompiler, val lhs: Type) : TypeParent()
data class RecursiveTypeParent(val typeCompiler: TypeCompiler) : TypeParent()
data class FunctionCompilerTypeParent(val functionCompiler: FunctionCompiler) : TypeParent()
data class FunctionToCompilerTypeParent(val functionCompiler: FunctionCompiler) : TypeParent()
data class DefineCompilerTypeParent(val defineCompiler: DefineCompiler) : TypeParent()
data class RepeatDoingCompilerTypeParent(val repeatCompiler: RepeatCompiler) : TypeParent()

fun TypeParent.plus(type: Type): Processor =
	when (this) {
		is TypeNameTypeParent -> typeCompiler.plus(name, type)
		is ChoiceNameTypeParent -> ChoiceCompilerProcessor(choiceCompiler.plus(name, type))
		is ArrowDoingTypeParent -> ArrowCompilerProcessor(arrowCompiler.plusDoing(lhs, type))
		is ArrowNameTypeParent -> ArrowCompilerProcessor(arrowCompiler.set(lhs.plus(name compiledLineTo type)))
		is RecursiveTypeParent -> typeCompiler.plusRecursive(type)
		is FunctionCompilerTypeParent -> functionCompiler.plus(type)
		is FunctionToCompilerTypeParent -> functionCompiler.plusTo(type).processor
		is DefineCompilerTypeParent -> defineCompiler.plus(typeDefinition(type.lineStack.onlyOrNull!!)).processor
		is RepeatDoingCompilerTypeParent -> repeatCompiler.plusDoing(type)
	}
