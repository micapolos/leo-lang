package leo21.evaluated

import com.sun.jdi.Value
import java.lang.reflect.Type

data class Evaluated(
	val context: Context,
	val type: Type,
	val value: Value
)