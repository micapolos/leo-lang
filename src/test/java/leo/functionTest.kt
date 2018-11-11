package leo

import leo.base.assertEqualTo
import leo.base.stack
import kotlin.test.Test

class FunctionTest {
  @Test
  fun invoke_fallback() {
    Function(null)
        .invoke(script(term(oneWord)))
        .assertEqualTo(script(term(oneWord)))
  }

  @Test
  fun invoke_single() {
    Function(
        stack(
            pattern(term(nameWord)) returns template(term(stringWord)),
	        pattern(term(ageWord)) returns template(term(numberWord))))
        .invoke(script(term(nameWord)))
        .assertEqualTo(script(term(stringWord)))
  }

  @Test
  fun invoke_chain() {
    Function(
        stack(
            pattern(term(nameWord)) returns template(term(stringWord)),
	        pattern(term(ageWord)) returns template(term(nameWord))))
        .invoke(script(term(ageWord)))
        .assertEqualTo(script(term(stringWord)))
  }
}