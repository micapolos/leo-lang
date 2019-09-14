package leo13.locator

import leo13.trace
import leo13.traced

data class Locator<out S>(
	val state: S,
	val location: Location)

fun <S> S.locator(location: Location = location()): Locator<S> =
	Locator(this, location)

fun <S> Locator<S>.plus(char: Char, fn: S.(Char) -> S): Locator<S> =
	trace {
		location.scriptLine
	}.traced {
		state.fn(char).run { locator(location.plus(char)) }
	}
