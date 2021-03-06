package leo13.atom

import leo.base.updateIfNotNull
import leo13.ObjectScripting
import leo13.expressionName
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script

data class Expression(val lhs: Expression?, val op: Op) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = expressionName lineTo bodyScript

	val bodyScript: Script
		get() =
			script(op.scriptingOpLine)
				.updateIfNotNull(lhs) { plus(bodyScript) }
}
