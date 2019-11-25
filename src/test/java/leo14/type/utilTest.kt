package leo14.type

import kotlin.test.Test

class ConstructorsTest {
	@Test
	fun constructors() {
		reference(0)
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
		type(reference(0) actionTo reference(0))
		type(list("bit".field))
		type(index(size(8)))
		type(table("bit".field, size(8)))

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
		table("bit".field, size(8))

		recursive(type())

		scope()
		scope(type())
		scope(type(), type())
	}
}