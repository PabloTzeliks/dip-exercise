package pablo.tzeliks.application;

import pablo.tzeliks.domain.entity.User;
import pablo.tzeliks.domain.sender.NotificationSender;

public class PasswordResetter {

    private NotificationSender sender;

    public PasswordResetter(NotificationSender sender) {
        this.sender = sender;
    }

    public void reset(User user) {
        String link = "http://techstore.com/reset?token=123";

        sender.send(user.getEmail(), "Seu link: " + link);
    }
}
