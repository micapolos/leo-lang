package leo22.term

import leo.base.assertEqualTo
import leo22.dsl.*
import kotlin.test.Test

class ScopeTest {
	@Test
	fun native() {
		scope()
			.scopeValue(term(native(text("Hello, world!"))))
			.assertEqualTo(value(native(text("Hello, world!"))))
	}

	@Test
	fun variable() {
		scope()
			.scopePush(value(native(text("Hello, world!"))))
			.scopeValue(term(variable(number(0))))
			.assertEqualTo(value(native(text("Hello, world!"))))
	}

	@Test
	fun abstraction() {
		scope()
			.scopeValue(term(abstraction(term(variable(number(0))))))
			.assertEqualTo(value(function(scope(), term(variable(number(0))))))
	}

	@Test
	fun application() {
		scope()
			.scopeValue(
				term(
					application(
						lhs(term(abstraction(term(variable(number(0)))))),
						rhs(term(native(text("Hello, world!")))))))
			.assertEqualTo(value(native(text("Hello, world!"))))
	}
}