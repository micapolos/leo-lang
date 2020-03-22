package leo14.untyped.dsl

import org.junit.Test

class EvalTest {
	@Test
	fun main() {
		_test(
			zero(),
			plus(one()),
			check(
				zero(),
				plus(one())),

			number(2),
			plus(number(3)),
			check(number(5)),

			number(5),
			minus(number(3)),
			check(number(2)),

			number(5),
			times(number(3)),
			check(number(15)),

			minus(number(2)),
			check(number(-2)),

			number(2),
			plus(
				number(3),
				times(number(4))),
			check(number(14)),

			text("Hello, "),
			plus(text("world!")),
			check(text("Hello, world!")),

			vector(
				x(number(10)),
				y(number(20))),
			x(),
			check(x(number(10))),

			vector(
				x(number(10)),
				y(number(20))),
			y(),
			check(y(number(20))),

			vector(
				x(number(10)),
				y(number(20))),
			z(),
			check(
				vector(
					x(number(10)),
					y(number(20))),
				z()),

			x(number(10)),
			number(),
			check(number(10)),

			x(foo()),
			number(),
			check(
				x(foo()),
				number()),

			x(text("foo")),
			text(),
			check(text("foo")),

			x(foo()),
			text(),
			check(
				x(foo()),
				text()),

			x(),
			gives(number(10)),
			check(),

			x(),
			gives(number(10)),
			x(),
			check(number(10)),

			x(),
			gives(
				number(2),
				plus(number(3))),
			x(),
			check(number(5)),

			number(10),
			_is(x()),
			check(),

			number(10),
			_is(x()),
			x(),
			check(number(10)),

			number(),
			increment(),
			does(
				given(),
				number(),
				plus(number(1))),
			check(),

			number(),
			increment(),
			does(
				given(),
				number(),
				plus(number(1))),
			number(2),
			increment(),
			check(number(3)))
	}
}