package leo14.untyped.typed

import leo.base.reverseStack
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
		quoteDepth.dec()
			.also { if (it < 0) error("unquote0") }
			.let { copy(quoteDepth = it) }

fun Compiler.compile(script: Script): Compiled =
	plus(script).compiled

fun Compiler.plus(script: Script): Compiler =
	fold(script.lineSeq.reverseStack) { plus(it) }

fun Compiler.plus(line: ScriptLine): Compiler =
	when (line) {
		is LiteralScriptLine -> plus(line.literal)
		is FieldScriptLine -> plus(line.field)
	}

fun Compiler.plus(literal: Literal): Compiler =
	TODO()

fun Compiler.plus(field: ScriptField): Compiler =
	when (field.string) {
		quoteName ->
			clear.incQuoteDepth.compile(field.rhs).let { compiled ->
				if (quoteDepth == 0) append(compiled)
				else append(emptyType.plus(quoteName lineTo compiled.type).compiled(compiled.expression))
			}
		unquoteName ->
			// TODO: This is wrong
			clear.decQuoteDepth.compile(field.rhs).let { compiled ->
				if (quoteDepth == 1) append(compiled)
				else append(emptyType.plus(quoteName lineTo compiled.type).compiled(compiled.expression))
			}
		else -> plusUnquoted(field)
	}

fun Compiler.plusUnquoted(field: ScriptField): Compiler =
	if (field.isSimple) clear.apply(begin(field.string), compiled)
	else plusNormalized(field)

fun Compiler.plusNormalized(field: ScriptField): Compiler =
	when (field.string) {
		else -> apply(begin(field.string), library.compiler(emptyCompiled).compile(field.rhs))
	}

fun Compiler.apply(begin: Begin, rhs: Compiled): Compiler =
	copy(compiled = library.apply(compiled, begin, rhs))

fun Compiler.append(compiled: Compiled): Compiler =
	TODO()
