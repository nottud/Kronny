package handler;

import java.util.function.Supplier;

public class HandlerType<T> {
	
	private Supplier<Handler<T>> handlerFactory;

	public HandlerType(Supplier<Handler<T>> handlerFactory) {
		this.handlerFactory = handlerFactory;
	}
	
	public Handler<T> build(){
		return handlerFactory.get();
	}

}
