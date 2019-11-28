package leo14.typed.compiler

import leo.base.fold
import leo.base.notNullOrError
import leo.base.orIfNull
import leo14.*
import leo14.parser.*
import leo14.syntax.*

data class CharCompiler<T>(
	val tokenParser: SpacedTokenParser,
	val compiler: Compiler<T>) {
	override fun toString() = "$reflectScriptLine"
}

fun <T> CharCompiler<T>.put(char: Char): CharCompiler<T> =
	if (char == '\n')
		if (tokenParser is NewSpacedTokenParser || tokenParser.parse(' ')?.tokenOrNull != null) putRaw(' ')
		else putRaw(' ').putRaw(' ')
	else putRaw(char)

fun <T> CharCompiler<T>.putRaw(char: Char): CharCompiler<T> =
	tokenParser
		.parse(char)
		?.let { parsedTokenParser ->
			parsedTokenParser
				.tokenOrNull
				?.let { token ->
					if (parsedTokenParser.canContinue) CharCompiler(parsedTokenParser, compiler)
					else CharCompiler(newSpacedTokenParser, compiler.parse(token))
				}
				?: CharCompiler(parsedTokenParser, compiler)
		}
		.orIfNull {
			tokenParser
				.tokenOrNull
				?.let { token -> CharCompiler(newSpacedTokenParser, compiler.parse(token)).put(char) }
				?: error("$this.put($char)")
		}

fun <T> CharCompiler<T>.put(string: String) =
	fold(string) { put(it) }

fun <T> CharCompiler<T>.put(script: Script) =
	copy(compiler = compiler.parse(script))

val <T> CharCompiler<T>.spacedString: String
	get() =
		processorString {
			map(Token::coreString).map(Syntax::token).process(compiler)
		} + tokenParser.spacedString

val <T> CharCompiler<T>.coreString: String
	get() =
		processorString {
			map(Token::coreString).map(Syntax::token).process(compiler)
		} + tokenParser.coreString

val <T> CharCompiler<T>.coreColorString: String
	get() =
		processorString {
			map(Syntax::coreColorString).process(compiler)
		} + tokenParser.coreString

val <T> CharCompiler<T>.indentColorString: String
	get() =
		emptyFragment
			.foldProcessor<Fragment, Syntax>({ plus(it.token).notNullOrError("$this.write($it)") }) {
				process(compiler)
			}.indentString + tokenParser.coreString

