package leo15.typed

import leo.base.assertEqualTo
import leo15.asName
import leo15.lambda.append
import leo15.lambda.valueFn
import leo15.lambda.valueTerm
import leo15.plusName
import kotlin.test.Test
import kotlin.test.assertFails

class TypedTest {
	@Test
	fun append() {
		runLeo {
			"Hello, ".typed
				.apply(plusName, "world!".typed)
				.eval
				.assertEqualTo("Hello, world!".typed)

			10.typed
				.apply(plusName, 20.typed)
				.eval
				.assertEqualTo(30.typed)

			define(
				"double".valueTerm.append(intType),
				intType,
				valueFn {
					(it as Int) * 2
				})

			10.typed
				.apply("double")
				.eval
				.assertEqualTo(20.typed)
		}
	}

	@Test
	fun as_() {
		runLeo {
			20.typed.apply(asName, intType.typeTyped).assertEqualTo(20.typed)
			assertFails { 20.typed.apply(asName, textType.typeTyped) }
		}
	}
}