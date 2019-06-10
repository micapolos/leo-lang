package leo5

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class DispatcherTest {
	private val dictionary
		get() = dispatcher(
			"zero" to body(value(script(line("zero")))),
			"one" to body(value(script(line("one")))))

	@Test
	fun at() {
		dictionary.at("zero").assertEqualTo(body(value(script(line("zero")))))
		dictionary.at("one").assertEqualTo(body(value(script(line("one")))))
		assertFails { dictionary.at("two") }
	}
}