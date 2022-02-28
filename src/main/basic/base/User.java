package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания объекта user, для получения информации об объекте
 * Поля: логин, пароль, id, список групп, список сообщений, список приглашений
 */
public class User implements Serializable {
    private static final long serialVersionUID = 4529685098267757690L; // для Сериализации
    private String name; // Имя пользователя
    private String password; // Пароль
    private int userId; // ID пользователя (для поиска в UserStorage)
    private List<Group> userGroups = new ArrayList<>(); // Список групп пользователя
    private List<Message> Messages = new ArrayList<>(); // Сообщения пользователя
    private List<Invite> invites = new ArrayList<>(); // Приглашения в группу

    public User(String name, String password, int id) {
        this.name = name;
        this.password = password;
        userId = id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    /**
     * Пользователь может создать свою группу, автоматом становится администратором.
     *
     * @param name          - название группы
     * @param adminPassword - пароль для адмнстратора
     */
    public void createGroup(String name, String adminPassword) {
        Group newGroup = new Group(name, adminPassword);
        newGroup.setAdmin(this);
        this.userGroups.add(newGroup);
        newGroup.addUser(this);
    }

    /**
     * При приглашении в группу текущего пользователя, добавляем в список приглашение
     *
     * @param inv - объект класса invite
     */

    public void setInvites(Invite inv) {
        invites.add(inv);
    }

    /**
     * Показыват список всех групп пользователя
     */
    public void showUserGroups() {
        for (int i = 0; i < userGroups.size(); i++) {
            System.out.println("Номер группы: " + i + " Название: " + userGroups.get(i).getGroupName());
        }
    }

    /**
     * @param index - номер в списке
     * @return - объект класса group
     */
    public Group getUserGroup(int index) {
        return userGroups.get(index);
    }
    public boolean beInGroup(Group group){
        return userGroups.contains(group);
    }


    public List<Invite> getInvites() {
        return invites;
    }

    /**
     * Добавляет в список групп объект класса group
     *
     * @param group - объект класса group
     */
    public void addGroup(Group group) {
        userGroups.add(group);
    }

    /**
     * Добавляем сообщение в список
     *
     * @param message - сообщение
     */
    public void setMessages(Message message) {
        Messages.add(message);
    }

    /**
     * Вывод всех сообщений пользователя
     */
    public void getMessages() {
        for (Message e : Messages
        ) {
            System.out.println("Отправитель: " + e.getSender().getUserId() + " " + e.getSender().getName() + " Сообщене: " + e.getMessage());
        }
    }
}
