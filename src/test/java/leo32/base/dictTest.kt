package leo32.base

import leo.base.assertEqualTo
import kotlin.test.Test

class DictTest {
	@Test
	fun emptyString() {
		emptyDict
			.with("foo")
			.with("bar")
			.at("")
			.assertEqualTo(2)
	}

	@Test
	fun zeros0() {
		emptyDict
			.with("\u0000foo")
			.with("\u0000bar")
			.at("\u0000foo")
			.assertEqualTo(0)
	}

	@Test
	fun zeros1() {
		emptyDict
			.with("\u0000foo")
			.with("\u0000bar")
			.at("\u0000bar")
			.assertEqualTo(1)
	}

	@Test
	fun zeros2() {
		emptyDict
			.with("\u0000foo")
			.with("\u0000bar")
			.at("\u0000")
			.assertEqualTo(2)
	}

	@Test
	fun single() {
		emptyDict
			.at("foo")
			.assertEqualTo(0)
	}

	@Test
	fun same() {
		emptyDict
			.with("foo")
			.at("foo")
			.assertEqualTo(0)
	}

	@Test
	fun different_first() {
		emptyDict
			.with("foo")
			.with("bar")
			.at("foo")
			.assertEqualTo(0)
	}

	@Test
	fun different_second() {
		emptyDict
			.with("foo")
			.with("bar")
			.at("bar")
			.assertEqualTo(1)
	}

	@Test
	fun different_third() {
		emptyDict
			.with("foo")
			.with("bar")
			.at("zoo")
			.assertEqualTo(2)
	}

	@Test
	fun shorter() {
		emptyDict
			.with("foo")
			.at("fo")
			.assertEqualTo(1)
	}

	@Test
	fun longer() {
		emptyDict
			.with("foo")
			.at("fooo")
			.assertEqualTo(1)
	}
}