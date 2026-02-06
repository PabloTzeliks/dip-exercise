package pablo.tzeliks.domain.entity.vo;

public class Password {
    private String rawPassword;

    public Password(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    // Hashing

    private String hashes(String rawPassword) {

        var num = rawPassword.length();
        char[] reversed = new char[num];
        var index = num;

        var letters = rawPassword.toCharArray();

        for (int count = 0; num > count; count++) {
            index--;

            reversed[count] = letters[index];
        }

        String password = reversed.toString();

        return password;
    }
}
