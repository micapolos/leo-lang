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
		map.get("").assertEqualTo("empty")
		map.get("foo").assertEqualTo("zoo")
		map.get("bar").assertEqualTo("zar")
		map.get("foobar").assertEqualTo("zoozar")
		map.get("goo").assertEqualTo(null)
	}

	@Test
	fun set() {
		val map = testMap.set("foo", "moo")
		map.get("").assertEqualTo("empty")
		map.get("foo").assertEqualTo("moo")
		map.get("bar").assertEqualTo("zar")
		map.get("foobar").assertEqualTo("zoozar")
		map.get("goo").assertEqualTo(null)
	}

	@Test
	fun bulkTest() {
		var map = emptyMap<Byte, String>(Byte::bitStream)
		for (byte in Byte.MIN_VALUE..Byte.MAX_VALUE) {
			map = map.set(byte.clampedByte, byte.toString())
		}
		for (byte in Byte.MIN_VALUE..Byte.MAX_VALUE) {
			map.get(byte.clampedByte).assertEqualTo(byte.toString())
		}
	}
}