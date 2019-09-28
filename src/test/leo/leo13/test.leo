define
	processor
	contains: function
		byte process
		gives: processor

	void processor
	of: processor
	gives: processor: function
		byte process
		gives: void processor

void processor
do: process: byte: zero
do: process: byte: one
