package leo

import leo.base.*
import leo.base.int
import org.junit.Test

class CharacterTest {
	@Test
	fun reflect_begin() {
		begin.character
			.reflect
			.assertEqualTo(
				characterWord fieldTo beginWord.term)
	}

	@Test
	fun reflect_end() {
		end.character
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
			.assertEqualTo(begin.character)
	}

	@Test
	fun parse_end() {
		characterWord
			.fieldTo(endWord.term)
			.parseCharacter
			.assertEqualTo(end.character)
	}

	@Test
	fun parse_letter() {
		characterWord.fieldTo(aWord.term)
			.parseCharacter
			.assertEqualTo(LetterCharacter(Letter.A))
	}

	@Test
	fun bitParseCharacter_begin() {
		begin.character
			.bitStream
			.then { EnumBit.ZERO.onlyStream }
			.bitParseCharacter
			.assertParsedAndRest(begin.character, EnumBit.ZERO.onlyStream)
	}

	@Test
	fun bitParseCharacter_end() {
		end.character
			.bitStream
			.then { EnumBit.ZERO.onlyStream }
			.bitParseCharacter
			.assertParsedAndRest(end.character, EnumBit.ZERO.onlyStream)
	}

	@Test
	fun bitParseCharacter_letter() {
		Letter.A.character
			.bitStream
			.then { EnumBit.ZERO.onlyStream }
			.bitParseCharacter
			.assertParsedAndRest(Letter.A.character, EnumBit.ZERO.onlyStream)
	}

	@Test
	fun bitSequence() {
		Letter.A.character.bitSequence.map(EnumBit::int).assertContains(0, 0, 0, 0, 0)
		Letter.Z.character.bitSequence.map(EnumBit::int).assertContains(1, 1, 0, 0, 1)
		begin.character.bitSequence.map(EnumBit::int).assertContains(1, 1, 0, 1, 0)
		end.character.bitSequence.map(EnumBit::int).assertContains(1, 1, 0, 1, 1)
	}
}