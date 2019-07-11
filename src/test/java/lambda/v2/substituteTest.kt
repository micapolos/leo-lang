package lambda.v2

import leo.base.assertEqualTo
import leo.base.nat
import kotlin.test.Test

class SubstituteTest {
	@Test
	fun substituteArgument() {
		term(argument(0.nat))
			.substitute(0.nat, term(argument(128.nat)))
			.assertEqualTo(term(argument(128.nat)))

		term(argument(10.nat))
			.substitute(0.nat, term(argument(128.nat)))
			.assertEqualTo(term(argument(10.nat)))

		term(argument(10.nat))
			.substitute(10.nat, term(argument(128.nat)))
			.assertEqualTo(term(argument(128.nat)))
	}

	@Test
	fun substituteApplication() {
		application(term(argument(0.nat)), term(argument(10.nat)))
			.substitute(0.nat, term(argument(128.nat)))
			.assertEqualTo(term(application(term(argument(128.nat)), term(argument(10.nat)))))

		application(term(argument(0.nat)), term(argument(10.nat)))
			.substitute(10.nat, term(argument(128.nat)))
			.assertEqualTo(term(application(term(argument(0.nat)), term(argument(128.nat)))))
	}

	@Test
	fun functionApplication() {
		term(function(body(term(argument(0.nat)))))
			.substitute(0.nat, term(argument(128.nat)))
			.assertEqualTo(term(function(body(term(argument(0.nat))))))

		term(function(body(term(argument(1.nat)))))
			.substitute(0.nat, term(argument(128.nat)))
			.assertEqualTo(term(function(body(term(argument(128.nat))))))

		term(function(body(term(argument(0.nat)))))
			.substitute(1.nat, term(argument(128.nat)))
			.assertEqualTo(term(function(body(term(argument(0.nat))))))

		term(function(body(term(argument(1.nat)))))
			.substitute(1.nat, term(argument(128.nat)))
			.assertEqualTo(term(function(body(term(argument(1.nat))))))
	}
}
