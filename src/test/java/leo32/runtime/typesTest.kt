package leo32.runtime

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class TypesTest {
	@Test
	fun resolve() {
		val bitTypeTerm = term("bit" to term("either" to term("zero"), "either" to term("one")))
		val bitZeroTerm = term("bit" to term("zero"))
		val bitOneTerm = term("bit" to term("one"))
		val bitTwoTerm = term("bit" to term("two"))

		val typeTerms = empty.typeTerms
			.put(bitZeroTerm, bitTypeTerm)
			.put(bitOneTerm, bitTypeTerm)

		typeTerms.typeTerm(bitZeroTerm).assertEqualTo(bitTypeTerm)
		typeTerms.typeTerm(bitOneTerm).assertEqualTo(bitTypeTerm)
		typeTerms.typeTerm(bitTwoTerm).assertEqualTo(bitTwoTerm)
		typeTerms.typeTerm(term("the" to bitZeroTerm)).assertEqualTo(term("the" to bitZeroTerm))
		typeTerms.typeTerm(term("the" to bitOneTerm)).assertEqualTo(term("the" to bitOneTerm))
		typeTerms.typeTerm(term("the" to bitTwoTerm)).assertEqualTo(term("the" to bitTwoTerm))
		typeTerms.typeTerm(term("the" to bitTypeTerm)).assertEqualTo(term("the" to bitTypeTerm))

		typeTerms
			.typeTerm(
				term(
					"i3" to term(
						"the" to bitZeroTerm,
						"the" to bitOneTerm,
						"the" to bitTwoTerm)))
			.assertEqualTo(
				term(
					"i3" to term(
						"the" to bitZeroTerm,
						"the" to bitOneTerm,
						"the" to bitTwoTerm)))
	}
}