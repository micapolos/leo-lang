package leo16.reducers

import leo14.Reducer
import leo16.base.Text
import leo16.base.emptyText
import leo16.base.string

fun reduceString(fn: Reducer<Text, Char>.() -> Reducer<Text, Char>): String =
	emptyText.charReducer.fn().reduced.string