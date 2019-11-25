package leo14.type

import leo.base.assertEqualTo
import kotlin.test.Test

class StaticTest {
	@Test
	fun isStatic() {
		nativeType.isStatic.assertEqualTo(false)
		type().isStatic.assertEqualTo(true)
		type("foo" fieldTo type()).isStatic.assertEqualTo(true)
		type("foo" fieldTo type(choice())).isStatic.assertEqualTo(false)
		type("foo" fieldTo type(), "bar" fieldTo type()).isStatic.assertEqualTo(true)
		type(choice()).isStatic.assertEqualTo(false)
		type(type() actionTo type()).isStatic.assertEqualTo(true)
		type(type(choice()) actionTo type()).isStatic.assertEqualTo(true)
		type(type() actionTo type(choice())).isStatic.assertEqualTo(true)
		type(type(choice()) actionTo type(choice())).isStatic.assertEqualTo(false)
		type(recursive(type())).isStatic.assertEqualTo(false)

		type("foo" fieldTo reference(0)).with(scope(type())).isStatic.assertEqualTo(true)
		type("foo" fieldTo reference(0)).with(scope(type(choice()))).isStatic.assertEqualTo(false)
	}
}