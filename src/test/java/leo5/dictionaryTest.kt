package leo5

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class DispatcherTest {
	private val dictionary
		get() = dispatcher(
			"zero" to function(value(script(line("zero")))),
			"one" to function(value(script(line("one")))))

	@Test
	fun at() {
		dictionary.at("zero").assertEqualTo(function(value(script(line("zero")))))
		dictionary.at("one").assertEqualTo(function(value(script(line("one")))))
		assertFails { dictionary.at("two") }
	}
}