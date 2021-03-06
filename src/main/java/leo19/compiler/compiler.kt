package leo19.compiler

import leo.base.fold
import leo.base.notNullOrError
import leo.base.reverse
import leo.base.runIf
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
import leo14.lineTo
import leo14.rhsOrNull
import leo14.script
import leo19.term.function
import leo19.term.invoke
import leo19.term.term
import leo19.type.arrowTo
import leo19.type.choiceOrNull
import leo19.type.contentOrNull
import leo19.type.type
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
import leo19.typed.reflectScript
import leo19.typed.typed
import leo19.typed.typedEquals

data class Compiler(
	val context: Context,
	val typed: Typed
) {
	override fun toString() = reflect.toString()
}

val Compiler.reflect: ScriptLine
	get() =
		"compiler" lineTo script(context.reflect, "typed" lineTo typed.reflectScript)

val emptyCompiler = Compiler(emptyContext, nullTyped)

fun Context.compiler(typed: Typed) =
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
	if (scriptField.string == doKeyword) plusDo(scriptField.rhs)
	else if (scriptField.string == choiceKeyword) plusChoice(scriptField.rhs)
	else if (scriptField.string == switchKeyword) plusSwitch(scriptField.rhs)
	else if (scriptField.string == defineKeyword) plusDefine(scriptField.rhs)
	else if (scriptField.string == asKeyword) plusAs(scriptField.rhs)
	else if (scriptField.string == testKeyword) plusTest(scriptField.rhs)
	else if (scriptField.rhs.isEmpty) plus(scriptField.string)
	else plus(
		TypedField(
			scriptField.string,
			context.resolver.typed(scriptField.rhs)))

fun Compiler.plusDo(script: Script): Compiler =
	script
		.rhsOrNull(recursivelyKeyword)
		?.let { plusDo(it, isRecursive = true) }
		?: plusDo(script, isRecursive = false)

fun Compiler.plusDo(script: Script, isRecursive: Boolean): Compiler =
	context.resolver
		.runIf(isRecursive) {
			plus(functionBinding(typed.type arrowTo type("dupa")))
		} // TODO: resolve rhs type
		.plus(binding(typed.type))
		.typed(script)
		.let { giveTyped ->
			set(term(function(giveTyped.term, isRecursive)).invoke(typed.term).of(giveTyped.type))
		}

fun Compiler.plusChoice(script: Script): Compiler =
	set(context.resolver.choice(script).typed)

fun Compiler.plusSwitch(script: Script): Compiler =
	plus(
		context.resolver.switchCompiler(
			typed.type
				.contentOrNull.notNullOrError("not a struct")
				.choiceOrNull.notNullOrError("not a choice")
				.switchBuilder)
			.plus(script)
			.switchBuilder
			.build)

fun Compiler.plus(switch: TypedSwitch): Compiler =
	set(typed.invoke(switch))

fun Compiler.plusDefine(script: Script): Compiler =
	copy(context = DefineCompiler(context, type()).plus(script).compiledContext)

fun Compiler.plusTest(script: Script): Compiler =
	also { context.resolver.testCompiler.plus(script).end }

fun Compiler.plusAs(script: Script): Compiler =
	set(typed.castTo(script.type))

fun Compiler.plus(typedField: TypedField): Compiler =
	when (typedField.name) {
		equalsKeyword -> plusEquals(typedField.typed)
		else -> set(typed.plus(typedField)).resolve
	}

fun Compiler.plusEquals(rhs: Typed): Compiler =
	set(typed.typedEquals(rhs))

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
		context.resolver.resolveOrNull(typed)
			?.let { set(it) }

val Compiler.compiledTyped
	get() =
		context.scope.wrap(typed.term).of(typed.type)