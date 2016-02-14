package scriptManager;

import java.io.Serializable;

import javax.script.*;

public class ScriptManager implements Serializable {
	private static ScriptEngine js_engine = new ScriptEngineManager().getEngineByName("JavaScript");
	
	private static Invocable js_invocable = (Invocable) js_engine;
	
	public static void bindArgument(String name, Object obj) {
		js_engine.put(name, obj);
	}
	
	public static void loadScript(String script_name) {
		try {
			js_engine.eval(new java.io.FileReader(script_name));
		} catch (ScriptException se) {
			se.printStackTrace();
		} catch (java.io.IOException iox) {
			iox.printStackTrace();
		}
	}
	
	public static void executeScript() {
		try {
			js_invocable.invokeFunction("update");
		}
		catch(ScriptException se) {
			se.printStackTrace();
		}
		catch(NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
	}
	
	public static void executeScript(Object... args) {
		try {
			js_invocable.invokeFunction("update",args);
		}
		catch(ScriptException se) {
			se.printStackTrace();
		}
		catch(NoSuchMethodException nsme) {
			nsme.printStackTrace();
		}
	}
}
