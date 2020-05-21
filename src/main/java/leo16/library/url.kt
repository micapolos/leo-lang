package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	url
}

val url = compile_ {
	use { reflection.library }
	use { list.library }
	use { number.library }
	use { url.native.library }

	any.text.url
	does {
		string.url.constructor
		invoke { parameter { list { item { url.text.native } } } }
		url
	}

	native.url.read
	does {
		url.get.method
		invoke { parameter { list { item { read.url.native } } } }
		text.read
	}

	native.url.protocol.does {
		protocol.url.native.invoke {
			method { url.get.protocol }
			parameter { empty.list }
		}.text.protocol
	}

	native.url.user.does {
		user.url.native
		invoke {
			method { url.get.user.info }
			parameter { empty.list }
		}
		do_ {
			native.equals_ { null_.native }
			match {
				true_ { none }
				false_ { native.text }
			}.user
		}
	}

	native.url.host.does {
		host.url.native.invoke {
			method { url.get.host }
			parameter { empty.list }
		}.text.host
	}

	native.url.port.does {
		port.url.native.invoke {
			method { url.get.port }
			parameter { empty.list }
		}.int.number
		do_ {
			number.equals_ { (-1).number }
			match {
				true_ { none }
				false_ { number }
			}.port
		}
	}

	native.url.path.does {
		path.url.native.invoke {
			method { url.get.path }
			parameter { empty.list }
		}.text.path
	}

	native.url.query.does {
		query.url.native
		invoke {
			method { url.get.query }
			parameter { empty.list }
		}
		do_ {
			native.equals_ { null_.native }
			match {
				true_ { none }
				false_ { native.text }
			}.query
		}
	}

	native.url.fragment.does {
		fragment.url.native
		invoke {
			method { url.get.ref }
			parameter { empty.list }
		}
		do_ {
			native.equals_ { null_.native }
			match {
				true_ { none }
				false_ { native.text }
			}.fragment
		}
	}

	test { "http://mwiacek.com".text.url.protocol equals_ { "http".text.protocol } }
	test { "http://mwiacek.com".text.url.host equals_ { "mwiacek.com".text.host } }
	test { "http://mwiacek.com".text.url.port equals_ { none.port } }
	test { "http://mwiacek.com:8080".text.url.port equals_ { 8080.number.port } }
	test { "http://mwiacek.com".text.url.path equals_ { "".text.path } }
	test { "http://mwiacek.com/index.html".text.url.path equals_ { "/index.html".text.path } }
	test { "http://mwiacek.com".text.url.query equals_ { none.query } }
	test { "http://mwiacek.com?q=foo".text.url.query equals_ { "q=foo".text.query } }
	test { "http://mwiacek.com".text.url.fragment equals_ { none.fragment } }
	test { "http://mwiacek.com#foo".text.url.fragment equals_ { "foo".text.fragment } }
	test { "http://mwiacek.com".text.url.user equals_ { none.user } }
	test { "http://foo@mwiacek.com".text.url.user equals_ { "foo".text.user } }

	native.url.reflect.does {
		url {
			this_ { reflect.url.protocol }
			this_ { reflect.url.user }
			this_ { reflect.url.host }
			this_ { reflect.url.port }
			this_ { reflect.url.path }
			this_ { reflect.url.query }
			this_ { reflect.url.fragment }
		}
	}

	test {
		"http://mwiacek.com/index.html".text.url.reflect
		equals_ {
			url {
				protocol { "http".text }
				user { none }
				host { "mwiacek.com".text }
				port { none }
				path { "/index.html".text }
				query { none }
				fragment { none }
			}
		}
	}

	native.url.browse
	does {
		"java.awt.Desktop".text.name.class_
		method {
			name { "getDesktop".text }
			parameter { empty.list }
		}
		invoke { parameter { empty.list } }
		invoke {
			"java.awt.Desktop".text.name.class_
			method {
				name { "browse".text }
				parameter { list { item { "java.net.URI".text.name.class_ } } }
			}
			parameter {
				list {
					item {
						browse.url.native
						invoke {
							url.class_
							method {
								name { "toURI".text }
								parameter { empty.list }
							}
							parameter { empty.list }
						}
					}
				}
			}
		}
		do_ { nothing }
	}
}