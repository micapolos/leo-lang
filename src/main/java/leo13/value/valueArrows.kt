package leo13.value

import leo13.LeoObject
import leo13.base.List
import leo13.base.list
import leo13.base.mapFirst
import leo13.base.plus

data class ValueArrows(val list: List<ValueArrow>) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "arrows"
	override val scriptableBody get() = list.scriptableBody
}

fun arrows(list: List<ValueArrow>): ValueArrows = ValueArrows(list)
fun arrows(vararg arrows: ValueArrow): ValueArrows = arrows(list(*arrows))
fun ValueArrows.plus(arrow: ValueArrow): ValueArrows = arrows(list.plus(arrow))
fun ValueArrows.at(value: Value): Value? = list.mapFirst { at(value) }
