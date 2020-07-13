package org.devframework.calculator.bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.devframework.calculator.AbstractCalculator;

/**
 * 은행대출이자 계산기
 * @author stylehosting
 */
public class BankLoanInterestCalculator extends AbstractCalculator {

	/**
	 * repayMethod 상황방식 마다 대출이자를 계산하여 리턴.
	 * @param repayMethod 경세정평가 상환방식 구분
	 * @param loan 차입금 (대출금액)
	 * @param repayYear 원금상황기간 (년)
	 * @param termLoan 거치기간
	 * @param interestRate 차입금이자율 (대출이자)
	 */
	public List<BankLoanInterest> calculate(BankLoanRepayMethod repayMethod, double loan, double repayYear, double termLoan, double interestRate) {
		// 백분율로 변환
		interestRate = toPercentage(interestRate);
		// 회차 
		double loanPeriod = repayYear * MAX_MONTH;
		termLoan = termLoan * MAX_MONTH;
		double interestRateDividedValue = (interestRate / 12);
		List<BankLoanInterest> incomeLoanList = new ArrayList<>();
		int period = 0;
		int previousIndex = -1;
		double monthlyRepayment = 0;
		// 원리금균등분할
		if (repayMethod == BankLoanRepayMethod.PRINCIPAL_INTEREST) {
			// 월상환금
			if (interestRate != 0.0) {
				// ($C$2*($C$3/12)*((1+($C$3/12))^($C$4-$C$5)))/(((1+($C$3/12))^($C$4-$C$5))-1)
				monthlyRepayment = (loan * interestRateDividedValue
						* (Math.pow(1 + interestRateDividedValue, (loanPeriod - termLoan))))
						/ ((Math.pow(1 + interestRateDividedValue, (loanPeriod - termLoan))) - 1);
			} else {
				monthlyRepayment = 0;
			}
		}
		double loanRateFixed = 0;
		// 만기일시상환
		if (repayMethod == BankLoanRepayMethod.MATURITY) {
			// 납입이자 $D$19*($D$14/12)
			loanRateFixed = loan * (interestRateDividedValue);
		}
		while (period < loanPeriod) {
			period++;
			BigDecimal principal = null;
			BigDecimal loanRate = null;
			BigDecimal repayments = null;
			BigDecimal balance = null;
			BankLoanInterest previousLoan = null;
			if (previousIndex > -1) {
				previousLoan = incomeLoanList.get(previousIndex);
			}			
			// 원금균등분할
			if (repayMethod == BankLoanRepayMethod.PRINCIPAL) {
				// 거치기간이 입력된경우만
				if (termLoan > 0) {
					if (termLoan >= period) {
						principal = newDecimal(0);
					} else {
						principal = newDecimal(loan / (loanPeriod - termLoan));
					}
				} else {
					principal = newDecimal(loan / loanPeriod);
				}
				if (period == 1) {
					loanRate = newDecimal(loan * interestRateDividedValue);
					repayments = newDecimal(principal.doubleValue() + loanRate.doubleValue());
					balance = newDecimal(loan - repayments.doubleValue() + loanRate.doubleValue());
				} else {
					loanRate = newDecimal(previousLoan.getBalance().doubleValue() * interestRateDividedValue);
					repayments = newDecimal(principal.doubleValue() + loanRate.doubleValue());
					balance = newDecimal(previousLoan.getBalance().doubleValue() - repayments.doubleValue() + loanRate.doubleValue());
				}
			}
			// 원리금균등분할
			if (repayMethod == BankLoanRepayMethod.PRINCIPAL_INTEREST) {
				// 거치기간이 입력된경우만
				if (termLoan > 0) {
					if (termLoan >= period) {
						principal = newDecimal(0);
						loanRate = newDecimal(loan * interestRateDividedValue);
						repayments = newDecimal(loanRate.doubleValue());
						balance = newDecimal(loan -  repayments.doubleValue() + loanRate.doubleValue());
					} else {
						repayments = newDecimal(monthlyRepayment);
						loanRate = newDecimal(previousLoan.getBalance().doubleValue() * interestRateDividedValue);
						balance = newDecimal(previousLoan.getBalance().doubleValue() - repayments.doubleValue() + loanRate.doubleValue());
						principal = newDecimal(repayments.doubleValue() - loanRate.doubleValue());
					}
				} else {
					repayments = newDecimal(monthlyRepayment);
					if (period == 1) {
						loanRate = newDecimal(loan * interestRateDividedValue);
						balance = newDecimal(loan - repayments.doubleValue() + loanRate.doubleValue());
					} else {
						loanRate = newDecimal(previousLoan.getBalance().doubleValue() * interestRateDividedValue);
						balance = newDecimal(previousLoan.getBalance().doubleValue() - repayments.doubleValue() + loanRate.doubleValue());					
					}
					principal = newDecimal(repayments.doubleValue() - loanRate.doubleValue());
				}
			}
			// 만기일시상환
			if (repayMethod == BankLoanRepayMethod.MATURITY) {
				// 납입원금
				// 마지막회차
				loanRate = newDecimal(loanRateFixed);
				if (period == (int) loanPeriod) {
					principal = newDecimal(loan);
					repayments = newDecimal(principal.doubleValue() + loanRate.doubleValue());
					balance = newDecimal(0);
				} else {
					principal = newDecimal(0);
					repayments = newDecimal(loanRateFixed);
					balance = newDecimal(loan);
				}
			}			
			BankLoanInterest loanData = new BankLoanInterest();
			loanData.setLoanPeriod(period);
			loanData.setPrincipal(principal);
			loanData.setLoanRate(loanRate);
			loanData.setRepayments(repayments);
			loanData.setBalance(balance);
			
			loanData.setPrincipalValue(Math.round(principal.doubleValue()));
			loanData.setLoanRateValue(Math.round(loanRate.doubleValue()));
			loanData.setRepaymentsValue(Math.round(repayments.doubleValue()));
			loanData.setBalanceValue(Math.round(balance.doubleValue()));
			incomeLoanList.add(loanData);

			previousIndex++;
		}
		return incomeLoanList;
	}
	
}
