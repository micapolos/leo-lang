package leo13.type

import leo.base.fold
import leo.base.notNullIf
import leo13.*
import leo13.compiler.NameTrace
import leo13.compiler.nameTrace
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class Type : ObjectScripting() {
	override fun toString() = scriptingLine.toString()

	override val scriptingLine: ScriptLine
		get() = typeName lineTo
			when (this) {
				is EmptyType -> empty.scriptingLine.rhs
				is LinkType -> link.scriptingLine.rhs
				is OptionsType -> script(options.scriptingLine)
				is ArrowType -> arrow.scriptingLine.rhs
			}

	val isEmpty get() = this is EmptyType
	val linkOrNull get() = (this as? LinkType)?.link
	val optionsOrNull get() = (this as? OptionsType)?.options
	val arrowOrNull get() = (this as? ArrowType)?.arrow

	fun plus(item: TypeItem) =
		type(this linkTo item)

	fun plus(line: TypeLine) =
		plus(item(line))

	fun typeOrNull(name: String): Type? =
		when (this) {
			is EmptyType -> null
			is LinkType -> link.typeOrNull(name)
			is OptionsType -> null
			is ArrowType -> notNullIf(name == functionName) { type(arrow) }
		}

	fun setLineRhsOrNull(line: TypeLine): Type? =
		linkOrNull?.setLineRhsOrNull(line)?.let { type(it) }

	fun getOrNull(name: String): Type? =
		linkOrNull?.getOrNull(name)

	fun setOrNull(line: TypeLine): Type? =
		linkOrNull?.setOrNull(line)?.let { type(it) }

	val previousOrNull: Type?
		get() =
			linkOrNull?.lhs

	val contentOrNull: Type?
		get() =
			linkOrNull?.item?.line?.rhs

	val onlyNameOrNull: String?
		get() =
			linkOrNull?.onlyLineOrNull?.onlyNameOrNull

	fun leafPlusOrNull(type: Type): Type? =
		when (this) {
			is EmptyType -> type
			is LinkType -> link.leafPlusOrNull(type)?.let { type(it) }
			else -> null
		}

	fun leafNameTraceOrNull(acc: NameTrace = nameTrace()): NameTrace? =
		when (this) {
			is EmptyType -> acc
			is LinkType -> link.item.unexpandedLineOrNull?.let { line ->
				line.rhs.leafNameTraceOrNull(acc.plus(line.name))
			}
			else -> null
		}

	val beginOptionsOrNull: Options?
		get() =
			when (this) {
				is EmptyType -> options()
				is OptionsType -> options
				else -> null
			}

	fun expand(rootOrNull: RecurseRoot? = null): Type =
		when (this) {
			is EmptyType -> this
			is LinkType -> type(link.expand(rootOrNull))
			is OptionsType -> type(options.expand(rootOrNull))
			is ArrowType -> this
		}

	fun contains(type: Type, trace: TypeTrace? = null): Boolean =
		when (this) {
			is EmptyType -> type is EmptyType
			is LinkType -> type is LinkType && link.contains(type.link, trace)
			is OptionsType -> options.contains(type, trace)
			is ArrowType -> type is ArrowType && arrow.contains(type.arrow)
		}

	val isStatic: Boolean
		get() =
			when (this) {
				is EmptyType -> true
				is LinkType -> link.isStatic
				is OptionsType -> options.isStatic
				is ArrowType -> arrow.isStatic
			}
}

data class EmptyType(val empty: Empty) : Type() {
	override fun toString() = super.toString()
}

data class LinkType(val link: TypeLink) : Type() {
	override fun toString() = super.toString()
}

data class OptionsType(val options: Options) : Type() {
	override fun toString() = super.toString()
}

data class ArrowType(val arrow: TypeArrow) : Type() {
	override fun toString() = super.toString()
}

fun type() = type(empty)
fun type(empty: Empty): Type = EmptyType(empty)
fun type(link: TypeLink): Type = LinkType(link)
fun type(options: Options): Type = OptionsType(options)
fun type(arrow: TypeArrow): Type = ArrowType(arrow)

fun type(name: String) = type(name.typeLine)
fun type(recurse: Recurse) = type(item(recurse))

fun type(item: TypeItem, vararg items: TypeItem) =
	type(type() linkTo item).fold(items) { plus(it) }

fun type(line: TypeLine, vararg lines: TypeLine) =
	type(type() linkTo line).fold(lines) { plus(it) }

fun type(name: String, vararg names: String) =
	type(type() linkTo name.typeLine).fold(names) { plus(it.typeLine) }

val Script.type
	get() =
		type().fold(lineStack.reverse) { plus(it.typeLine) }
