package leo

import leo.base.*
import org.junit.Test

class TokenTest {
	@Test
	fun reflect_begin() {
		oneWord.beginToken
			.reflect
			.assertEqualTo(
				tokenWord fieldTo term(
					beginWord fieldTo term(
						oneWord.field)))
	}

	@Test
	fun reflect_end() {
		end.token
			.reflect
			.assertEqualTo(
				tokenWord fieldTo term(
					endWord.field))
	}

	@Test
	fun parse_begin() {
		(tokenWord fieldTo term(
			beginWord fieldTo term(
				oneWord.field)))
			.parseToken
			.assertEqualTo(oneWord.beginToken)
	}

	@Test
	fun parse_end() {
		tokenWord.fieldTo(endWord.term)
			.parseToken
			.assertEqualTo(end.token)
	}

	@Test
	fun bitParseToken_begin() {
		oneWord.beginToken
			.bitStream
			.then(Bit.ZERO.onlyStream)
			.bitParseToken
			.assertParsedAndRest(oneWord.beginToken, Bit.ZERO.onlyStream)
	}

	@Test
	fun bitParseToken_end() {
		end.token
			.bitStream
			.then(Bit.ZERO.onlyStream)
			.bitParseToken
			.assertParsedAndRest(end.token, Bit.ZERO.onlyStream)
	}
}