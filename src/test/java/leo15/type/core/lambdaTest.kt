package leo15.type.core

import leo.base.assertEqualTo
import leo15.core.*
import kotlin.test.Test

class LambdaTest {
	@Test
	fun lambdaTo() {
		intTyp
			.gives(stringTyp) { value.toString().anyJava }
			.invoke(10.java)
			.assertEqualTo("10".java)
	}
}