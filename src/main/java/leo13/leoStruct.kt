package leo13

import leo13.script.script
import leo9.Stack
import leo9.map
import leo9.stack

open class LeoStruct private constructor(val name: String, val fields: Stack<Scriptable>) : LeoObject() {
	constructor(name: String, vararg fields: Scriptable) : this(name, stack(*fields))

	override val scriptableName get() = name
	override val scriptableBody get() = fields.map { scriptableLine }.script
}
