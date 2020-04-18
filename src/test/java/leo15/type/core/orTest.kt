package leo15.type.core

import leo.base.assertEqualTo
import leo.base.assertNull
import leo15.core.intTyp
import leo15.core.java
import leo15.core.or
import leo15.core.stringTyp
import kotlin.test.Test

class OrTest {
	@Test
	fun eitherFirst() {
		10.java
			.or(stringTyp)
			.run {
				firstOrNull.assertEqualTo(10.java)
				secondOrNull.assertNull
				match(stringTyp,
					{ value.toString().java },
					{ value.toUpperCase().java })
					.assertEqualTo("10".java)
			}

		intTyp
			.or("foo".java)
			.run {
				firstOrNull.assertNull
				secondOrNull.assertEqualTo("foo".java)
				match(stringTyp,
					{ value.toString().java },
					{ value.toUpperCase().java })
					.assertEqualTo("FOO".java)
			}
	}
}