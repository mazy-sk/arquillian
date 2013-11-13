package org.arquillian.example;

import java.io.PrintStream;

import javax.inject.Inject;

public class Greeter {

	private PhraseBuilder builder;
	
	@Inject
	public Greeter(PhraseBuilder builder) {
		this.builder = builder;
	}
	
	public void greet(PrintStream to, String name) {
		to.println(createGreeting(name));
	}

	public String createGreeting(String name) {
		return builder.buildPhrase("hello", name);
	}
}
