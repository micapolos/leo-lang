package leo

import leo.base.assertEqualTo
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
		endToken
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
			.assertEqualTo(endToken)
	}
}