package vm.c

import leo.base.assertEqualTo
import vm.choice
import vm.float
import vm.get
import vm.int
import vm.of
import vm.string
import vm.struct
import kotlin.test.Test

class TypeCodeTest {
	@Test
	fun code() {
		int.code.assertEqualTo("int")
		float.code.assertEqualTo("float")
		string.code.assertEqualTo("char *")

		struct("width" of float, "height" of float)
			.code
			.assertEqualTo("struct { float width; float height; }")

		choice("float" of float, "int" of int)
			.code
			.assertEqualTo("struct { int i; union { float float; int int; } }")
		int[4].code.assertEqualTo("int[4]")
	}
}