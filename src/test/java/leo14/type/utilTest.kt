package leo14.type

import leo13.index0
import kotlin.test.Test

class ConstructorsTest {
	@Test
	fun constructors() {
		reference(index0)
		reference(type())

		type()
		type("foo")
		type("foo", "bar")
		type("foo" fieldTo type())
		type("foo" fieldTo type(), "bar" fieldTo type())
		type(structure())
		type(choice())
		type(recursive(type()))
		type(type() actionTo type())
		type(reference(index0) actionTo reference(index0))

		"foo" fieldTo type()
		"foo" fieldTo reference(index0)

		structure()
		structure("foo")
		structure("foo", "bar")
		structure("foo" fieldTo type())
		structure("foo" fieldTo type(), "bar" fieldTo type())

		"foo" optionTo type()
		"foo" optionTo reference(index0)

		choice()
		choice("foo")
		choice("foo", "bar")
		choice("foo" optionTo type())
		choice("foo" optionTo type(), "bar" optionTo type())

		type() actionTo type()

		recursive(type())

		scope()
		scope(type())
		scope(type(), type())
	}
}