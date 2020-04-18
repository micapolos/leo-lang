package leo15.type.core

import leo.base.assertEqualTo
import leo.base.assertNull
import leo15.core.*
import kotlin.test.Test

class OrTest {
	@Test
	fun switch() {
		10.java
			.or(stringTyp)
			.run {
				switch(
					intTyp.gives(stringTyp) { unsafeValue.toString().java },
					stringTyp.gives(stringTyp) { unsafeValue.toUpperCase().java })
					.assertGives("10".java)
			}

		intTyp
			.or("foo".java)
			.run {
				switch(
					intTyp.gives(stringTyp) { unsafeValue.toString().java },
					stringTyp.gives(stringTyp) { unsafeValue.toUpperCase().java })
					.assertGives("FOO".java)
			}
	}

	@Test
	fun unsafe() {
		10.java.or(stringTyp).unsafeFirstOrNull.assertEqualTo(10.java)
		10.java.or(stringTyp).unsafeSecondOrNull.assertNull

		intTyp.or("foo".java).unsafeFirstOrNull.assertNull
		intTyp.or("foo".java).unsafeSecondOrNull.assertEqualTo("foo".java)
	}
}