package leo19.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo19.type.caseTo
import leo19.type.struct
import leo19.typed.emptyTypedChoice
import leo19.typed.fieldTo
import leo19.typed.plusNo
import leo19.typed.plusYes
import leo19.typed.typed
import kotlin.test.Test
import kotlin.test.assertFails

class ChoiceCompilerTest {
	@Test
	fun correct() {
		emptyResolver
			.choice(
				script(
					"no" lineTo script("circle" lineTo script("radius")),
					"yes" lineTo script("square" lineTo script("side"))))
			.assertEqualTo(
				emptyTypedChoice
					.plusNo("circle" caseTo struct("radius"))
					.plusYes("square" fieldTo typed("side")))
	}

	@Test
	fun invalid() {
		assertFails {
			emptyResolver
				.choice(
					script(
						"no" lineTo script("circle" lineTo script("radius")),
						"true" lineTo script("circle" lineTo script("radius"))))
		}
	}
}