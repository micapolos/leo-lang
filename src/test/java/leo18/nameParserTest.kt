package leo18

import leo.base.assertEqualTo
import kotlin.test.Test

class NameParserTest {
	@Test
	fun test() {
		nameBuilder(Name("ba"))
			.read('r')
			.run { this as Builder.Result.Success }
			.builder
			.built
			.assertEqualTo(Name("bar"))

		nameBuilder(Name("ba"))
			.read(' ')
			.run { this as Builder.Result.Error }
			.error
			.assertEqualTo(NotLetter(' '))
	}
}
