package leo21.token.body

import leo.base.runIf
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.Term
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.value.Value
import leo14.lineTo
import leo14.plus
import leo14.script
import leo21.compiled.Compiled
import leo21.compiled.of
import leo21.prim.Prim
import leo21.prim.runtime.value
import leo21.type.Type
import leo21.type.script

data class Definition(val type: Type, val compiled: Compiled, val isFunction: Boolean) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "definition" lineTo script(
			(if (isFunction) "function" else "constant") lineTo script(
				type.reflectScriptLine,
				compiled.reflectScriptLine))
}


fun constantDefinition(type: Type, compiled: Compiled) = Definition(type, compiled, isFunction = false)
fun functionDefinition(type: Type, compiled: Compiled) = Definition(type, compiled, isFunction = true)

fun Compiled.wrap(definition: Definition): Compiled =
	fn(term).invoke(definition.term).of(type)

val Definition.term: Term<Prim>
	get() =
		compiled.term.runIf(isFunction) { fn(this) }

val Definition.binding: Binding
	get() =
		if (isFunction) type.functionBinding(compiled.type)
		else type.constantBinding(compiled.type)

val Definition.printScriptLine: ScriptLine
	get() =
		(if (isFunction) "function" else "constant") lineTo
			type.script.plus(
				(if (isFunction) "does" else "is") lineTo compiled.type.script)

val Definition.value: Value<Prim>
	get() =
		compiled.term.value
