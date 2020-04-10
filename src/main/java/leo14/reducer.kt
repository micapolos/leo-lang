package leo14

data class Reducer<S, T>(
	val state: S,
	val reduceFn: S.(T) -> Reducer<S, T>)

fun <S, T> S.reducer(reduceFn: S.(T) -> Reducer<S, T>) =
	Reducer(this, reduceFn)

fun <S, T> Reducer<S, T>.reduce(token: T) =
	state.reduceFn(token)

fun <S1, S2, T> Reducer<S1, T>.mapState(fn: S1.() -> S2): Reducer<S2, T> =
	state.fn().reducer { token ->
		reduce(token).mapState(fn)
	}

fun <T> unitReducer(): Reducer<Unit, T> = Unit.reducer { unitReducer() }
