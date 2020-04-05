package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val url = library_ {
	text.fetch
	does {
		text("leo.base.UrlKt").class_.native
		static.invoke {
			text("get")
			it { fetch.text.string.native }
		}.text
	}
}

fun main() = run_ { url() }