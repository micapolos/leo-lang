package leo19.compiler

import leo.base.fold
import leo.base.notNullOrError
import leo.base.reverse
import leo14.FieldScriptLine
import leo14.Literal
import leo14.LiteralScriptLine
import leo14.NumberLiteral
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.StringLiteral
import leo14.isEmpty
import leo14.lineSeq
import leo19.term.function
import leo19.term.invoke
import leo19.term.term
import leo19.type.Arrow
import leo19.type.choiceOrNull
import leo19.type.contentOrNull
import leo19.type.fieldTo
import leo19.type.struct
import leo19.typed.Typed
import leo19.typed.TypedField
import leo19.typed.TypedSwitch
import leo19.typed.castTo
import leo19.typed.fieldTo
import leo19.typed.getOrNull
import leo19.typed.invoke
import leo19.typed.make
import leo19.typed.nullTyped
import leo19.typed.of
import leo19.typed.plus
import leo19.typed.typed

data class Compiler(
	val resolver: Resolver,
	val typed: Typed
)

val emptyCompiler = Compiler(emptyResolver, nullTyped)

fun Resolver.compiler(typed: Typed) =
	Compiler(this, typed)

fun Compiler.plus(script: Script): Compiler =
	fold(script.lineSeq.reverse) { plus(it) }

fun Compiler.plus(scriptLine: ScriptLine) =
	when (scriptLine) {
		is LiteralScriptLine -> plus(scriptLine.literal)
		is FieldScriptLine -> plus(scriptLine.field)
	}

fun Compiler.plus(literal: Literal): Compiler =
	when (literal) {
		is StringLiteral -> TODO()
		is NumberLiteral -> TODO()
	}

fun Compiler.plus(scriptField: ScriptField) =
	if (scriptField.string == "give") plusGive(scriptField.rhs)
	else if (scriptField.string == "switch") plusSwitch(scriptField.rhs)
	else if (scriptField.string == "as") plusAs(scriptField.rhs)
	else if (scriptField.rhs.isEmpty) plus(scriptField.string)
	else plus(
		TypedField(
			scriptField.string,
			Compiler(resolver, nullTyped).plus(scriptField.rhs).typed))

fun Compiler.plusGive(script: Script): Compiler =
	resolver
		.plus(
			constantBinding(
				Arrow(
					struct("given" fieldTo struct()),
					struct("given" fieldTo typed.type))))
		.typed(script)
		.let { giveTyped ->
			set(term(function(giveTyped.term)).invoke(typed.term).of(giveTyped.type))
		}

fun Compiler.plusSwitch(script: Script): Compiler =
	plus(
		resolver.switchCompiler(
			typed.type
				.contentOrNull.notNullOrError("not a struct")
				.choiceOrNull.notNullOrError("not a choice")
				.switchBuilder)
			.plus(script)
			.switchBuilder
			.build)

fun Compiler.plus(switch: TypedSwitch): Compiler =
	set(typed.invoke(switch))

fun Compiler.plusAs(script: Script): Compiler =
	set(typed.castTo(script.type))

fun Compiler.plus(typedField: TypedField): Compiler =
	set(typed.plus(typedField)).resolve

fun Compiler.plus(name: String): Compiler =
	null
		?: maybePlusGet(name)
		?: maybeResolve(name)
		?: plusMake(name)

fun Compiler.maybePlusGet(name: String): Compiler? =
	typed.getOrNull(name)?.let { set(it) }

fun Compiler.maybeResolve(name: String): Compiler? =
	set(typed.plus(name fieldTo typed())).maybeResolve

fun Compiler.plusMake(name: String): Compiler =
	set(typed.make(name))

fun Compiler.set(typed: Typed) =
	copy(typed = typed)

val Compiler.resolve: Compiler
	get() =
		maybeResolve ?: this

val Compiler.maybeResolve: Compiler?
	get() =
		resolver.resolveOrNull(typed)
			?.let { set(it) }
