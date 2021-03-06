package model.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CommandModel {
	
	private Collection<AomMethod> methods;
	
	public CommandModel() {
		methods = new ArrayList<>();
	}
	
	public void add(AomMethod method) {
		methods.add(method);
	}
	
	public Collection<AomMethod> getMethods() {
		return Collections.unmodifiableCollection(methods);
	}

}
