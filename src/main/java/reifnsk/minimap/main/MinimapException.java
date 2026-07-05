package reifnsk.minimap.main;

public class MinimapException extends RuntimeException {
	public MinimapException() {
	}

	public MinimapException(String string1, Throwable throwable2) {
		super(string1, throwable2);
	}

	public MinimapException(String string1) {
		super(string1);
	}

	public MinimapException(Throwable throwable1) {
		super(throwable1);
	}
}
