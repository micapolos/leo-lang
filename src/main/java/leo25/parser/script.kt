package leo25.parser

import leo13.fold
import leo13.reverse
import leo14.*
import leo25.LiteralAtom
import leo25.NameAtom

val scriptParser: Parser<Script>
	get() =
		scriptBlockParser.stackParser.map {
			script().fold(it.reverse) { plus(it) }
		}
//scriptLineParser.stackParser.map { it.script }

val scriptLineParser: Parser<ScriptLine>
	get() =
		firstCharOneOf(
			literalParser.thenUnit(unitParser('\n')).map { line(it) },
			scriptFieldParser.map { line(it) })

val scriptFieldParser: Parser<ScriptField>
	get() =
		nameParser.bind { name ->
			scriptRhsParser.map { rhs ->
				name fieldTo rhs
			}
		}

val scriptRhsParser: Parser<Script>
	get() =
		firstCharOneOf(
			scriptIndentedRhsParser,
			scriptSpacedRhsParser
		)

val scriptIndentedRhsParser: Parser<Script>
	get() =
		unitParser('\n').bind {
			scriptParser.indented
		}

val scriptSpacedRhsParser: Parser<Script>
	get() =
		unitParser(' ').bind {
//			singleLineScriptParser
			scriptBlockParser
		}

val scriptBlockParser: Parser<Script>
	get() =
		atomParser.bind { atom ->
			when (atom) {
				is LiteralAtom ->
					dottedNameStackParser.bind { nameStack ->
						unitParser('\n').map {
							script(line(atom.literal)).fold(nameStack.reverse) { plus(it lineTo script()) }
						}
					}
				is NameAtom ->
					firstCharOneOf(
						scriptRhsParser.map {
							script(atom.name lineTo it)
						},
						dottedNameStackParser.bind { nameStack ->
							unitParser('\n').map {
								script(line(atom.name)).fold(nameStack.reverse) { plus(it lineTo script()) }
							}
						})
			}
		}
