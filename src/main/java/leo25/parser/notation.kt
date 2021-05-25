package leo25.parser

import leo13.Stack
import leo13.array
import leo13.fold
import leo13.reverse
import leo25.*

val atomParser: Parser<Atom>
	get() =
		firstCharOneOf(
			literalParser.map { atom(it) },
			nameParser.map { atom(it) })

val chainParser: Parser<Chain>
	get() =
		atomParser.bind { atom ->
			nameStackParser
			unitParser('.')
				.bind { nameParser }
				.stackParser.bind { nameStack ->
					chain(atom).fold(nameStack.reverse) { plus(it) }.parser()
				}
		}

val nameStackParser: Parser<Stack<String>>
	get() =
		unitParser('.')
			.bind { nameParser }
			.stackParser

val notationParser: Parser<Notation>
	get() =
		notationLineParser.stackParser.map { notation(*it.array) }

val notationLinkParser: Parser<NotationLink>
	get() =
		notationLineParser.stackLinkParser.map {
			it.reverse.let {
				notation(*it.stack.array) linkTo it.value
			}
		}

val notationLineParser: Parser<NotationLine>
	get() =
		atomParser.bind { atom ->
			firstCharOneOf(
				notationFieldParser.map { line(it) },
				nameStackParser.map { nameStack -> line(chain(atom).fold(nameStack.reverse) { plus(it) }) })
		}

val notationFieldParser: Parser<NotationField>
	get() =
		nameParser.bind { name ->
			notationRhsParser.map { rhs ->
				name fieldTo rhs
			}
		}

val notationRhsParser: Parser<NotationLink>
	get() =
		firstCharOneOf(
			notationLinkIndentedRhsParser,
			notationLinkSpacedRhsParser
		)

val notationLinkIndentedRhsParser: Parser<NotationLink>
	get() =
		unitParser('\n').bind {
			notationLinkParser.indented
		}

val notationLinkSpacedRhsParser: Parser<NotationLink>
	get() =
		unitParser(' ').bind {
			notationLineParser.map {
				emptyNotation linkTo it
			}
		}
