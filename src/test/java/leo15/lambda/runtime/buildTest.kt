package leo15.lambda.runtime

import leo.base.assertEqualTo
import leo15.lambda.builder.append
import leo15.lambda.builder.term
import org.junit.Test

class BuildTest {
	@Test
	fun pair() {
		10.term
			.append(20.term)
			.build
			.assertEqualTo(
				term(lambda(lambda(lambda(at(0), term(at(2)), term(at(1))))), term(value(10)), term(value(20))))
	}
}