package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.emptyFragment
import kotlin.test.Test

class FragmentTest {
	@Test
	fun empty() {
		emptyLeo.fragment.assertEqualTo(emptyFragment)
	}
}