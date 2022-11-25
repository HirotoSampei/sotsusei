package jp.te4a.spring.boot.sotsusei.validate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
public class WritterValidator implements ConstraintValidator<Writter,String>{
	String param;
	@Override
	public void initialize(Writter nv){ param =  nv.ok(); }
	@Override
	public boolean isValid(String in,ConstraintValidatorContext cxt){
		if(in == null){
			return false;
		}
		System.out.println(in.equals(param));
		return in.equals(param);
}}
