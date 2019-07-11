package lambda.v2

import leo.base.assertEqualTo
import kotlin.test.Test

class TupleTest {
	@Test
	fun all() {
		nthOf(1, 2).assertEqualTo(fn { fn { arg0(1) } } )
		nthOf(2, 2).assertEqualTo(fn { fn { arg0(0) } } )
		nthOf(1, 2).invoke(id).assertEqualTo(id)
		tuple(2).assertEqualTo(fn { fn { fn { arg0(0)(arg0(2), arg0(1)) } } })
		tuple(2)(nthOf(1, 3), nthOf(2, 3))(nthOf(1, 2)).assertEqualTo(nthOf(1, 4))
	}
}