package leo21.compiled

import leo.base.notNullIf
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.invoke
import leo14.lineTo
import leo14.orError
import leo14.script
import leo21.prim.Prim
import leo21.type.Arrow
import leo21.type.script

data class ArrowCompiled(
	val term: Term<Prim>,
	val arrow: Arrow
) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "compiled" lineTo script(term.reflectScriptLine, arrow.reflectScriptLine)
}

infix fun Term<Prim>.of(arrow: Arrow) = ArrowCompiled(this, arrow)

fun ArrowCompiled.invokeOrNull(compiled: Compiled): Compiled? =
	if (arrow.lhs != compiled.type) null
	else Compiled(term.invoke(compiled.term), arrow.rhs)

fun ArrowCompiled.invoke(compiled: Compiled): Compiled =
	invokeOrNull(compiled).orError(
		"apply" lineTo script(
			"expected" lineTo arrow.lhs.script,
			"was" lineTo compiled.type.script))

fun ArrowCompiled.resolveOrNull(index: Int, param: Compiled) =
	notNullIf(arrow.lhs == param.type) {
		arg<Prim>(index).invoke(param.term) of arrow.rhs
	}
