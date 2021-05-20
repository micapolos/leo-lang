package leo25

import leo.base.assertEqualTo
import leo13.charString
import leo13.stack
import leo14.literal
import leo14.number
import kotlin.test.Test

class ParserTest {
	@Test
	fun stringParser() {
		parser("abc").parsed("abc").assertEqualTo("abc")
		parser("abc").parsed("ab").assertEqualTo(null)
		parser("abc").parsed("abcd").assertEqualTo(null)
	}

	@Test
	fun stackParser() {
		stackParser(letterCharParser).parsed("").assertEqualTo(stack())
		stackParser(letterCharParser).parsed("abc").assertEqualTo(stack('a', 'b', 'c'))
	}

	@Test
	fun name() {
		nameParser.parsed("a").assertEqualTo("a")
		nameParser.parsed("foo").assertEqualTo("foo")

		nameParser.parsed("").assertEqualTo(null)
		nameParser.parsed("1").assertEqualTo(null)
		nameParser.parsed("foo1").assertEqualTo(null)
		nameParser.parsed("foo bar").assertEqualTo(null)
	}

	@Test
	fun number() {
		numberParser.parsed("0").assertEqualTo(number(0))
		numberParser.parsed("-0").assertEqualTo(number(0))
		numberParser.parsed("1").assertEqualTo(number(1))
		numberParser.parsed("12").assertEqualTo(number(12))
		numberParser.parsed("-1").assertEqualTo(number(-1))
		numberParser.parsed("-12").assertEqualTo(number(-12))

		numberParser.parsed("").assertEqualTo(null)
		numberParser.parsed("-").assertEqualTo(null)
		numberParser.parsed("+").assertEqualTo(null)
		numberParser.parsed("+1").assertEqualTo(null)
		numberParser.parsed("1.").assertEqualTo(null)
		numberParser.parsed("1.0").assertEqualTo(null)
		numberParser.parsed("1a").assertEqualTo(null)
	}

	@Test
	fun literal() {
		literalParser.parsed("\"foo\"").assertEqualTo(literal("foo"))
		literalParser.parsed("123").assertEqualTo(literal(123))

		literalParser.parsed("").assertEqualTo(null)
		literalParser.parsed("abc").assertEqualTo(null)
	}

	@Test
	fun map() {
		val parser = stackParser(letterCharParser).bind { letterCharStack ->
			stackParser(digitCharParser).map { digitCharStack ->
				letterCharStack.charString + digitCharStack.charString
			}
		}

		parser.parsed("").assertEqualTo("")
		parser.parsed("a").assertEqualTo("a")
		parser.parsed("ab").assertEqualTo("ab")
		parser.parsed("1").assertEqualTo("1")
		parser.parsed("12").assertEqualTo("12")
		parser.parsed("a1").assertEqualTo("a1")
		parser.parsed("ab12").assertEqualTo("ab12")

		parser.parsed("+").assertEqualTo(null)
		parser.parsed("a+").assertEqualTo(null)
		parser.parsed("1+").assertEqualTo(null)
		parser.parsed("a1+").assertEqualTo(null)
	}

	@Test
	fun firstCharOr() {
		val parser = parser("12").firstCharOr(parser("ab"))
		parser.parsed("12").assertEqualTo("12")
		parser.parsed("ab").assertEqualTo("ab")
		parser.parsed("1").assertEqualTo(null)
		parser.parsed("a").assertEqualTo(null)

		val parser2 = parser("12").firstCharOr(parser("13"))
		parser2.parsed("12").assertEqualTo("12")
		parser2.parsed("13").assertEqualTo(null)
	}

	@Test
	fun or() {
		val parser = parser("12").or(parser("13"))
		parser.parsed("12").assertEqualTo("12")
		parser.parsed("13").assertEqualTo("13")
	}

	@Test
	fun string() {
		stringParser.parsed("\"\"").assertEqualTo("")
		stringParser.parsed("\"foo\"").assertEqualTo("foo")
		stringParser.parsed("\"foo 123\"").assertEqualTo("foo 123")
		stringParser.parsed("\"\t\"").assertEqualTo("\t")
		stringParser.parsed("\"\n\"").assertEqualTo("\n")
		stringParser.parsed("\"\\\\\"").assertEqualTo("\\")
		stringParser.parsed("\"\\\"\"").assertEqualTo("\"")

		stringParser.parsed("").assertEqualTo(null)
		stringParser.parsed("\"").assertEqualTo(null)
		stringParser.parsed("\"foo").assertEqualTo(null)
	}

	@Test
	fun tabParser() {
		indentParser.parsed("  ").assertEqualTo(Unit)
		indentParser.parsed("").assertEqualTo(null)
		indentParser.parsed(" ").assertEqualTo(null)
		indentParser.parsed("   ").assertEqualTo(null)
	}
}