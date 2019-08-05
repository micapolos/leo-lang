package leo13

import leo.base.notNullIf
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class Types(val entryStack: Stack<TypeEntry>)
data class TypeEntry(val link: TypeLink, val type: Type)

val Stack<TypeEntry>.types get() = Types(this)
fun Types.plus(entry: TypeEntry) = entryStack.push(entry).types
fun types(vararg entries: TypeEntry) = stack(*entries).types

fun Types.typeOrNull(link: TypeLink): Type? =
	entryStack.mapFirst { typeOrNull(link) }

fun TypeEntry.typeOrNull(link: TypeLink): Type? =
	notNullIf(this.link == link) { type }
