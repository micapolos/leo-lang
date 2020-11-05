package leo21.parser

import leo13.firstEither
import leo13.secondEither
import leo13.stack
import org.junit.Test

class ParserTest {
	@Test
	fun charParser() {
		parser('a').plus("a").assertParsed('a')
		parser('a').plus("aa").assertNotParsed
	}

	@Test
	fun toParser() {
		parser('a').to(parser('b')).plus("a").assertNotParsed
		parser('a').to(parser('b')).plus("aa").assertNotParsed
		parser('a').to(parser('b')).plus("ab").assertParsed('a' to 'b')
		parser('a').to(parser('b')).plus("aba").assertNotParsed
	}

	@Test
	fun orParser() {
		parser('a').or(parser('b')).plus("a").assertParsed('a'.firstEither())
		parser('a').or(parser('b')).plus("b").assertParsed('b'.secondEither())
		parser('a').or(parser('b')).plus("c").assertNotParsed
		parser('a').or(parser('b')).plus("aa").assertNotParsed
		parser('a').or(parser('b')).plus("ab").assertNotParsed
	}

	@Test
	fun stackParser() {
		parser('a').stackParser.plus("a").assertParsed(stack('a'))
		parser('a').stackParser.plus("aa").assertParsed(stack('a', 'a'))
	}

	@Test
	fun stringParser() {
		stringParser.plus("abc").assertParsed("abc")
	}
}