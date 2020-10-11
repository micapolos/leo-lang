package leo19.typed

import leo.base.assertEqualTo
import leo.base.assertNull
import leo19.term.get
import leo19.term.nullTerm
import leo19.term.term
import leo19.term.variable
import leo19.type.choice
import leo19.type.fieldTo
import leo19.type.struct
import kotlin.test.Test

class TypedTest {
	@Test
	fun get() {
		nullTyped.getOrNull("foo").assertNull
	}

	@Test
	fun getField() {
		term(variable(0))
			.of(struct("point" fieldTo struct("x" fieldTo choice(), "y" fieldTo choice())))
			.getOrNull("x")
			.assertEqualTo(term(variable(0)).get(term(0)).of(struct("x" fieldTo choice())))

		term(variable(0))
			.of(struct("point" fieldTo struct("x" fieldTo choice(), "y" fieldTo choice())))
			.getOrNull("y")
			.assertEqualTo(term(variable(0)).get(term(1)).of(struct("y" fieldTo choice())))

		term(variable(0))
			.of(struct("point" fieldTo struct("x" fieldTo struct(), "y" fieldTo choice())))
			.getOrNull("x")
			.assertEqualTo(nullTerm.of(struct("x" fieldTo struct())))

		term(variable(0))
			.of(struct("point" fieldTo struct("x" fieldTo struct(), "y" fieldTo choice())))
			.getOrNull("y")
			.assertEqualTo(term(variable(0)).of(struct("y" fieldTo choice())))

		term(variable(0))
			.of(struct("point" fieldTo struct("x" fieldTo choice(), "y" fieldTo struct())))
			.getOrNull("x")
			.assertEqualTo(term(variable(0)).of(struct("x" fieldTo choice())))

		term(variable(0))
			.of(struct("point" fieldTo struct("x" fieldTo choice(), "y" fieldTo struct())))
			.getOrNull("y")
			.assertEqualTo(nullTerm.of(struct("y" fieldTo struct())))

		term(variable(0))
			.of(struct("point" fieldTo struct("x" fieldTo struct(), "y" fieldTo struct())))
			.getOrNull("x")
			.assertEqualTo(nullTerm.of(struct("x" fieldTo struct())))

		term(variable(0))
			.of(struct("point" fieldTo struct("x" fieldTo choice(), "y" fieldTo struct())))
			.getOrNull("y")
			.assertEqualTo(nullTerm.of(struct("y" fieldTo struct())))
	}
}