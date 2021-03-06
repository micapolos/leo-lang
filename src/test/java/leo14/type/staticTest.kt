package leo14.type

import leo.base.assertEqualTo
import leo14.type.thunk.with
import kotlin.test.Test

class StaticTest {
	@Test
	fun isStatic() {
		type(native(type("foo"), isStatic = false)).isStatic.assertEqualTo(false)
		type(native(type("foo"), isStatic = true)).isStatic.assertEqualTo(true)

		type().isStatic.assertEqualTo(true)
		type("foo" fieldTo type()).isStatic.assertEqualTo(true)
		type("foo" fieldTo type(choice())).isStatic.assertEqualTo(false)
		type("foo" fieldTo type(), "bar" fieldTo type()).isStatic.assertEqualTo(true)
		type(choice()).isStatic.assertEqualTo(false)
		type(type() actionTo type()).isStatic.assertEqualTo(true)
		type(type(choice()) actionTo type()).isStatic.assertEqualTo(true)
		type(type() actionTo type(choice())).isStatic.assertEqualTo(true)
		type(type(choice()) actionTo type(choice())).isStatic.assertEqualTo(false)

		type("foo" fieldTo reference(0)).with(scope(type())).isStatic.assertEqualTo(true)
		type("foo" fieldTo reference(0)).with(scope(type(choice()))).isStatic.assertEqualTo(false)
		type("foo" fieldTo reference(recursive(type()))).with(scope()).isStatic.assertEqualTo(false)
	}
}