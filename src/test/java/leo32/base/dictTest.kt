package leo32.base

import leo.base.assertEqualTo
import kotlin.test.Test

class DictTest {
	@Test
	fun emptyString() {
		emptyDict<String>()
			.put("foo", "FOO")
			.put("bar", "BAR")
			.at("")
			.assertEqualTo(null)
	}

	@Test
	fun zeros0() {
		emptyDict<String>()
			.put("\u0000foo", "FOO")
			.put("\u0000bar", "BAR")
			.at("\u0000foo")
			.assertEqualTo("FOO")
	}

	@Test
	fun zeros1() {
		emptyDict<String>()
			.put("\u0000foo", "FOO")
			.put("\u0000bar", "BAR")
			.at("\u0000bar")
			.assertEqualTo("BAR")
	}

	@Test
	fun zeros2() {
		emptyDict<String>()
			.put("\u0000foo", "FOO")
			.put("\u0000bar", "BAR")
			.at("\u0000")
			.assertEqualTo(null)
	}

	@Test
	fun single() {
		emptyDict<String>()
			.put("foo", "FOO")
			.at("foo")
			.assertEqualTo("FOO")
	}

	@Test
	fun different_first() {
		emptyDict<String>()
			.put("foo", "FOO")
			.put("bar", "BAR")
			.at("foo")
			.assertEqualTo("FOO")
	}

	@Test
	fun different_second() {
		emptyDict<String>()
			.put("foo", "FOO")
			.put("bar", "BAR")
			.at("bar")
			.assertEqualTo("BAR")
	}

	@Test
	fun different_third() {
		emptyDict<String>()
			.put("foo", "FOO")
			.put("bar", "BAR")
			.at("zoo")
			.assertEqualTo(null)
	}

	@Test
	fun shorter() {
		emptyDict<String>()
			.put("foo", "FOO")
			.at("fo")
			.assertEqualTo(null)
	}

	@Test
	fun longer() {
		emptyDict<String>()
			.put("foo", "FOO")
			.at("fooo")
			.assertEqualTo(null)
	}
}