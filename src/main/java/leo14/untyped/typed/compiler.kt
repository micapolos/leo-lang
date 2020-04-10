package leo14.untyped.typed

import leo.base.reverseStack
import leo.base.runIf
import leo13.fold
import leo13.quoteName
import leo13.unquoteName
import leo14.*

data class Compiler(val library: Library, val quoteDepth: Int, val compiled: Compiled)

fun Library.compiler(compiled: Compiled) = Compiler(this, 0, compiled)
val emptyCompiler = emptyLibrary.compiler(emptyCompiled)

val Compiler.clear: Compiler
	get() =
		library.compiler(emptyCompiled)

val Compiler.incQuoteDepth
	get() =
		copy(quoteDepth = quoteDepth.inc())

val Compiler.decQuoteDepth
	get() =
		copy(quoteDepth = quoteDepth.dec())

val Compiler.evaluate
	get() =
		copy(compiled = compiled.evaluate)

val Compiler.evaluatedScript
	get() =
		library.scope.script(compiled.evaluate.typed)

fun Compiler.compile(script: Script): Compiled =
	plus(script).compiled

fun Compiler.plus(script: Script): Compiler =
	fold(script.lineSeq.reverseStack) { plus(it) }

fun Compiler.plus(line: ScriptLine): Compiler =
	when (line) {
		is LiteralScriptLine -> plus(line.literal)
		is FieldScriptLine -> plus(line.field)
	}

fun Compiler.plus(field: ScriptField): Compiler =
	if (field.isSimple) clear.plus(begin(field.string), compiled)
	else plusNormalized(field)

fun Compiler.plusNormalized(field: ScriptField): Compiler =
	when (field.string) {
		quoteName ->
			if (quoteDepth == 0) incQuoteDepth.plus(field.rhs).decQuoteDepth
			else plus(begin(field.string), clear.incQuoteDepth.compile(field.rhs))
		unquoteName ->
			if (quoteDepth == 1) decQuoteDepth.plus(field.rhs).incQuoteDepth
			else plus(begin(field.string), clear.decQuoteDepth.compile(field.rhs))
		else ->
			plus(begin(field.string), clear.compile(field.rhs))
	}

fun Compiler.plus(literal: Literal): Compiler =
	copy(compiled = compiled.append(literal)).compile

fun Compiler.plus(begin: Begin, rhs: Compiler): Compiler =
	// TODO: Support importing library
	plus(begin, rhs.compiled)

fun Compiler.plus(begin: Begin, rhs: Compiled): Compiler =
	copy(compiled = compiled.append(begin, rhs)).compile

val Compiler.compile: Compiler
	get() =
		runIf(quoteDepth <= 0) { apply }

val Compiler.apply: Compiler
	get() =
		null
			?: applyLibrary
			?: applyScope
			?: applyStatic

val Compiler.applyLibrary: Compiler?
	get() =
		library.applyDefinition(compiled)?.let { copy(library = it, compiled = emptyCompiled) }

val Compiler.applyScope: Compiler?
	get() =
		library.applyCompiled(compiled)?.let { copy(compiled = it) }

val Compiler.applyStatic: Compiler
	get() =
		copy(compiled = compiled.apply)

fun Compiler.set(compiled: Compiled): Compiler =
	copy(compiled = compiled)

fun Compiler.plus(definition: Definition): Compiler =
	copy(library = library.plus(definition))

val Compiler.script: Script
	get() =
		when (compiled.expression) {
			is ConstantExpression -> library.scope.script(compiled.typed)
			is DynamicExpression -> compiled.type.script
		}
