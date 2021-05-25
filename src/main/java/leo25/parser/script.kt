package leo25.parser

import leo14.*

val scriptParser: Parser<Script>
	get() =
		scriptLineParser.stackParser.map { it.script }

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
			scriptLineParser.map {
				script(it)
			}
		}
