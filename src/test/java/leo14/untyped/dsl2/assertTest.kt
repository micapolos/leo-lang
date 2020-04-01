package leo14.untyped.dsl2

import kotlin.test.Test
import kotlin.test.assertFails

class AssertTest {
	@Test
	fun test_() {
		run_ {
			assert {
				zero.equals_ { zero }
			}
		}

		assertFails {
			run_ {
				assert {
					zero.equals_ { one }
				}
			}
		}
	}
}