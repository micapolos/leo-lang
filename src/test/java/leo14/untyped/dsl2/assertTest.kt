package leo14.untyped.dsl2

import kotlin.test.Test
import kotlin.test.assertFails

class AssertTest {
	@Test
	fun _test() {
		_run {
			assert {
				zero.gives { zero }
			}
		}

		assertFails {
			_run {
				assert {
					zero.gives { one }
				}
			}
		}
	}
}