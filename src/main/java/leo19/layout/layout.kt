package leo19.layout

import leo13.Stack

data class Layout(
	val isDynamic: Boolean,
	val isPossible: Boolean,
	val body: LayoutBody
)

sealed class LayoutBody
data class StructLayoutBody(val size: Int, val fieldStack: Stack<LayoutField>) : LayoutBody()
data class ChoiceLayoutBody(val size: Int, val fieldStack: Stack<LayoutField>) : LayoutBody()
data class ArrowLayoutBody(val lhs: Layout, val rhs: Layout) : LayoutBody()
data class RecursiveLayoutBody(val layout: Layout) : LayoutBody()
data class RecurseLayoutBody(val depth: Int) : LayoutBody()

data class LayoutField(val index: Int, val name: String, val value: Layout)