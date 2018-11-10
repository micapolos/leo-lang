package leo

import leo.*
import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class PatternTest {
	@Test
	fun string() {
		pattern(term(oneWord))
				.string
				.assertEqualTo("pattern term identifier word one")
	}

	@Test
	fun parse_literal() {
		script(term(oneWord))
				.parsePattern
				.assertEqualTo(pattern(term(oneWord)))
	}

	@Test
	fun parse_list() {
		script(
      term(
        nameWord fieldTo term(stringWord),
        ageWord fieldTo term(numberWord)
      )
    )
				.parsePattern
				.assertEqualTo(
          pattern(
            term(
              nameWord fieldTo term(stringWord),
              ageWord fieldTo term(numberWord)
            )
          )
        )
	}

	@Test
	fun parse_oneOf() {
		script(
      term(
        eitherWord fieldTo term(stringWord),
        eitherWord fieldTo term(numberWord)
      )
    )
				.parsePattern
				.assertEqualTo(
          pattern(
            oneOf(
              pattern(term(stringWord)),
              pattern(term(numberWord))
            )
          )
        )
	}

	@Test
	fun parse_innerOneOf() {
		script(
      term(
        oneWord fieldTo term(
          eitherWord fieldTo term(stringWord),
          eitherWord fieldTo term(numberWord)
        )
      )
    )
				.parsePattern
				.assertEqualTo(
          pattern(
            term(
              oneWord fieldTo term(
                oneOf(
                  pattern(term(stringWord)),
                  pattern(term(numberWord))
                )
              )
            )
          )
        )
	}

	@Test
	fun scriptMatches_literal() {
		script(term(oneWord))
				.matches(pattern(term(oneWord)))
				.assertEqualTo(true)
	}

	@Test
	fun scriptMatches_list() {
		script(
      term(
        nameWord fieldTo term(stringWord),
        ageWord fieldTo term(numberWord)
      )
    )
				.matches(
          pattern(
            term(
              nameWord fieldTo term(stringWord),
              ageWord fieldTo term(numberWord)
            )
          )
        )
				.assertEqualTo(true)
	}

	@Test
	fun scriptMatches_oneOf_match() {
		script(term(nameWord))
				.matches(
          pattern(
            term(
              oneOf(
                pattern(term(nameWord)),
                pattern(term(ageWord))
              )
            )
          )
        )
				.assertEqualTo(true)
	}
}