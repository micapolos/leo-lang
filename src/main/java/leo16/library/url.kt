package leo16.library

import leo15.dsl.*
import leo16.dictionary_
import leo16.run_

fun main() = run_ { url }

val url = dictionary_ {
	reflection.import

	import {
		dictionary {
			url.class_.is_ { "leo.base.UrlKt".text.name.class_ }
			url.get.method.is_ {
				url.class_
				method {
					name { "get".text }
					parameter { list { "java.lang.String".text.name.class_ } }
				}
			}
		}
	}

	any.text.url.get
	gives {
		url.get.method
		invoke { parameter { list { given.get.url.text.native } } }
		text
	}
}