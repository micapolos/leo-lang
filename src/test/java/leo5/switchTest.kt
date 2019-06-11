package leo5

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class SwitchTest {
	@Test
	fun contains() {
		switch().contains(value()).assertEqualTo(false)

		switch(name("zero") caseTo type(empty), name("one") caseTo type(empty))
			.contains(value("zero" lineTo value()))
			.assertEqualTo(true)
		switch(name("zero") caseTo type(empty), name("one") caseTo type(empty))
			.contains(value("one" lineTo value()))
			.assertEqualTo(true)
		switch(name("zero") caseTo type(empty), name("one") caseTo type(empty))
			.contains(value("two" lineTo value()))
			.assertEqualTo(false)
	}
}