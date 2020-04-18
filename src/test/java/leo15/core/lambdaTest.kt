package leo15.core

import kotlin.test.Test

class LambdaTest {
	@Test
	fun lambdaTo() {
		intTyp
			.gives(stringTyp) { unsafeValue.toString().anyJava }
			.invoke(10.java)
			.assertGives("10".java)
	}
}