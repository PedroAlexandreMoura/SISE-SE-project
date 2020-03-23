package mbway;

public class MbwayException extends Exception {
	private String message;

	public MbwayException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
