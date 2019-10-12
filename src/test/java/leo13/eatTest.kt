package leo13

import leo.base.assertEqualTo
import leo.base.indexed
import kotlin.test.Test

class EatTest {
	@Test
	fun intAdd() {
		lazy { 1 } eat eval assertEqualTo 1
		lazy { lazy { 1 } } eat eval eat eval assertEqualTo 1
		intInc eat 1 assertEqualTo 2
		intAdd eat 1 eat 2 assertEqualTo 3
		listAt eat listOf("foo", "bar") eat 1 assertEqualTo "bar"
		intInc dot intInc dot intInc eat 1 assertEqualTo 4

		switch(fn { "0:$it" }, fn { "1:$it" })
			.eat(0 indexed false)
			.assertEqualTo("0:false")
	}
}