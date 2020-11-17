package leo21.token.processor

import leo13.fold
import leo13.reverse
import leo14.Script
import leo14.Token
import leo14.tokenStack
import leo15.dsl.*
import leo21.compiled.Compiled
import leo21.token.body.Body
import leo21.token.body.BodyCompiler
import leo21.token.body.DefineCompiler
import leo21.token.body.FunctionCompiler
import leo21.token.body.FunctionItCompiler
import leo21.token.body.FunctionItDoesCompiler
import leo21.token.body.SwitchCompiler
import leo21.token.body.emptyBodyCompiler
import leo21.token.body.plus
import leo21.token.evaluator.EvaluatorNode
import leo21.token.evaluator.emptyEvaluatorNode
import leo21.token.evaluator.plus
import leo21.token.script.ScriptCompiler
import leo21.token.script.emptyScriptCompiler
import leo21.token.script.plus
import leo21.token.type.compiler.ArrowCompiler
import leo21.token.type.compiler.ChoiceCompiler
import leo21.token.type.compiler.TypeCompiler
import leo21.token.type.compiler.emptyTypeCompiler
import leo21.token.type.compiler.plus
import leo21.type.Type

sealed class Processor
data class TypeCompilerProcessor(val typeCompiler: TypeCompiler) : Processor()
data class ChoiceCompilerProcessor(val choiceCompiler: ChoiceCompiler) : Processor()
data class ArrowCompilerProcessor(val arrowCompiler: ArrowCompiler) : Processor()
data class ScriptCompilerProcessor(val scriptCompiler: ScriptCompiler) : Processor()
data class BodyCompilerProcessor(val bodyCompiler: BodyCompiler) : Processor()
data class FunctionCompilerProcessor(val functionCompiler: FunctionCompiler) : Processor()
data class FunctionItCompilerProcessor(val functionItCompiler: FunctionItCompiler) : Processor()
data class FunctionItDoesCompilerProcessor(val functionItDoesCompiler: FunctionItDoesCompiler) : Processor()
data class DefineCompilerProcessor(val defineCompiler: DefineCompiler) : Processor()
data class SwitchCompilerProcessor(val switchCompiler: SwitchCompiler) : Processor()
data class EvaluatorProcessor(val evaluatorNode: EvaluatorNode) : Processor()

val emptyTypeProcessor: Processor =
	TypeCompilerProcessor(emptyTypeCompiler)

val emptyScriptProcessor: Processor =
	ScriptCompilerProcessor(emptyScriptCompiler)

val emptyBodyProcessor: Processor =
	BodyCompilerProcessor(emptyBodyCompiler)

val emptyEvaluatorProcessor: Processor =
	EvaluatorProcessor(emptyEvaluatorNode)

val BodyCompiler.processor: Processor get() = BodyCompilerProcessor(this)
val FunctionCompiler.processor: Processor get() = FunctionCompilerProcessor(this)
val FunctionItCompiler.processor: Processor get() = FunctionItCompilerProcessor(this)
val FunctionItDoesCompiler.processor: Processor get() = FunctionItDoesCompilerProcessor(this)
val EvaluatorNode.processor: Processor get() = EvaluatorProcessor(this)

fun Processor.plus(token: Token): Processor =
	when (this) {
		is TypeCompilerProcessor -> typeCompiler.plus(token)
		is ChoiceCompilerProcessor -> choiceCompiler.plus(token)
		is ArrowCompilerProcessor -> arrowCompiler.plus(token)
		is ScriptCompilerProcessor -> scriptCompiler.plus(token)
		is BodyCompilerProcessor -> bodyCompiler.plus(token)
		is FunctionCompilerProcessor -> functionCompiler.plus(token)
		is FunctionItCompilerProcessor -> functionItCompiler.plus(token)
		is FunctionItDoesCompilerProcessor -> functionItDoesCompiler.plus(token)
		is DefineCompilerProcessor -> defineCompiler.plus(token)
		is SwitchCompilerProcessor -> switchCompiler.plus(token)
		is EvaluatorProcessor -> evaluatorNode.plus(token)
	}

fun Processor.plus(script: Script): Processor =
	fold(script.tokenStack.reverse) { plus(it) }

fun Processor.plus(f: F): Processor =
	plus(script_(f))

val Processor.type: Type
	get() =
		(this as TypeCompilerProcessor).typeCompiler.type

val Processor.body: Body
	get() =
		(this as BodyCompilerProcessor).bodyCompiler.body

val Processor.compiled: Compiled
	get() =
		body.compiled
