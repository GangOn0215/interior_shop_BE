package amo.java;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserBuild {
    private String name;
    private String pass;
}
