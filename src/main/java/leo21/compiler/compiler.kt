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
import leo21.compiled.Compiled
import leo21.compiled.LineCompiled
import leo21.compiled.compiled
import leo21.compiled.lineCompiled
import leo21.compiled.lineTo
import leo21.compiled.make
import leo21.compiled.plus
import leo21.compiled.resolveGetOrNull

data class Compiler(
	val bindings: Bindings,
	val compiled: Compiled
) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "compiled" lineTo script(bindings.reflectScriptLine, compiled.reflectScriptLine)
}

fun Bindings.compiled(script: Script): Compiled =
	Compiler(this, compiled()).plus(script).compiled

fun Compiler.plus(script: Script): Compiler =
	fold(script.lineSeq.reverse) { plus(it) }

fun Compiler.plus(scriptLine: ScriptLine): Compiler =
	when (scriptLine) {
		is LiteralScriptLine -> plus(scriptLine.literal)
		is FieldScriptLine -> plus(scriptLine.field)
	}

fun Compiler.plus(literal: Literal): Compiler =
	plus(lineCompiled(literal)).resolve

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
	bindings.push(compiled.type).compiled(script).let { typed ->
		setBody(
			Compiled(
				fn(typed.term).invoke(this.compiled.term),
				typed.type))
	}

fun Compiler.plusDo(rhs: Compiled): Compiler =
	setBody(
		Compiled(
			fn(rhs.term).invoke(compiled.term),
			rhs.type))

fun Compiler.plusFunction(script: Script): Compiler =
	setBody(compiled.plus(lineCompiled(bindings.arrowTyped(script))))

fun Compiler.plusMake(script: Script): Compiler =
	setBody(compiled.make(script.onlyStringOrNull.notNullOrError("make syntax error")))

fun Compiler.plusNonKeyword(scriptField: ScriptField): Compiler =
	resolvePlus(scriptField).resolve

fun Compiler.resolvePlus(scriptField: ScriptField): Compiler =
	plus(scriptField.string lineTo bindings.compiled(scriptField.rhs))

val Compiler.resolve
	get() =
		setBody(compiled.resolveGetOrNull ?: bindings.resolve(compiled))

fun Compiler.plus(rhs: LineCompiled): Compiler =
	setBody(bindings.resolve(compiled.plus(rhs)))

fun Compiler.plusData(rhs: LineCompiled): Compiler =
	setBody(compiled.plus(rhs))

fun Compiler.setBody(body: Compiled) = copy(compiled = body)

