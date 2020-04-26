package leo15.lambda.runtime.java

import leo.base.assertEqualTo
import leo15.lambda.builder.id
import leo15.lambda.runtime.at
import leo15.lambda.runtime.build
import leo15.lambda.runtime.lambda
import leo15.lambda.runtime.term
import kotlin.test.Test

class BuildTest {
	@Test
	fun script() {
		id<Nothing>().build.assertEqualTo(term(lambda(term(at(0)))))
	}
}