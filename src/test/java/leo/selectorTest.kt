package leo

import leo.base.assertEqualTo
import kotlin.test.Test


class SelectorTest {
  val testTerm: Term<Int> = term(
      oneWord fieldTo term(1),
      ageWord fieldTo term(42),
      ageWord fieldTo term(44),
      numberWord fieldTo term(
          firstWord fieldTo term(100),
	      lastWord fieldTo term(200)))

  @Test
  fun invokeEmpty() {
    selector()
        .invoke(testTerm)
        .assertEqualTo(testTerm)
  }

  @Test
  fun invokeSingleChoice() {
    selector(oneWord)
        .invoke(testTerm)
        .assertEqualTo(term(1))
  }

  @Test
  fun invokeMultipleChoice() {
    selector(ageWord)
        .invoke(testTerm)
        .assertEqualTo(null)
  }

  @Test
  fun invokeMissingChoice() {
    selector(personWord)
        .invoke(testTerm)
        .assertEqualTo(null)
  }

  @Test
  fun invokeDeep() {
    selector(numberWord, lastWord)
        .invoke(testTerm)
        .assertEqualTo(term(200))
  }

  @Test
  fun parse_this() {
    script(term(thisWord))
        .parseSelector(pattern(term(oneWord)))
        .assertEqualTo(selector())
  }

  @Test
  fun parse_simple() {
    script(term(oneWord fieldTo term(thisWord)))
        .parseSelector(
            pattern(
                term(
                    oneWord fieldTo term(numberWord),
	                twoWord fieldTo term(stringWord))))
        .assertEqualTo(selector(oneWord))
  }

  @Test
  fun parse_deep() {
    script(term(twoWord fieldTo term(oneWord fieldTo term(thisWord))))
        .parseSelector(pattern(term(oneWord fieldTo term(twoWord fieldTo term(numberWord)))))
        .assertEqualTo(selector(oneWord, twoWord))
  }

  @Test
  fun parse_mismatch() {
    script(term(oneWord fieldTo term(thisWord)))
        .parseSelector(pattern(term(twoWord fieldTo term(numberWord))))
        .assertEqualTo(null)
  }

  @Test
  fun parse_notOnly() {
    script(term(oneWord fieldTo term(thisWord)))
        .parseSelector(
            pattern(
                term(
                    oneWord fieldTo term(numberWord),
	                oneWord fieldTo term(stringWord))))
        .assertEqualTo(null)
  }
}