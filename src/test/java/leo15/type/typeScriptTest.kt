package leo15.type

import leo14.invoke
import leo14.leo
import kotlin.test.Test

class TypeScriptTest {
	@Test
	fun scriptType() {
		leo().assertTypeSerializes
		leo(10).assertTypeSerializes
		leo("foo").assertTypeSerializes
		leo("foo"()).assertTypeSerializes
	}

	@Test
	fun choice() {
		leo("choice"("true"(), "false"()))
			.assertTypeSerializes
	}

	@Test
	fun builtin() {
		leo("number"()).assertTypeSerializes
		leo("text"()).assertTypeSerializes
		leo("java"()).assertTypeSerializes
	}

	@Test
	fun repeating() {
		leo("repeating"("number"())).assertTypeSerializes
	}

	@Test
	fun recursive() {
		leo("recursive"("number"())).assertTypeSerializes
		leo("recurse"()).assertTypeSerializes
	}
}