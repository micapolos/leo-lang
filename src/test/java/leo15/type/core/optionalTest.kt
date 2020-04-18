package leo15.type.core

import leo.base.assertEqualTo
import leo.base.assertNotEqualTo
import leo.base.assertNull
import leo14.invoke
import leo15.absentName
import leo15.core.*
import leo15.optionalName
import leo15.presentName
import kotlin.test.Test

class OptionalTest {
	@Test
	fun equality() {
		intTyp.optional.assertEqualTo(intTyp.optional)
		10.java.optional.assertEqualTo(10.java.optional)
		intTyp.optional.assertNotEqualTo(10.java.optional)
		10.java.optional.assertNotEqualTo(intTyp.optional)
	}

	@Test
	fun logic() {
		nothingTyp.optional.orNull.assertNull
		nothing.optional.orNull.assertEqualTo(nothing)
	}

	@Test
	fun scriptLine() {
		nothingTyp.optional.scriptLine.assertEqualTo(optionalName(absentName(nothingTyp.scriptLine)))
		nothing.optional.scriptLine.assertEqualTo(optionalName(presentName(nothing.scriptLine)))
	}
}