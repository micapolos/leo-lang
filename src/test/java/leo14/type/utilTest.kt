package leo14.type

import kotlin.test.Test

class ConstructorsTest {
	@Test
	fun constructors() {
		reference(0)
		reference(type())
		reference(recursive(type()))

		type()
		type("foo")
		type("foo", "bar")
		type("foo" fieldTo type())
		type("foo" fieldTo type(), "bar" fieldTo type())
		type(structure())
		type(choice())
		type(type() actionTo type())
		type(reference(0) actionTo reference(0))
		type(list("bit".field))

		"foo" fieldTo type()
		"foo" fieldTo reference(0)

		structure()
		structure("foo")
		structure("foo", "bar")
		structure("foo" fieldTo type())
		structure("foo" fieldTo type(), "bar" fieldTo type())

		choice()
		choice("foo")
		choice("foo", "bar")
		choice("foo" fieldTo type())
		choice("foo" fieldTo type(), "bar" fieldTo type())

		type() actionTo type()

		list("bit".field)

		recursive(type())

		scope()
		scope(type())
		scope(type(), type())
	}
}