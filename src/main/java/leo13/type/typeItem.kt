package leo13.type

import leo.base.notNullOrError
import leo13.ObjectScripting
import leo13.itemName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class TypeItem : ObjectScripting() {
	override fun toString() = scriptingLine.toString()

	override val scriptingLine: ScriptLine
		get() = itemName lineTo when (this) {
			is LineTypeItem -> line.scriptingLine.rhs
			is RecurseTypeItem -> script(recurse.scriptingLine)
		}

	val unexpandedLineOrNull get() = (this as? LineTypeItem)?.unexpandedLine
	val recurseOrNull get() = (this as? RecurseTypeItem)?.recurse
	val line get() = unexpandedLineOrNull!!.notNullOrError("recurse")

	fun expand(rootOrNull: RecurseRoot? = null): TypeItem =
		when (this) {
			is LineTypeItem -> item(line.expand(rootOrNull))
			is RecurseTypeItem ->
				rootOrNull
					.notNullOrError("recurse")
					.let { root ->
						if (recurse == root.recurse) item(root.line)
						else this
					}
		}

	fun contains(item: TypeItem, traceOrNull: TypeTrace?): Boolean =
		when (this) {
			is LineTypeItem ->
				when (item) {
					is LineTypeItem -> line.contains(item.line, traceOrNull)
					is RecurseTypeItem -> false
				}
			is RecurseTypeItem ->
				when (item) {
					is LineTypeItem -> traceOrNull.orNullPlus(item.line).run { line.contains(item.line, this) }
					is RecurseTypeItem -> recurse == item.recurse
				}
		}

	val isStatic: Boolean
		get() =
			when (this) {
				is LineTypeItem -> line.isStatic
				is RecurseTypeItem -> false
			}
}

data class LineTypeItem(val unexpandedLine: TypeLine) : TypeItem()
data class RecurseTypeItem(val recurse: Recurse) : TypeItem()

fun item(line: TypeLine): TypeItem = LineTypeItem(line)
fun item(recurse: Recurse): TypeItem = RecurseTypeItem(recurse)

fun item(name: String) = item(name.typeLine)