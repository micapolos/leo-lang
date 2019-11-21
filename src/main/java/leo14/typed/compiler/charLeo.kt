package leo14.typed.compiler

import leo.base.fold
import leo.base.notNullOrError
import leo.base.orIfNull
import leo14.*
import leo14.native.Native
import leo14.parser.*
import leo14.syntax.*

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
			map(Token::coreString).map(Syntax::token).process(leo)
		} + tokenParser.spacedString

val CharLeo.coreString: String
	get() =
		processorString {
			map(Token::coreString).map(Syntax::token).process(leo)
		} + tokenParser.coreString

val CharLeo.coreColorString: String
	get() =
		processorString {
			map(Syntax::coreColorString).process(leo)
		} + tokenParser.coreString

val CharLeo.indentColorString: String
	get() =
		emptyWriter
			.foldProcessor<Writer, Syntax>({ write(it).notNullOrError("$this.write($it)") }) {
				process(leo)
			}.indentString + tokenParser.coreString

val String.leoEval get() = emptyCharLeo.put(this).coreString