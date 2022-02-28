package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания объекта (группа), для работы с пользователями группы ( проврека на адмниистрацию, отправить сообщение всем пользователям )
 * Поля: название группы, список пользователей состоящих в группе, пароль для прав администратора, список администраторов
 */
public class Group implements Serializable {
    private static final long serialVersionUID = 1529685098267757690L;
    private String groupName;
    private List<User> userList = new ArrayList<>();
    private String adminPassword;
    private List<User> admins = new ArrayList<>();

    public Group(String groupName, String adminPassword) {
        this.groupName = groupName;
        this.adminPassword = adminPassword;
    }

    /**
     * Добавление пользователя в список администрации
     *
     * @param user -пользователь
     */
    public void setAdmin(User user) {
        admins.add(user);
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * Отправление приглашение в группу.
     *
     * @param userStorage - текущая база
     * @param group       - ссылка на группу
     * @param sender      - ссылка на отправителя
     * @param idUsers     - массив для поиска пользователей в базе
     */
    public void invite(UserStorage userStorage, Group group, User sender, int[] idUsers) {
        for (int e : idUsers
        ) {
            if (!userStorage.getList().get(e).getInvites().contains(group) && !userStorage.getList().get(e).beInGroup(group)) {
                userStorage.getList().get(e).setInvites(new Invite(sender, group));
                System.out.println("Пользователю с ID " + e + " отправлено приглашение в группу: " + group.groupName);
            }
            else
            {
                System.out.println("Пользователь с ID: " + e + " уже состоит или имеет приглашение в группу: " + group.groupName);
            }
        }
    }

    public String getGroupName() {
        return groupName;
    }

    /**
     * Отправляем сообщение каждому пользователю группы
     *
     * @param message - сообщение
     */
    public void setMessage(Message message) {
        for (User e : userList
        ) {
            e.setMessages(message);
        }
    }

    /**
     * проверка является ли пользователь администратором
     *
     * @param user - проверяющийся пользователь
     * @return - true/false
     */
    public boolean isAdmin(User user) {
        boolean isAdmin = false;
        for (User e : admins
        ) {
            if (e.equals(user)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Добавление пользователя в список участников группы
     *
     * @param user - пользователь
     */
    public void addUser(User user) {
        userList.add(user);
    }
}
