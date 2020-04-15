package leo15

import leo.base.assertContains
import leo15.lambda.nilTerm
import leo15.lambda.valueTerm
import kotlin.test.Test

class TermsTest {
	@Test
	fun repeatingTermSeq() {
		nilTerm.repeatingTermSeq.assertContains()

		nilTerm.plus(10.valueTerm)
			.repeatingTermSeq
			.assertContains(10.valueTerm)

		nilTerm.plus(10.valueTerm).plus(20.valueTerm)
			.repeatingTermSeq
			.assertContains(20.valueTerm, 10.valueTerm)

		nilTerm.plus(10.valueTerm).plus(20.valueTerm).plus(30.valueTerm)
			.repeatingTermSeq
			.assertContains(30.valueTerm, 20.valueTerm, 10.valueTerm)
	}
}