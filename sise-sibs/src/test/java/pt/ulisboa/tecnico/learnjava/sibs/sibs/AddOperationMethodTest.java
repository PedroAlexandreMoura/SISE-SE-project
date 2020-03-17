package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class AddOperationMethodTest {
	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;

	private Sibs sibs;

	@Before
	public void setUp() {
		this.sibs = new Sibs(3, new Services());
	}

	@Test
	public void success() throws OperationException, SibsException {
		int position = this.sibs.addOperation(Operation.OPERATION_TRANSFER, SOURCE_IBAN, TARGET_IBAN, VALUE);

		Operation operation = this.sibs.getOperation(position);

		assertEquals(1, this.sibs.getNumberOfOperations());
		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(VALUE, operation.getValue());
	}

	@Test
	public void successWithDelete() throws OperationException, SibsException {
		int position = this.sibs.addOperation(Operation.OPERATION_TRANSFER, SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.removeOperation(position);
		position = this.sibs.addOperation(Operation.OPERATION_PAYMENT, null, TARGET_IBAN, 200);

		Operation operation = this.sibs.getOperation(position);

		assertEquals(3, this.sibs.getNumberOfOperations());
		assertEquals(Operation.OPERATION_PAYMENT, operation.getType());
		assertEquals(200, operation.getValue());
	}

	@Test(expected = SibsException.class)
	public void failIsFull() throws OperationException, SibsException {
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, SOURCE_IBAN, TARGET_IBAN, VALUE);
		this.sibs.addOperation(Operation.OPERATION_TRANSFER, SOURCE_IBAN, TARGET_IBAN, VALUE);
	}

	@After
	public void tearDown() {
		this.sibs = null;
	}

}
