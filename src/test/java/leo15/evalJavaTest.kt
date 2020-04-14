package leo15

import leo14.untyped.dsl2.*
import kotlin.test.Test

class EvalJavaTest {
	@Test
	fun javaType() {
		script_ {
			int.class_.java
		}.evals {
			class_ { java_(Integer.TYPE) }
		}
	}

	@Test
	fun javaType_missing() {
		script_ {
			missing.class_.java
		}.evals {
			java { class_ { missing } }
		}
	}

	@Test
	fun javaClass() {
		script_ {
			text("java.lang.String").name.class_.java
		}.evals {
			class_ { java_(java.lang.String::class.java) }
		}
	}

	@Test
	fun javaClass_missing() {
		script_ {
			text("Missing").name.class_.java
		}.evals {
			class_ { java_(ClassNotFoundException("Missing")) }
		}
	}

	@Test
	fun javaClassField() {
		script_ {
			text("java.awt.Point").name.class_.java
			field { text("x").name }
		}.evals {
			field { java_(java.awt.Point::class.java.getField("x")) }
		}
	}

	@Test
	fun javaClassField_missing() {
		script_ {
			text("java.awt.Point").name.class_.java
			field { text("missing").name }
		}.evals {
			field { java_(NoSuchFieldException("missing")) }
		}
	}
}