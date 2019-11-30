package leo14.typed.compiler

import leo.base.fold
import leo.base.notNullOrError
import leo.base.orIfNull
import leo14.*
import leo14.parser.*
import leo14.syntax.*

data class CharCompiler(
	val tokenParser: SpacedTokenParser,
	val tokenReader: TokenReader) {
	override fun toString() = "$reflectScriptLine"
}

fun CharCompiler.put(char: Char): CharCompiler =
	if (char == '\n')
		if (tokenParser is NewSpacedTokenParser || tokenParser.parse(' ')?.tokenOrNull != null) putRaw(' ')
		else putRaw(' ').putRaw(' ')
	else putRaw(char)

fun CharCompiler.putRaw(char: Char): CharCompiler =
	tokenParser
		.parse(char)
		?.let { parsedTokenParser ->
			parsedTokenParser
				.tokenOrNull
				?.let { token ->
					if (parsedTokenParser.canContinue) CharCompiler(parsedTokenParser, tokenReader)
					else CharCompiler(newSpacedTokenParser, tokenReader.read(token))
				}
				?: CharCompiler(parsedTokenParser, tokenReader)
		}
		.orIfNull {
			tokenParser
				.tokenOrNull
				?.let { token -> CharCompiler(newSpacedTokenParser, tokenReader.read(token)).put(char) }
				?: error("$this.put($char)")
		}

fun CharCompiler.put(string: String) =
	fold(string) { put(it) }

fun CharCompiler.put(script: Script) =
	copy(tokenReader = tokenReader.read(script))

val CharCompiler.spacedString: String
	get() =
		processorString {
			map(Token::coreString).map(Syntax::token).process(tokenReader)
		} + tokenParser.spacedString

val CharCompiler.coreString: String
	get() =
		processorString {
			map(Token::coreString).map(Syntax::token).process(tokenReader)
		} + tokenParser.coreString

val CharCompiler.coreColorString: String
	get() =
		processorString {
			map(Syntax::coreColorString).process(tokenReader)
		} + tokenParser.coreString

val CharCompiler.indentColorString: String
	get() =
		emptyFragment
			.foldProcessor<Fragment, Syntax>({ plus(it.token).notNullOrError("$this.write($it)") }) {
				process(tokenReader)
			}.indentString + tokenParser.coreString

