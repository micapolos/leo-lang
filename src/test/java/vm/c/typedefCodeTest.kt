package vm.c

import leo.base.assertEqualTo
import vm.int
import vm.of
import vm.struct
import kotlin.test.Test

class TypedefCodeTest {
	@Test
	fun code() {
		struct(
			"circle" of struct(
				"radius" of int,
				"center" of struct(
					"point" of struct(
						"x" of int,
						"y" of int))))
			.defCode
			.assertEqualTo("""
					typedef struct { int x; int y; } t0;
					typedef struct { t0 point; } t1;
					typedef struct { int radius; t1 center; } t2;
					typedef struct { t2 circle; } t3;
				
					""".trimIndent())
	}
}