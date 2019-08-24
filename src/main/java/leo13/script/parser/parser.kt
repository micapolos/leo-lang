package leo13.script.parser

import leo.base.notNullIf
import leo13.*

data class Parser(
	val parentOrNull: ParserParent?,
	val script: Script) {
	override fun toString() = asScript.toString()
	val asScript: Script
		get() = script(
			"parent" lineTo (parentOrNull?.asScript ?: nullScript),
			"script" lineTo script.asScript)
}

data class ParserParent(
	val parser: Parser,
	val opening: Opening) {
	override fun toString() = asScript.toString()
	val asScript: Script
		get() = script(
			"parser" lineTo parser.asScript,
			"opening" lineTo opening.asScript)
}

fun parser(): Parser = Parser(null, script())
val Parser.completedScript: Script? get() = notNullIf(parentOrNull == null) { script }

fun Parser.push(token: Token): Parser? =
	when (token) {
		is OpeningToken -> push(token.opening)
		is ClosingToken -> push(token.end)
	}

fun Parser.push(opening: Opening): Parser =
	Parser(ParserParent(this, opening), script())

fun Parser.push(closing: Closing): Parser? =
	parentOrNull?.let { parent ->
		parent.parser.copy(script = script.plus(parent.opening.name lineTo script))
	}
