package leo

import leo.base.*

fun <V> Term<Nothing>.parseStack(parseValue: (Field<Nothing>) -> V?): Stack<V>? =
	structureTermOrNull
		?.fieldStream
		?.run {
			parseValue(first)?.onlyStack?.orNull.fold(nextOrNull) { field ->
				parseValue(field)?.let {
					this?.push(it)
				}
			}
		}
