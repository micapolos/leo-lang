package leo.base

data class Effect<out S, out V>(val state: S, val value: V)

infix fun <S, V> S.effect(value: V) = Effect(this, value)

inline fun <S, V1, V2> Effect<S, V1>.bind(fn: S.(V1) -> V2): V2 =
	state.fn(value)

inline fun <S, V> Effect<S, V>.updateState(fn: S.() -> S): Effect<S, V> =
	state.fn() effect value

infix fun <S, V1, V2> Effect<S, V1>.set(value: V2): Effect<S, V2> = state effect value

infix fun <S, V1, V2> Effect<S, V1>.update(fn: (V1) -> V2): Effect<S, V2> = set(fn(value))
