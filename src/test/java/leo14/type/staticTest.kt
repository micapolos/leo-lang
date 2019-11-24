package leo14.type

import leo.base.assertEqualTo
import leo13.index0
import kotlin.test.Test

class StaticTest {
	@Test
	fun isStatic() {
		emptyType.isStatic.assertEqualTo(true)
		nativeType.isStatic.assertEqualTo(false)
		type("foo" fieldTo type()).isStatic.assertEqualTo(true)
		type("foo" fieldTo type(choice())).isStatic.assertEqualTo(false)
		type("foo" fieldTo type(), "bar" fieldTo type()).isStatic.assertEqualTo(true)
		type(choice()).isStatic.assertEqualTo(false)
		type(type() actionTo type()).isStatic.assertEqualTo(true)
		type(type(choice()) actionTo type()).isStatic.assertEqualTo(true)
		type(type() actionTo type(choice())).isStatic.assertEqualTo(true)
		type(type(choice()) actionTo type(choice())).isStatic.assertEqualTo(false)
		type(recursive(type())).isStatic.assertEqualTo(false)

		reference(index0).isStatic(scope(type())).assertEqualTo(true)
		reference(index0).isStatic(scope(type(choice()))).assertEqualTo(false)
	}
}