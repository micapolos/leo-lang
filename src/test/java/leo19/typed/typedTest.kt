package leo19.typed

import leo.base.assertEqualTo
import leo.base.assertNull
import leo19.compiler.equalsKeyword
import leo19.term.get
import leo19.term.nullTerm
import leo19.term.term
import leo19.term.termEquals
import leo19.term.variable
import leo19.type.booleanType
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.type
import kotlin.test.Test
import kotlin.test.assertFails

class TypedTest {
	@Test
	fun get() {
		nullTyped.getOrNull("foo").assertNull
	}

	@Test
	fun getField() {
		term(variable(0))
			.of(type("point" fieldTo type("x" fieldTo choice(), "y" fieldTo choice())))
			.getOrNull("x")
			.assertEqualTo(term(variable(0)).get(term(0)).of(type("x" fieldTo choice())))

		term(variable(0))
			.of(type("point" fieldTo type("x" fieldTo choice(), "y" fieldTo choice())))
			.getOrNull("y")
			.assertEqualTo(term(variable(0)).get(term(1)).of(type("y" fieldTo choice())))

		term(variable(0))
			.of(type("point" fieldTo type("x" fieldTo type(), "y" fieldTo choice())))
			.getOrNull("x")
			.assertEqualTo(nullTerm.of(type("x" fieldTo type())))

		term(variable(0))
			.of(type("point" fieldTo type("x" fieldTo type(), "y" fieldTo choice())))
			.getOrNull("y")
			.assertEqualTo(term(variable(0)).of(type("y" fieldTo choice())))

		term(variable(0))
			.of(type("point" fieldTo type("x" fieldTo choice(), "y" fieldTo type())))
			.getOrNull("x")
			.assertEqualTo(term(variable(0)).of(type("x" fieldTo choice())))

		term(variable(0))
			.of(type("point" fieldTo type("x" fieldTo choice(), "y" fieldTo type())))
			.getOrNull("y")
			.assertEqualTo(nullTerm.of(type("y" fieldTo type())))

		term(variable(0))
			.of(type("point" fieldTo type("x" fieldTo type(), "y" fieldTo type())))
			.getOrNull("x")
			.assertEqualTo(nullTerm.of(type("x" fieldTo type())))

		term(variable(0))
			.of(type("point" fieldTo type("x" fieldTo choice(), "y" fieldTo type())))
			.getOrNull("y")
			.assertEqualTo(nullTerm.of(type("y" fieldTo type())))
	}

	@Test
	fun typedEquals_typeMatch() {
		true.typed
			.typedEquals(false.typed)
			.assertEqualTo(true.term.termEquals(false.term) of type(equalsKeyword fieldTo booleanType))
	}

	@Test
	fun typedEquals_typeMismatch() {
		assertFails {
			typed("foo").typedEquals(typed("bar"))
		}
	}
}