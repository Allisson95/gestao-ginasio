package br.com.gestaoginasio.converter.jpa;

import java.time.Year;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class JpaYearConverter implements AttributeConverter<Year, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Year year) {
		Integer yearInteger = null;
		if (year != null) {
			yearInteger = year.getValue();
		}
		return yearInteger;
	}

	@Override
	public Year convertToEntityAttribute(Integer yearInteger) {
		Year year = null;
		if (yearInteger != null) {
			year = Year.of(yearInteger);
		}
		return year;
	}

}
