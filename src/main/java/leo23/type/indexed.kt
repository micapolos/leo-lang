package leo23.type

import leo.base.notNullIf
import leo13.indexed
import leo13.mapFirst

fun Types.indexedTypeOrNull(name: String): IndexedValue<Type>? =
	indexed.mapFirst {
		let { indexed ->
			notNullIf(indexed.value.name == name) {
				indexed
			}
		}
	}