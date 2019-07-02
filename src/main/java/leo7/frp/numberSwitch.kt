package leo7.frp

data class NumberSwitch(
	val selector: BoolSelector,
	val ifTrue: NumberIfTrue,
	val ifFalse: NumberIfFalse)

fun BoolSelector.switch(ifTrue: NumberIfTrue, ifFalse: NumberIfFalse) =
	NumberSwitch(this, ifTrue, ifFalse)
