package lambda.lib

import lambda.invoke
import leo.base.assertEqualTo
import kotlin.test.Test

class BranchTest {
	@Test
	fun test() {
		branch0(zero).assertEqualTo(branch0(zero))
		branch0(one).assertEqualTo(branch0(one))
		branch1(zero).assertEqualTo(branch1(zero))
		branch1(one).assertEqualTo(branch1(one))

		branch0(zero).switch(not, id).assertEqualTo(one)
		branch0(one).switch(not, id).assertEqualTo(zero)
		branch1(zero).switch(not, id).assertEqualTo(zero)
		branch1(one).switch(not, id).assertEqualTo(one)
	}
}