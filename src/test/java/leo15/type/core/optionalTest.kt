package leo15.type.core

import leo.base.assertEqualTo
import leo.base.assertNull
import leo14.invoke
import leo15.absentName
import leo15.core.nothing
import leo15.core.nothingTyp
import leo15.core.optional
import leo15.optionalName
import leo15.presentName
import kotlin.test.Test

class OptionalTest {
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