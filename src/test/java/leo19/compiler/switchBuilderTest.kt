package leo19.compiler

import leo.base.assertEqualTo
import leo.base.reverse
import leo13.stack
import leo19.term.term
import leo19.term.variable
import leo19.type.caseTo
import leo19.type.struct
import leo19.typed.TypedSwitch
import leo19.typed.emptyTypedSwitch
import leo19.typed.fieldTo
import leo19.typed.of
import leo19.typed.plus
import leo19.typed.typed
import kotlin.test.Test
import kotlin.test.assertFails

class SwitchBuilderTest {
	@Test
	fun empty() {
		switchBuilder().build.assertEqualTo(emptyTypedSwitch)
	}

	@Test
	fun nullType() {
		SwitchBuilder(
			stack(
				"one" caseTo struct(),
				"zero" caseTo struct("ok")),
			TypedSwitch(
				stack(term(variable(128))),
				null))
			.plus("zero" fieldTo term(variable(0)).of(struct("ok")))
			.assertEqualTo(
				SwitchBuilder(
					stack("one" caseTo struct()),
					TypedSwitch(
						stack(term(variable(128)), term(variable(0))),
						struct("ok"))))
	}

	@Test
	fun existingType() {
		SwitchBuilder(
			stack(
				"one" caseTo struct(),
				"zero" caseTo struct("ok")),
			TypedSwitch(
				stack(term(variable(128))),
				struct("ok")))
			.plus("zero" fieldTo term(variable(0)).of(struct("ok")))
			.assertEqualTo(
				SwitchBuilder(
					stack("one" caseTo struct()),
					TypedSwitch(
						stack(term(variable(128)), term(variable(0))),
						struct("ok"))))
	}

	@Test
	fun existingTypeMismatch() {
		assertFails {
			SwitchBuilder(
				stack(
					"one" caseTo struct(),
					"zero" caseTo struct("ok")),
				TypedSwitch(
					stack(term(variable(128))),
					struct("zoo")))
				.plus("zero" fieldTo term(variable(0)).of(struct("ok")))
		}
	}

	@Test
	fun exhausted() {
		assertFails {
			SwitchBuilder(
				stack(),
				emptyTypedSwitch)
				.plus("zero" fieldTo term(variable(0)).of(struct("ok")))
		}
	}

	@Test
	fun build() {
		val switch = emptyTypedSwitch.plus("zero" fieldTo typed())
		SwitchBuilder(stack(), switch)
			.build
			.assertEqualTo(switch)
	}

	@Test
	fun build_incomplete() {
		val switch = emptyTypedSwitch.plus("zero" fieldTo typed())
		assertFails {
			SwitchBuilder(stack("zero" caseTo struct()), switch).build
		}
	}
}