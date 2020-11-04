package vm.c

import leo.base.assertEqualTo
import vm.array
import vm.get
import vm.of
import vm.value
import kotlin.test.Test

class ExpressionCodeTest {
	@Test
	fun code() {
		value(123).bodyCode.assertEqualTo("int v0 = 123; return v0;")
		array().bodyCode.assertEqualTo("void[0] v0 = {  }; return v0;")
		array(value(10), value(20))
			.bodyCode
			.assertEqualTo("int v0 = 10; int v1 = 20; int[2] v2 = { v0, v1 }; return v2;")
		value("width" of value(10), "height" of value(20))
			.bodyCode
			.assertEqualTo("int v0 = 10; int v1 = 20; struct { int width; int height; } v2 = { v0, v1 }; return v2;")
		array(value(10), value(20))[value(0)]
			.bodyCode
			.assertEqualTo("int v0 = 10; int v1 = 20; int[2] v2 = { v0, v1 }; int v3 = 0; int v4 = v2[v3]; return v4;")
	}
}