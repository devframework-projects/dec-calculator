package org.devframework.calculator.bank;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class BankLoanInterest implements Serializable {

	private static final long serialVersionUID = -6390238611569716078L;

	private int loanPeriod;

	private BigDecimal principal; // 납입원금
	private BigDecimal loanRate; // 대출이자
	private BigDecimal repayments; // 월 상환금
	private BigDecimal balance; // 대출 잔금

	private long principalValue;
	private long loanRateValue;
	private long repaymentsValue;
	private long balanceValue;

}
