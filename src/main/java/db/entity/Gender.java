package db.entity;

import java.util.Arrays;

/**
 * @author Vova Iatsyk
 *         Date: 4/20/17.
 */
public enum Gender {

    MALE,
    FEMALE;
    
    public static Gender parse(final String gender) {
        return Arrays.stream(values()).filter(g -> g.name().equals(gender)).findAny().orElse(null);
    }
}
