package leo13.base

import leo13.LeoObject
import leo13.ScriptException
import leo13.Scriptable
import leo13.fail
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script

abstract class WriterObject<I : Scriptable> : LeoObject(), Writer<I> {
	override fun toString() = super.toString()

	final override val scriptableName get() = "writer"
	final override val scriptableBody get() = script(writerScriptableLine)

	abstract val writerScriptableName: String
	abstract val writerScriptableBody: Script
	val writerScriptableLine get() = writerScriptableName lineTo writerScriptableBody

	final override fun write(value: I): Writer<I> =
		try {
			writerWrite(value)
		} catch (se: ScriptException) {
			fail(script(scriptableLine)
				.plus("write" lineTo script(value.scriptableLine))
				.plus("reason" lineTo se.script))
		}

	final override val finishWriting: Unit
		get() =
			try {
				writerFinishWriting
			} catch (se: ScriptException) {
				fail(script(scriptableLine)
					.plus("finish" lineTo script("writing"))
					.plus("reason" lineTo se.script))
			}

	abstract fun writerWrite(value: I): Writer<I>
	abstract val writerFinishWriting: Unit
}
