package leo32.runtime

import leo.base.assertEqualTo
import kotlin.test.Test

class TermBuilderTest {

	@Test
	fun fnSyntax() {
		term {
			zero.gives { one }
			zero
		}.assertEqualTo(term { one })
	}

	@Test
	fun fnSyntax2() {
		term {
			circle {
				radius { int(10) }
				center {
					x { int(12) }
					y { int(15) }
				}
			}.circle.center.x
		}.assertEqualTo(term { int(12) })
	}
}