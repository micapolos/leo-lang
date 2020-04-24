package leo15.lambda.java

import leo.base.assertEqualTo
import leo15.lambda.idTerm
import leo15.lambda.runtime.atom
import leo15.lambda.runtime.term
import kotlin.test.Test

class FromLambdaTest {
	@Test
	fun script() {
		term<Any?>(idTerm).assertEqualTo(term(atom(term(atom(0), null)), null))
	}
}