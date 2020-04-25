package leo15.lambda.runtime.java

import leo.base.assertEqualTo
import leo15.lambda.idTerm
import leo15.lambda.runtime.at
import leo15.lambda.runtime.lambda
import leo15.lambda.runtime.term
import kotlin.test.Test

class FromLambdaTest {
	@Test
	fun script() {
		term<Any?>(idTerm).assertEqualTo(term(lambda(term(at(0)))))
	}
}