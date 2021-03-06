package leo21.token.processor

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.ScriptLine
import leo14.Scriptable
import leo14.Token
import leo14.anyReflectScriptLine
import leo14.lineTo
import leo14.script
import leo14.tokenStack
import leo15.dsl.*
import leo21.compiled.Compiled
import leo21.token.body.Body
import leo21.token.body.BodyCompiler
import leo21.token.body.FunctionCompiler
import leo21.token.body.FunctionDoesCompiler
import leo21.token.body.RepeatCompiler
import leo21.token.body.SwitchCompiler
import leo21.token.body.emptyBodyCompiler
import leo21.token.body.plus
import leo21.token.body.wrapCompiled
import leo21.token.define.DefineCompiler
import leo21.token.define.plus
import leo21.token.evaluator.EvaluatorNode
import leo21.token.evaluator.emptyEvaluatorNode
import leo21.token.evaluator.plus
import leo21.token.script.ScriptCompiler
import leo21.token.script.emptyScriptCompiler
import leo21.token.script.plus
import leo21.token.type.compiler.ArrowCompiler
import leo21.token.type.compiler.ChoiceCompiler
import leo21.token.type.compiler.TypeCompiler
import leo21.token.type.compiler.TypeRecurseCompiler
import leo21.token.type.compiler.emptyTypeCompiler
import leo21.token.type.compiler.plus
import leo21.type.Type

sealed class Processor : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "processor" lineTo script(
			when (this) {
				is TypeCompilerProcessor -> typeCompiler.anyReflectScriptLine
				is TypeRecurseCompilerProcessor -> typeRecurseCompiler.anyReflectScriptLine
				is ChoiceCompilerProcessor -> choiceCompiler.anyReflectScriptLine
				is ArrowCompilerProcessor -> arrowCompiler.anyReflectScriptLine
				is ScriptCompilerProcessor -> scriptCompiler.anyReflectScriptLine
				is BodyCompilerProcessor -> bodyCompiler.anyReflectScriptLine
				is FunctionCompilerProcessor -> functionCompiler.anyReflectScriptLine
				is FunctionItDoesCompilerProcessor -> functionDoesCompiler.anyReflectScriptLine
				is DefineCompilerProcessor -> defineCompiler.anyReflectScriptLine
				is SwitchCompilerProcessor -> switchCompiler.anyReflectScriptLine
				is EvaluatorProcessor -> evaluatorNode.anyReflectScriptLine
				is RepeatCompilerProcessor -> repeatCompiler.anyReflectScriptLine
			}
		)
}

data class TypeCompilerProcessor(val typeCompiler: TypeCompiler) : Processor() {
	override fun toString() = super.toString()
}

data class TypeRecurseCompilerProcessor(val typeRecurseCompiler: TypeRecurseCompiler) : Processor() {
	override fun toString() = super.toString()
}

data class ChoiceCompilerProcessor(val choiceCompiler: ChoiceCompiler) : Processor() {
	override fun toString() = super.toString()
}

data class ArrowCompilerProcessor(val arrowCompiler: ArrowCompiler) : Processor() {
	override fun toString() = super.toString()
}

data class ScriptCompilerProcessor(val scriptCompiler: ScriptCompiler) : Processor() {
	override fun toString() = super.toString()
}

data class BodyCompilerProcessor(val bodyCompiler: BodyCompiler) : Processor() {
	override fun toString() = super.toString()
}

data class FunctionCompilerProcessor(val functionCompiler: FunctionCompiler) : Processor() {
	override fun toString() = super.toString()
}

data class FunctionItDoesCompilerProcessor(val functionDoesCompiler: FunctionDoesCompiler) : Processor() {
	override fun toString() = super.toString()
}

data class DefineCompilerProcessor(val defineCompiler: DefineCompiler) : Processor() {
	override fun toString() = super.toString()
}

data class SwitchCompilerProcessor(val switchCompiler: SwitchCompiler) : Processor() {
	override fun toString() = super.toString()
}

data class EvaluatorProcessor(val evaluatorNode: EvaluatorNode) : Processor() {
	override fun toString() = super.toString()
}

data class RepeatCompilerProcessor(val repeatCompiler: RepeatCompiler) : Processor() {
	override fun toString() = super.toString()
}

val emptyTypeProcessor: Processor =
	TypeCompilerProcessor(emptyTypeCompiler)

val emptyScriptProcessor: Processor =
	ScriptCompilerProcessor(emptyScriptCompiler)

val emptyBodyProcessor: Processor =
	BodyCompilerProcessor(emptyBodyCompiler)

val emptyEvaluatorProcessor: Processor =
	EvaluatorProcessor(emptyEvaluatorNode)

val BodyCompiler.processor: Processor get() = BodyCompilerProcessor(this)
val TypeCompiler.processor: Processor get() = TypeCompilerProcessor(this)
val FunctionCompiler.processor: Processor get() = FunctionCompilerProcessor(this)
val FunctionDoesCompiler.processor: Processor get() = FunctionItDoesCompilerProcessor(this)
val EvaluatorNode.processor: Processor get() = EvaluatorProcessor(this)
val DefineCompiler.processor: Processor get() = DefineCompilerProcessor(this)
val RepeatCompiler.processor: Processor get() = RepeatCompilerProcessor(this)

fun Processor.plus(token: Token): Processor =
	when (this) {
		is TypeCompilerProcessor -> typeCompiler.plus(token)
		is TypeRecurseCompilerProcessor -> typeRecurseCompiler.plus(token)
		is ChoiceCompilerProcessor -> choiceCompiler.plus(token)
		is ArrowCompilerProcessor -> arrowCompiler.plus(token)
		is ScriptCompilerProcessor -> scriptCompiler.plus(token)
		is BodyCompilerProcessor -> bodyCompiler.plus(token)
		is FunctionCompilerProcessor -> functionCompiler.plus(token)
		is FunctionItDoesCompilerProcessor -> functionDoesCompiler.plus(token)
		is DefineCompilerProcessor -> defineCompiler.plus(token)
		is SwitchCompilerProcessor -> switchCompiler.plus(token)
		is EvaluatorProcessor -> evaluatorNode.plus(token)
		is RepeatCompilerProcessor -> repeatCompiler.plus(token)
	}

fun Processor.plus(script: Script): Processor =
	fold(script.tokenStack.reverse) { plus(it) }

fun Processor.plus(f: F): Processor =
	plus(script_(f))

val Processor.type: Type
	get() =
		(this as TypeCompilerProcessor).typeCompiler.type

val Processor.bodyCompiler: BodyCompiler
	get() =
		(this as BodyCompilerProcessor).bodyCompiler

val Processor.body: Body
	get() =
		(this as BodyCompilerProcessor).bodyCompiler.body

val Processor.compiled: Compiled
	get() =
		body.wrapCompiled

val Processor.evaluatorNode: EvaluatorNode
	get() =
		(this as EvaluatorProcessor).evaluatorNode

