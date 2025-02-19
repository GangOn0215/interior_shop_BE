package server.api.java;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBuild {
    private String name;
    private String pass;
}
