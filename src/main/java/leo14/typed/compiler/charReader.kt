package leo14.typed.compiler

import leo.base.fold
import leo.base.notNullOrError
import leo.base.orIfNull
import leo14.*
import leo14.parser.*
import leo14.syntax.*

data class CharReader(
	val tokenReader: TokenReader,
	val tokenParser: SpacedTokenParser) {
	override fun toString() = "$reflectScriptLine"
}

val TokenReader.charReader
	get() =
		CharReader(this, newSpacedTokenParser)

fun CharReader.put(char: Char): CharReader =
	if (char == '\n')
		if (tokenParser is NewSpacedTokenParser || tokenParser.parse(' ')?.tokenOrNull != null) putRaw(' ')
		else putRaw(' ').putRaw(' ')
	else putRaw(char)

fun CharReader.putRaw(char: Char): CharReader =
	tokenParser
		.parse(char)
		?.let { parsedTokenParser ->
			parsedTokenParser
				.tokenOrNull
				?.let { token ->
					if (parsedTokenParser.canContinue) CharReader(tokenReader, parsedTokenParser)
					else CharReader(tokenReader.read(token), newSpacedTokenParser)
				}
				?: CharReader(tokenReader, parsedTokenParser)
		}
		.orIfNull {
			tokenParser
				.tokenOrNull
				?.let { token -> CharReader(tokenReader.read(token), newSpacedTokenParser).put(char) }
				?: error("$this.put($char)")
		}

fun CharReader.put(string: String) =
	fold(string) { put(it) }

fun CharReader.put(script: Script) =
	copy(tokenReader = tokenReader.read(script))

val CharReader.spacedString: String
	get() =
		processorString {
			map(Token::coreString).map(Syntax::token).process(tokenReader)
		} + tokenParser.spacedString

val CharReader.coreString: String
	get() =
		processorString {
			map(Token::coreString).map(Syntax::token).process(tokenReader)
		} + tokenParser.coreString

val CharReader.coreColorString: String
	get() =
		processorString {
			map(Syntax::coreColorString).process(tokenReader)
		} + tokenParser.coreString

val CharReader.indentColorString: String
	get() =
		emptyFragment
			.foldProcessor<Fragment, Syntax>({ plus(it.token).notNullOrError("$this.write($it)") }) {
				process(tokenReader)
			}.indentString + tokenParser.coreString

