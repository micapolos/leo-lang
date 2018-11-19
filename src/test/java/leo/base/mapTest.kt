package leo.base

import kotlin.test.Test

class MapTest {
	val testMap =
		emptyMap<String, String>(String::bitStreamOrNull)
			.set("", "empty")
			.set("foo", "zoo")
			.set("bar", "zar")
			.set("foobar", "zoozar")

	@Test
	fun get() {
		val map = testMap
		map.get("").assertEqualTo("empty".the)
		map.get("foo").assertEqualTo("zoo".the)
		map.get("bar").assertEqualTo("zar".the)
		map.get("foobar").assertEqualTo("zoozar".the)
		map.get("goo").assertEqualTo(null)
	}

	@Test
	fun set() {
		val map = testMap.set("foo", "moo")
		map.get("").assertEqualTo("empty".the)
		map.get("foo").assertEqualTo("moo".the)
		map.get("bar").assertEqualTo("zar".the)
		map.get("foobar").assertEqualTo("zoozar".the)
		map.get("goo").assertEqualTo(null)
	}

	@Test
	fun bulkTest() {
		val maxKey = 255
		var map = emptyMap<String, Int>(String::bitStreamOrNull)
		for (key in 0..maxKey) {
			map = map.set(key.toString(), key)
		}
		for (key in 0..maxKey) {
			map.get(key.toString()).assertEqualTo(key.the)
		}
	}
}