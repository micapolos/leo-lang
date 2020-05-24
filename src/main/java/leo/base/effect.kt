package leo.base

data class Effect<out S, out V>(val state: S, val value: V)

infix fun <S, V> S.effect(value: V) = Effect(this, value)

inline fun <A, B, C> Effect<A, B>.bind(fn: A.(B) -> C): C =
	state.fn(value)
