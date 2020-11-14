package leo22.type

import leo.base.assertEqualTo
import leo22.dsl.*
import kotlin.test.Test

class IsStaticTest {
	@Test
	fun empty() {
		type(struct()).typeIsStatic.assertEqualTo(true)
		type(struct(line(literal(text())))).typeIsStatic.assertEqualTo(false)
		type(struct(line(literal(number())))).typeIsStatic.assertEqualTo(false)
		type(struct(line(field(name(text("foo")), rhs(type(struct())))))).typeIsStatic.assertEqualTo(true)
		type(choice()).typeIsStatic.assertEqualTo(false)
		type(recursive()).typeIsStatic.assertEqualTo(false)
		type(recurse()).typeIsStatic.assertEqualTo(false)
	}
}