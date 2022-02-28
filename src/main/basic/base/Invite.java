package base;

import java.io.Serializable;

/**
 * Класс для созданя объекта (приглашение в группу)
 * Поля: отправитель, ссылка на группу.
 */
public class Invite implements Serializable {
    private static final long serialVersionUID = 2529685098267757690L;
    private User sender;
    private Group inviteGroup;

    public Invite(User user, Group group) {
        sender = user;
        inviteGroup = group;
    }

    public Group getInviteGroup() {
        return inviteGroup;
    }

    public User getSender() {
        return sender;
    }
}
