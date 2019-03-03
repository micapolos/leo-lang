package leo

import leo.base.assertEqualTo
import leo.base.assertParsedAndRest
import leo.base.onlyStream
import leo.base.then
import leo.binary.Bit
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
	fun reflect_control() {
		begin.control.token
			.reflect
			.assertEqualTo(
				tokenWord fieldTo begin.control.reflect.term)
	}

	@Test
	fun parse_word() {
		(tokenWord fieldTo term(
			wordWord fieldTo oneWord.term))
			.parseToken
			.assertEqualTo(oneWord.token)
	}

	@Test
	fun parse_control() {
		(tokenWord fieldTo begin.control.reflect.term)
			.parseToken
			.assertEqualTo(begin.control.token)
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
		begin.control.token
			.bitStream
			.then { Bit.ZERO.onlyStream }
			.bitParseToken
			.assertParsedAndRest(begin.control.token, Bit.ZERO.onlyStream)
	}

	@Test
	fun bitParseToken_end() {
		end.control.token
			.bitStream
			.then { Bit.ZERO.onlyStream }
			.bitParseToken
			.assertParsedAndRest(end.control.token, Bit.ZERO.onlyStream)
	}
}