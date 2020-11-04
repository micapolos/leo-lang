package leo21.compiler

import leo.base.fold
import leo.base.notNullOrError
import leo.base.reverse
import leo14.FieldScriptLine
import leo14.Literal
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lineSeq
import leo14.lineTo
import leo14.onlyStringOrNull
import leo14.script
import leo21.typed.LineTyped
import leo21.typed.Typed
import leo21.typed.lineTo
import leo21.typed.lineTyped
import leo21.typed.make
import leo21.typed.plus
import leo21.typed.resolveGetOrNull
import leo21.typed.typed

data class Compiler(
	val bindings: Bindings,
	val typed: Typed
) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "compiled" lineTo script(bindings.reflectScriptLine, typed.reflectScriptLine)
}

fun Bindings.typed(script: Script): Typed =
	Compiler(this, typed()).plus(script).typed

fun Compiler.plus(script: Script): Compiler =
	fold(script.lineSeq.reverse) { plus(it) }

fun Compiler.plus(scriptLine: ScriptLine): Compiler =
	when (scriptLine) {
		is LiteralScriptLine -> plus(scriptLine.literal)
		is FieldScriptLine -> plus(scriptLine.field)
	}

fun Compiler.plus(literal: Literal): Compiler =
	plus(lineTyped(literal)).resolve

fun Compiler.plus(scriptField: ScriptField): Compiler =
	null
		?: plusKeywordOrNull(scriptField)
		?: plusNonKeyword(scriptField)

fun Compiler.plusKeywordOrNull(scriptField: ScriptField): Compiler? =
	when (scriptField.string) {
		"do" -> plusDo(scriptField.rhs)
		"function" -> plusFunction(scriptField.rhs)
		"make" -> plusMake(scriptField.rhs)
		else -> null
	}

fun Compiler.plusDo(script: Script): Compiler =
	bindings.push(typed.type).typed(script).let { typed ->
		setBody(
			Typed(
				fn(typed.term).invoke(this.typed.term),
				typed.type))
	}

fun Compiler.plusFunction(script: Script): Compiler =
	setBody(typed.plus(lineTyped(bindings.arrowTyped(script))))

fun Compiler.plusMake(script: Script): Compiler =
	setBody(typed.make(script.onlyStringOrNull.notNullOrError("make syntax error")))

fun Compiler.plusNonKeyword(scriptField: ScriptField): Compiler =
	resolvePlus(scriptField).resolve

fun Compiler.resolvePlus(scriptField: ScriptField): Compiler =
	plus(scriptField.string lineTo bindings.typed(scriptField.rhs))

val Compiler.resolve
	get() =
		setBody(typed.resolveGetOrNull ?: bindings.resolve(typed))

fun Compiler.plus(typed: LineTyped): Compiler =
	setBody(bindings.resolve(this.typed.plus(typed)))

fun Compiler.setBody(body: Typed) = copy(typed = body)
