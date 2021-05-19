package leo25

import leo.base.fold
import leo.base.reverse
import leo14.*

data class Interpreter(
	val context: Context,
	val valueOrNull: Value?
)

fun Context.interpreter(valueOrNull: Value? = null) =
	Interpreter(this, valueOrNull)

fun Context.interpretedValueOrNull(script: Script): Value? =
	interpreter().fold(script.lineSeq.reverse) { plus(it) }.valueOrNull

val Script.interpret: Script
	get() =
		context().interpretedValueOrNull(this).orNullScript

fun Interpreter.plus(scriptLine: ScriptLine): Interpreter =
	// TODO: Resolve static definitions, function etc...
	when (scriptLine) {
		is FieldScriptLine -> plus(scriptLine.field)
		is LiteralScriptLine -> plus(scriptLine.literal)
	}

fun Interpreter.plus(scriptField: ScriptField): Interpreter =
	null
		?: plusStaticOrNull(scriptField)
		?: plusDynamic(scriptField)

fun Interpreter.plusStaticOrNull(scriptField: ScriptField): Interpreter? =
	when (scriptField.string) {
		"function" -> plus("function" fieldTo value(Function(context, scriptField.rhs)))
		"define" -> context.define(scriptField.rhs).interpreter(valueOrNull)
		else -> null
	}

fun Interpreter.plusDynamic(scriptField: ScriptField): Interpreter =
	context.interpretedValueOrNull(scriptField.rhs).let { valueOrNull ->
		if (valueOrNull != null) plus(scriptField.string fieldTo valueOrNull)
		else plus(word(scriptField.string))
	}

fun Interpreter.plus(literal: Literal): Interpreter =
	plus(literal.field)

val Literal.field: Field
	get() =
		when (this) {
			is NumberLiteral -> Field(word("number"), value(number.toString()))
			is StringLiteral -> Field(word("text"), value(string))
		}

fun Interpreter.plus(field: Field): Interpreter =
	context.interpreter(context.resolve(valueOrNull.plus(field)))

fun Interpreter.plus(word: Word): Interpreter =
	context.interpreter(
		context.resolve(
			if (valueOrNull != null) value(word fieldTo valueOrNull)
			else value(word)
		)
	)
