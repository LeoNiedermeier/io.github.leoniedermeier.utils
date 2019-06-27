package io.github.leoniedermeier.utils.excecption;

public interface EnumErrorCode extends ErrorCode {

    @Override
    default String code() {
        return name();
    }

    String name();
    
    @Override
    public default String description() {
        if(! this.getClass().isEnum()) {
            return null;
        }
        try {
            Description description = this.getClass().getField(((Enum<?>) this).name())
                    .getAnnotation(Description.class);
            if (description != null) {
                return description.value();
            }
        } catch (NoSuchFieldException | SecurityException e ) {
           return "No description available. Error occured: " + e;
        }
        return null;
    }
}
