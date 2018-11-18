package leo

import leo.base.assertEqualTo
import org.junit.Test

class TokenTest {
	@Test
	fun reflect_begin() {
		BeginToken(oneWord)
			.reflect
			.assertEqualTo(
				tokenWord fieldTo term(
					beginWord fieldTo term(
						oneWord.field)))
	}

	@Test
	fun reflect_end() {
		EndToken
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
			.assertEqualTo(BeginToken(oneWord))
	}

	@Test
	fun parse_end() {
		tokenWord.fieldTo(endWord.term)
			.parseToken
			.assertEqualTo(EndToken)
	}
}