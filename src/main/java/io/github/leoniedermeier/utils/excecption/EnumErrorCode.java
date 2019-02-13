package io.github.leoniedermeier.utils.excecption;

public interface EnumErrorCode extends ErrorCode {

	@Override
	default String code() {
		return name();
	}
	
	String name();
}
