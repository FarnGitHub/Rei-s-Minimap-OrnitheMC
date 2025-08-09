package reifnsk.minimap;

public class MinimapException extends RuntimeException {
	public MinimapException() {
	}

	public MinimapException(String message, Throwable cause) {
		super(message, cause);
	}

	public MinimapException(String message) {
		super(message);
	}

	public MinimapException(Throwable cause) {
		super(cause);
	}
}
