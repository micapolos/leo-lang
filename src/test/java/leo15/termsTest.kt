package leo15

import leo.base.assertContains
import leo14.lambda2.nil
import leo14.lambda2.valueTerm
import kotlin.test.Test

class TermsTest {
	@Test
	fun repeatingTermSeq() {
		nil.repeatingTermSeq.assertContains()

		nil.plus(10.valueTerm)
			.repeatingTermSeq
			.assertContains(10.valueTerm)

		nil.plus(10.valueTerm).plus(20.valueTerm)
			.repeatingTermSeq
			.assertContains(20.valueTerm, 10.valueTerm)

		nil.plus(10.valueTerm).plus(20.valueTerm).plus(30.valueTerm)
			.repeatingTermSeq
			.assertContains(30.valueTerm, 20.valueTerm, 10.valueTerm)
	}
}