package handler;

import java.util.LinkedHashMap;
import java.util.Map;

public class HandlerManager {
	
	private Map<HandlerType<?>, Handler<?>> handlerTypeToHandler;
	
	public HandlerManager() {
		handlerTypeToHandler = new LinkedHashMap<>();
	}
	
	public <T> void handle(HandlerType<T> handlerType, T value) {
		Handler<?> handler = handlerTypeToHandler.computeIfAbsent(handlerType, HandlerType::build);
		@SuppressWarnings("unchecked") //Type forced to be cosistent with params
		Handler<T> handlerCast = (Handler<T>) handler;
		handlerCast.handle(value);
	}

}
