package leo13.script.reflect

import leo.base.assertEqualTo
import leo.binary.One
import leo.binary.Zero
import leo.binary.one
import leo.binary.zero
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test
import kotlin.test.assertFails
import leo13.base.Option as ValueOption
import leo13.base.option as valueOption

data class ZeroOne(
	val zero: Zero,
	val one: One)

class TypeTest {
	@Test
	fun struct() {
		val structType = type(
			"zeroone",
			body(
				struct(
					field(zeroType) { zero },
					field(oneType) { one }
				) { zero: Zero, one: One ->
					ZeroOne(zero, one)
				}))

		structType
			.scriptLine(ZeroOne(zero, one))
			.assertEqualTo("zeroone" lineTo script("zero" lineTo script(), "one" lineTo script()))

		assertFails {
			structType.unsafeValue("zeroone" lineTo script("zeroo" lineTo script(), "one" lineTo script()))
		}

		assertFails {
			structType.unsafeValue("zeroonie" lineTo script("zero" lineTo script(), "one" lineTo script()))
		}
	}

	@Test
	fun optionTest() {
		val optionType: Type<ValueOption<Zero>> = type("zeroable", body(option(zeroType)))

		optionType
			.scriptLine(valueOption(zero))
			.assertEqualTo("zeroable" lineTo script("option" lineTo script("zero")))

		optionType
			.scriptLine(valueOption<Zero>(null))
			.assertEqualTo("zeroable" lineTo script("option" lineTo script("null")))

		optionType
			.unsafeValue("zeroable" lineTo script("option" lineTo script("zero")))
			.assertEqualTo(valueOption(zero))

		optionType
			.unsafeValue("zeroable" lineTo script("option" lineTo script("null")))
			.assertEqualTo(valueOption(null))

		assertFails {
			optionType.unsafeValue("zeroable2" lineTo script("option" lineTo script("null")))
		}

		assertFails {
			optionType.unsafeValue("zeroable" lineTo script("option2" lineTo script("nulla")))
		}

		assertFails {
			optionType.unsafeValue("zeroable" lineTo script("option" lineTo script("null2")))
		}
	}
}