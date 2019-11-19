package leo14.typed.compiler

import leo.base.fold
import leo.base.orIfNull
import leo14.*
import leo14.native.Native
import leo14.parser.*

data class CharLeo(
	val tokenParser: SpacedTokenParser,
	val leo: Leo<Native>)

val emptyCharLeo = CharLeo(newSpacedTokenParser, emptyLeo)

fun CharLeo.put(char: Char): CharLeo =
	tokenParser
		.parse(char)
		?.let { parsedTokenParser ->
			parsedTokenParser
				.tokenOrNull
				?.let { token ->
					if (parsedTokenParser.canContinue) CharLeo(parsedTokenParser, leo)
					else CharLeo(newSpacedTokenParser, leo.parse(token))
				}
				?: CharLeo(parsedTokenParser, leo)
		}
		.orIfNull {
			tokenParser
				.tokenOrNull
				?.let { token -> CharLeo(newSpacedTokenParser, leo.parse(token)).put(char) }
				?: error("$this.put($char)")
		}

fun CharLeo.put(string: String) =
	fold(string) { put(it) }

val CharLeo.spacedString: String
	get() =
		processorString {
			map<String, Token> { it.spacedString }.process(leo)
		} + tokenParser.spacedString

val CharLeo.coreString: String
	get() =
		processorString {
			map<String, Token> { it.coreString }.process(leo)
		} + tokenParser.coreString

val String.leoEval get() = emptyCharLeo.put(this).coreString