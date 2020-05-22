package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	base
}

val base = compile_ {
	export { core }
	export { help }
	export { leo }
	export { boolean }
	export { printing }
	export { number }
	export { byte }
	export { int }
	export { text }
	export { character }
	export { list }
	export { url }
	export { animation }
}
