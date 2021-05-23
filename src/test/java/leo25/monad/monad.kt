package leo25.monad

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.println
import leo14.*

data class Leo<V>(
	val run: (PersistentMap<String, String>) -> Pair<PersistentMap<String, String>, V>
)

val <T> Leo<T>.get: T
	get() =
		run(persistentMapOf()).second

fun <V> leo(value: V) =
	Leo { map -> map to value }

fun <V, O> Leo<V>.bind(fn: (V) -> Leo<O>): Leo<O> =
	Leo { map ->
		run(map).let { mapToV ->
			fn(mapToV.second).let { leoO ->
				leoO.run(mapToV.first)
			}
		}
	}

fun <V, O> Leo<V>.map(fn: (V) -> O): Leo<O> =
	bind { leo(fn(it)) }

fun rename(name: String): Leo<String> =
	Leo { map ->
		map[name].let { renamed ->
			if (renamed != null) map to renamed
			else "v${map.size}".let { renamed ->
				map.put(name, renamed) to renamed
			}
		}
	}

fun Script.rename(): Leo<Script> =
	when (this) {
		is LinkScript -> link.rename().map { script(it) }
		is UnitScript -> leo(script())
	}

fun ScriptLink.rename(): Leo<ScriptLink> =
	lhs.rename().bind { lhsRename ->
		line.rename().map { lineRename ->
			lhsRename linkTo lineRename
		}
	}

fun ScriptLine.rename(): Leo<ScriptLine> =
	when (this) {
		is FieldScriptLine -> field.rename().map { line(it) }
		is LiteralScriptLine -> leo(line(literal))
	}

fun ScriptField.rename(): Leo<ScriptField> =
	rename(string).bind { name ->
		rhs.rename().map { rhs ->
			name fieldTo rhs
		}
	}

fun main() {
	script(
		"first" lineTo script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))
			)
		),
		"second" lineTo script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))
			)
		)
	)
		.rename()
		.get
		.println
}