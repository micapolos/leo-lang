package leo

import leo.base.*
import org.junit.Test

class TokenTest {
	@Test
	fun reflect_word() {
		oneWord.token
			.reflect
			.assertEqualTo(
				tokenWord fieldTo oneWord.reflect.term)
	}

	@Test
	fun reflect_begin() {
		begin.token
			.reflect
			.assertEqualTo(
				tokenWord fieldTo beginWord.term)
	}

	@Test
	fun reflect_end() {
		end.token
			.reflect
			.assertEqualTo(
				tokenWord fieldTo endWord.term)
	}

	@Test
	fun parse_word() {
		(tokenWord fieldTo term(
			wordWord fieldTo oneWord.term))
			.parseToken
			.assertEqualTo(oneWord.token)
	}

	@Test
	fun parse_begin() {
		(tokenWord fieldTo beginWord.term)
			.parseToken
			.assertEqualTo(begin.token)
	}

	@Test
	fun parse_end() {
		tokenWord.fieldTo(endWord.term)
			.parseToken
			.assertEqualTo(end.token)
	}

	@Test
	fun bitParseToken_word() {
		oneWord.token
			.bitStream
			.then { Bit.ZERO.onlyStream }
			.bitParseToken
			.assertParsedAndRest(oneWord.token, Bit.ZERO.onlyStream)
	}

	@Test
	fun bitParseToken_begin() {
		begin.token
			.bitStream
			.then { Bit.ZERO.onlyStream }
			.bitParseToken
			.assertParsedAndRest(begin.token, Bit.ZERO.onlyStream)
	}

	@Test
	fun bitParseToken_end() {
		end.token
			.bitStream
			.then { Bit.ZERO.onlyStream }
			.bitParseToken
			.assertParsedAndRest(end.token, Bit.ZERO.onlyStream)
	}
}