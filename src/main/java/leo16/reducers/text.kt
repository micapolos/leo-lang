package leo16.reducers

import leo14.Reducer
import leo14.reducer
import leo16.base.Text
import leo16.base.plus

val Text.charReducer: Reducer<Text, Char>
	get() =
		reducer { char ->
			plus(char).charReducer
		}
