package leo14.typed

import leo.base.assertEqualTo
import leo14.fieldTo
import leo14.lineTo
import leo14.literal
import leo14.native.native
import leo14.script
import kotlin.test.Test

class DecompileTest {
	@Test
	fun string() {
		typed(native("Hello, world!"))
			.decompile
			.assertEqualTo(script(literal("Hello, world!")))
	}

	@Test
	fun field() {
		typed("text" fieldTo typed(native("Hello, world!")))
			.decompile
			.assertEqualTo(script("text" fieldTo literal("Hello, world!")))
	}

	@Test
	fun struct() {
		typed("vec" fieldTo typed(
			"x" fieldTo typed(native(2.0)),
			"y" fieldTo typed(native(3.0))))
			.decompile
			.assertEqualTo(
				script(
					"vec" fieldTo script(
						"x" fieldTo literal(2.0),
						"y" fieldTo literal(3.0))))
	}

	@Test
	fun nativeInt() {
		typed(native(123))
			.decompile
			.assertEqualTo(script(literal(123)))
	}

	@Test
	fun nativeFields() {
		typed(
			"x" lineTo typed(native(2)),
			"y" lineTo typed(native(3)))
			.decompile
			.assertEqualTo(
				script(
					"x" lineTo script(literal(2)),
					"y" lineTo script(literal(3))))
	}
}