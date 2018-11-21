package leo.base

import kotlin.test.Test

fun <V, P> Parse<V, P>?.assertParsedAndRest(parsed: P, rest: Stream<V>?) {
	assertNotNull
	(this!!.parsed to this.streamOrNull?.stack).assertEqualTo(parsed to rest?.stack)
}

class ParseTest {
	@Test
	fun bind() {
		stream(1, 2, 3)
			.parseItself
			.bind { int1 ->
				parseItself.bind { int2 ->
					parsed(stack(int1, int2))
				}
			}
			.assertParsedAndRest(stack(1, 2), 3.onlyStream)
	}
}