package leo14.untyped

import leo.base.assert
import leo13.base.negate
import leo14.fieldTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class MatchTest {
	@Test
	fun all() {
		script().matches(script()).assert

		script("number").matches(script(literal(123))).assert
		script("text").matches(script(literal(123))).negate.assert
		script("foo").matches(script(literal(123))).negate.assert

		script("number").matches(script(literal("foo"))).negate.assert
		script("text").matches(script(literal("foo"))).assert
		script("foo").matches(script(literal("foo"))).negate.assert

		script("foo").matches(script("foo")).assert

		script("zero" fieldTo script(), "plus" fieldTo script("one"))
			.matches(script("zero" fieldTo script(), "plus" fieldTo script("one")))
			.assert

		script("zero" fieldTo script(), "plus" fieldTo script("one"))
			.matches(script("one" fieldTo script(), "plus" fieldTo script("zero")))
			.negate
			.assert

		script("zero" fieldTo script(), "or" fieldTo script("one")).matches(script("zero")).assert
		script("zero" fieldTo script(), "or" fieldTo script("one")).matches(script("one")).assert
		script("zero" fieldTo script(), "or" fieldTo script("one")).matches(script("two")).negate.assert
	}
}