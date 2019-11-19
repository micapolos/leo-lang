package leo14.typed.compiler

import leo.base.fold
import leo.base.orIfNull
import leo14.Token
import leo14.map
import leo14.native.Native
import leo14.parser.*
import leo14.processorString

data class CharLeo(
	val tokenParser: TokenParser,
	val leo: Leo<Native>)

val emptyCharLeo = CharLeo(newTokenParser, emptyLeo)

fun CharLeo.put(char: Char): CharLeo =
	tokenParser
		.parse(char)
		?.let { parsedTokenParser ->
			parsedTokenParser
				.tokenOrNull
				?.let { token ->
					if (newTokenParser.canContinue) CharLeo(parsedTokenParser, leo)
					else CharLeo(newTokenParser, leo.parse(token))
				}
				?: CharLeo(parsedTokenParser, leo)
		}
		.orIfNull {
			tokenParser
				.tokenOrNull
				?.let { token -> CharLeo(newTokenParser, leo.parse(token)).put(char) }
				?: error("$this.put($char)")
		}

fun CharLeo.put(string: String) =
	fold(string) { put(it) }

val CharLeo.string: String
	get() =
		processorString {
			map<String, Token> { it.toString() }.process(leo)
		} + tokenParser.coreString

val String.leoEval get() = emptyCharLeo.put(this).string