package org.devframework.calculator;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 계산 관련 클래스. 
 * @author stylehosting
 */
public abstract class AbstractCalculator {
	
	protected static final int MAX_MONTH = 12; // 최대개월

	final Logger logger = LogManager.getLogger(getClass());
	
	/**
	 * 백분율로 변환
	 * @param v
	 * @return
	 */
	protected double toPercentage(double v) {
		return (v / 100);
	}
	
	/**
	 * BigDecimal 클래스에 v 값을 생성. String.valueOf 필수 (원본 값유지)
	 * @param v
	 * @return
	 */
	protected BigDecimal newDecimal(double v) {
		return new BigDecimal(String.valueOf(v));
	}
	
}
