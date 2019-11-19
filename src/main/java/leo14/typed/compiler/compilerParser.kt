package leo14.typed.compiler

import leo.base.fold
import leo.base.notNullIf
import leo.base.notNullOrError
import leo14.native.Native
import leo14.parser.*
import leo14.typed.Typed

data class CompilerParser<T>(
	val compiler: Compiler<T>,
	val accTokenParser: TokenParser)

val nativeCompilerParser = CompilerParser(nativeCompiler, newTokenParser)

fun <T> CompilerParser<T>.parse(char: Char): CompilerParser<T> =
	accTokenParser
		.parse(char)
		.notNullOrError("$this.parse($char)")
		.let { newTokenParser ->
			newTokenParser
				.tokenOrNull
				?.let { token -> CompilerParser(compiler.compile(token), leo14.parser.newTokenParser) }
				?: CompilerParser(compiler, newTokenParser)
		}

fun <T> CompilerParser<T>.parseAllowingWhitespaces(char: Char): CompilerParser<T> =
	if (accTokenParser.isEmpty && char.isWhitespace()) this
	else parse(char)

val <T> CompilerParser<T>.compilerOrNull: Compiler<T>?
	get() =
		notNullIf(accTokenParser is BeginTokenParser) { compiler }

fun <T> CompilerParser<T>.compile(string: String): CompilerParser<T> =
	fold(string) { parseAllowingWhitespaces(it) }

fun compile(string: String): Typed<Native> =
	nativeCompilerParser.compile(string).compilerOrNull!!.typed
