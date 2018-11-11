package leo

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class OneOfTest {
  @Test
  fun string() {
    oneOf(pattern(term(oneWord)), pattern(term(twoWord)))
        .string
        .assertEqualTo("one of(pattern term identifier word one, pattern term identifier word two)")
  }

  @Test
  fun parse_onePattern() {
    script(
	    term(eitherWord fieldTo term(oneWord)))
        .parseOneOf
        .assertEqualTo(
            oneOf(
	            pattern(term(oneWord))))
  }

  @Test
  fun parse_manyPatterns() {
    script(
        term(
            eitherWord fieldTo term(oneWord),
	        eitherWord fieldTo term(twoWord)))
        .parseOneOf
        .assertEqualTo(
            oneOf(
                pattern(term(oneWord)),
	            pattern(term(twoWord))))
  }

  @Test
  fun parse_illegal() {
    script(
        term(
            eitherWord fieldTo term(oneWord),
            personWord fieldTo term(nameWord),
	        eitherWord fieldTo term(twoWord)))
        .parseOneOf
        .assertEqualTo(null)
  }

  @Test
  fun scriptMatches_first() {
    script(term(oneWord))
        .matches(oneOf(pattern(term(oneWord)), pattern(term(twoWord))))
        .assertEqualTo(true)
  }

  @Test
  fun scriptMatches_second() {
    script(term(twoWord))
        .matches(oneOf(pattern(term(oneWord)), pattern(term(twoWord))))
        .assertEqualTo(true)
  }

  @Test
  fun scriptMatches_none() {
    script(term(ageWord))
        .matches(oneOf(pattern(term(oneWord)), pattern(term(twoWord))))
        .assertEqualTo(false)
  }
}