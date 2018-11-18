package leo

import leo.base.assertEqualTo
import org.junit.Test

class CharacterTest {
	@Test
	fun reflect_begin() {
		BeginCharacter
			.reflect
			.assertEqualTo(
				characterWord fieldTo beginWord.term)
	}

	@Test
	fun reflect_end() {
		EndCharacter
			.reflect
			.assertEqualTo(
				characterWord fieldTo endWord.term)
	}

	@Test
	fun reflect_letter() {
		LetterCharacter(Letter.A)
			.reflect
			.assertEqualTo(
				characterWord fieldTo aWord.term)
	}

	@Test
	fun parse_begin() {
		characterWord
			.fieldTo(beginWord.term)
			.parseCharacter
			.assertEqualTo(BeginCharacter)
	}

	@Test
	fun parse_end() {
		characterWord
			.fieldTo(endWord.term)
			.parseCharacter
			.assertEqualTo(EndCharacter)
	}

	@Test
	fun parse_letter() {
		characterWord.fieldTo(aWord.term)
			.parseCharacter
			.assertEqualTo(LetterCharacter(Letter.A))
	}
}