package leo20

import leo.base.assertEqualTo
import kotlin.test.Test

class DefinitionTest {
	@Test
	fun resolve_numberPlus() {
		numberPlusDefinition
			.resolveOrNull(
				value(
					line(2),
					"plus" lineTo value(3)))
			.assertEqualTo(value(5))
	}

	@Test
	fun resolve_numberMinus() {
		numberMinusDefinition
			.resolveOrNull(
				value(
					line(5),
					"minus" lineTo value(3)))
			.assertEqualTo(value(2))
	}

	@Test
	fun resolve_numberEquals() {
		numberEqualsDefinition
			.resolveOrNull(
				value(
					line(2),
					"equals" lineTo value(2)))
			.assertEqualTo(true.value)
	}

	@Test
	fun resolve_textAppend() {
		textAppendDefinition
			.resolveOrNull(
				value(
					line("Hello, "),
					"append" lineTo value("world!")))
			.assertEqualTo(value("Hello, world!"))
	}
}