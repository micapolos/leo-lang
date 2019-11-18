package leo14.typed.compiler

import leo.base.fold
import leo14.Token
import leo14.map
import leo14.native.Native
import leo14.parser.*
import leo14.processorString

data class CharLeo(
	val tokenParser: TokenParser,
	val leo: Leo<Native>)

val emptyCharLeo = CharLeo(tokenParser, emptyLeo)

fun CharLeo.put(char: Char): CharLeo =
	tokenParser
		.parse(char)
		?.let { newTokenParser ->
			newTokenParser
				.tokenOrNull
				?.let { token -> CharLeo(leo14.parser.tokenParser, leo.parse(token)) }
				?: CharLeo(newTokenParser, leo)
		}
		?: error("$this.put($char)")

fun CharLeo.put(string: String) =
	fold(string) { put(it) }

val CharLeo.string: String
	get() =
		processorString {
			map<String, Token> { it.toString() }.process(leo)
		} + tokenParser.coreString

val String.leoEval get() = emptyCharLeo.put(this).string