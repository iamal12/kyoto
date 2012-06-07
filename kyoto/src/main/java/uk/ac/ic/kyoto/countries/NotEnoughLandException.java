package uk.ac.ic.kyoto.countries;

public class NotEnoughLandException extends RuntimeException {
	private static final long serialVersionUID = -6705918025569748287L;
	
	public NotEnoughLandException() {
		super("Not enough arable land for the investment");
	}
}