package leo5

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class DispatcherTest {
	private val dictionary
		get() = dictionary(
			"zero" lineTo body(value(line("zero", value()))),
			"one" lineTo body(value(line("one", value()))))

	@Test
	fun at() {
		dictionary.at("zero").assertEqualTo(body(value("zero" lineTo value())))
		dictionary.at("one").assertEqualTo(body(value("one" lineTo value())))
		assertFails { dictionary.at("two") }
	}
}