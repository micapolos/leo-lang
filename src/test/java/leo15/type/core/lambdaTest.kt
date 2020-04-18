package leo15.type.core

import leo.base.assertEqualTo
import leo15.core.anyJava
import leo15.core.javaTyp
import leo15.core.lambdaTo
import kotlin.test.Test

class LambdaTest {
	@Test
	fun lambdaTo() {
		Integer.TYPE.javaTyp
			.lambdaTo(String::class.java.javaTyp) { value.toString().anyJava }
			.invoke(10.anyJava)
			.assertEqualTo("10".anyJava)
	}
}