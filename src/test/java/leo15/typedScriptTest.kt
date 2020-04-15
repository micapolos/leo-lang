package leo15

import leo.base.assertEqualTo
import leo14.bigDecimal
import leo14.invoke
import leo14.leo
import leo15.lambda.valueTerm
import kotlin.test.Test
import kotlin.test.assertFails

class TypedScriptTest {
	@Test
	fun literals() {
		12.typed.script.assertEqualTo(leo(12))
		"foo".typed.script.assertEqualTo(leo("foo"))
		typed("foo").script.assertEqualTo(leo("foo"()))
	}

	@Test
	fun alternative() {
		textType
			.or(numberType)
			.typed(0, 10.bigDecimal.valueTerm)
			.script
			.assertEqualTo(leo(10))

		textType
			.or(numberType)
			.typed(1, "foo".valueTerm)
			.script
			.assertEqualTo(leo("foo"))

		assertFails {
			textType
				.or(numberType)
				.typed(2, "foo".valueTerm)
				.script
		}
	}

	@Test
	fun repeating() {
		textTypeLine.repeating.emptyTyped
			.script
			.assertEqualTo(leo())

		textTypeLine.repeating.emptyTyped
			.plusRepeating("foo".typedLine)
			.script
			.assertEqualTo(leo("foo"))

		textTypeLine.repeating.emptyTyped
			.plusRepeating("foo".typedLine)
			.plusRepeating("bar".typedLine)
			.script
			.assertEqualTo(leo("foo", "bar"))
	}
}