package test;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.devframework.calculator.bank.BankLoanInterest;
import org.devframework.calculator.bank.BankLoanInterestCalculator;
import org.devframework.calculator.bank.BankLoanRepayMethod;
import org.junit.jupiter.api.Test;

public class TestCalculator {
	
	protected final Logger logger = LogManager.getLogger(getClass());

	protected static final DecimalFormat DEFAULT_NUMBER_FORMAT = new DecimalFormat("###,###");
	
	@Test
	public void test() {
		
		BankLoanInterestCalculator calculator = new BankLoanInterestCalculator();
		double loan = 100000000;
		double repayYear = 5;
		double termLoan = 0;
		double interestRate = 3.5;
		List<BankLoanInterest> loanInterests = calculator.calculate(BankLoanRepayMethod.MATURITY, loan, repayYear, termLoan, interestRate);
		for (BankLoanInterest loanInterest : loanInterests) {
			logger.debug("회차 : {}, 납입원금 : {}, 대출이자 : {}, 월상환금 : {}, 대출잔금 : {}", loanInterest.getLoanPeriod(),
					format(loanInterest.getPrincipalValue()), format(loanInterest.getLoanRateValue()),
					format(loanInterest.getRepaymentsValue()), format(loanInterest.getBalanceValue()));
		}
		
	}
	
	protected String format(Object value) {
  	return DEFAULT_NUMBER_FORMAT.format(value);
  }
}
