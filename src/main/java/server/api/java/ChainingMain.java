package server.api.java;

public class ChainingMain {
    public static void main(String[] args) {
        // Chaining 을 사용
        User user = new User()
                .setName("John Wick")
                .setPass("1234");

        // Builder 를 사용
        UserBuild builder = UserBuild.builder()
                .name("John Wick")
                .pass("1234")
                .build();

    }
}
