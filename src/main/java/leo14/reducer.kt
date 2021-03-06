package leo14

import leo14.parser.coreString
import leo14.reader.charReader
import leo14.reader.reducer
import leo14.untyped.leoStringNonTail

data class Reducer<S, T>(
	val reduced: S,
	val reduceFn: S.(T) -> Reducer<S, T>)

fun <S, T> S.reducer(reduceFn: S.(T) -> Reducer<S, T>) =
	Reducer(this, reduceFn)

fun <S, T> Reducer<S, T>.reduce(token: T): Reducer<S, T> =
	reduced.reduceFn(token)

fun <S1, S2, T> Reducer<S1, T>.mapState(fn: S1.() -> S2): Reducer<S2, T> =
	reduced.fn().reducer { token ->
		reduce(token).mapState(fn)
	}

fun <T> unitReducer(): Reducer<Unit, T> = Unit.reducer { unitReducer() }

fun <T> Reducer<T, Token>.stringCharReducer(fragmentFn: T.() -> Fragment): Reducer<String, Char> =
	charReader().reducer.mapState {
		tokenReducer.reduced.fragmentFn().leoStringNonTail.plus(tokenParser.coreString)
	}

fun <T> Reducer<T, Token>.promptStringCharReducer(promptToFragmentFn: T.() -> Pair<String, Fragment>): Reducer<String, Char> =
	charReader().reducer.mapState {
		tokenReducer.reduced.promptToFragmentFn().let { (prompt, fragment) ->
			prompt + fragment.leoStringNonTail.plus(tokenParser.coreString)
		}
	}
