package leo14.typed.compiler

import leo.base.fold
import leo.base.notNullOrError
import leo.base.orIfNull
import leo14.*
import leo14.parser.*
import leo14.syntax.*

data class CharCompiler(
	val tokenReader: TokenReader,
	val tokenParser: SpacedTokenParser) {
	override fun toString() = "$reflectScriptLine"
}

val TokenReader.charCompiler
	get() =
		CharCompiler(this, newSpacedTokenParser)

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
					if (parsedTokenParser.canContinue) CharCompiler(tokenReader, parsedTokenParser)
					else CharCompiler(tokenReader.read(token), newSpacedTokenParser)
				}
				?: CharCompiler(tokenReader, parsedTokenParser)
		}
		.orIfNull {
			tokenParser
				.tokenOrNull
				?.let { token -> CharCompiler(tokenReader.read(token), newSpacedTokenParser).put(char) }
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

