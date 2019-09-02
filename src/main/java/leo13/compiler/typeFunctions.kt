package leo13.compiler

import leo.base.notNullIf
import leo13.LeoObject
import leo13.script.asScript
import leo13.type.Type
import leo13.type.contains
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class TypeFunctions(val stack: Stack<TypeFunction>) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "types"
	override val scriptableBody get() = stack.asScript { scriptableLine }
}

val Stack<TypeFunction>.typeFunctions get() = TypeFunctions(this)
fun typeFunctions() = stack<TypeFunction>().typeFunctions
fun typeFunctions(vararg types: TypeFunction) = stack(*types).typeFunctions
fun TypeFunctions.plus(type: TypeFunction) = stack.push(type).typeFunctions

fun TypeFunctions.resolve(type: Type): Type =
	stack.mapFirst {
		notNullIf(this.type.contains(type)) {
			this.type
		}
	} ?: type

fun TypeFunctions.typeOrNull(name: String): Type? =
	stack.mapFirst {
		notNullIf(this.name == name) {
			this.type
		}
	}
