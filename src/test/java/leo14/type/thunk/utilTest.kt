package leo14.type.thunk

import leo.base.assertEqualTo
import leo14.type.fieldTo
import leo14.type.reference
import leo14.type.scope
import leo14.type.type
import kotlin.test.Test

class UtilTest {
	@Test
	fun access() {
		val thunk = type(
			"circle" fieldTo type(
				"radius" fieldTo type("zero"),
				"center" fieldTo type(
					"point" fieldTo type(
						"x" fieldTo type("one"),
						"y" fieldTo type("two")))))
			.with(scope(type("foo")))

		thunk
			.structureThunkOrNull!!
			.lastFieldThunkOrNull!!
			.rhsThunk
			.structureThunkOrNull!!
			.previousThunkOrNull!!
			.lastFieldThunkOrNull!!
			.rhsThunk
			.assertEqualTo(type("zero").with(scope(type("foo"))))
	}

	@Test
	fun loop() {
		val type = type("loop" fieldTo reference(0))
		val thunk = type.with(scope(type))
		thunk
			.structureThunkOrNull!!
			.onlyFieldThunkOrNull!!
			.rhsThunk
			.assertEqualTo(thunk)
	}
}