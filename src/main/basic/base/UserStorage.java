package base;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Хранине базы зарегистрированных пользователей
 * поле userId - для работы с текущим пользователем
 */
public class UserStorage {
    private List<User> list = new ArrayList<>();
    private int userId;

    /**
     * Добавление пользователя при регистрации
     *
     * @param name - имя
     * @param pas  - пароль
     * @param id   - номер в списке
     */
    public void addUser(String name, String pas, int id) {
        list.add(new User(name, pas, id));
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public int getListSize() {
        return list.size();
    }

    public List<User> getList() {
        return list;
    }

    /**
     * Проверка верин ли логин и пароль при авторизации
     *
     * @param login    - логин
     * @param password - пароль
     * @return - true/false
     */
    public boolean correctLogin(String login, String password) {
        boolean findUser = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(login)) {
                if (list.get(i).getPassword().equals(password)) {
                    findUser = true;
                    userId = i;
                }
                break;
            }
        }
        return findUser;
    }

    /**
     * Проверка есть ли текущее имя в списке пользователей
     *
     * @param login - имя
     * @return -  корректен ли введённый логин пользователя
     */
    public boolean isLoginDuplicate(String login) {
        boolean findUser = false;
        for (User e : list) {
            if (e.getName().equals(login)) {
                findUser = true;
            }
        }
        return findUser;
    }

    /**
     * Сериализация базы (сохранение)
     */
    public void save() {
        try {
            FileOutputStream fos = new FileOutputStream("users.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeInt(list.size());

            for (User user : list) {
                oos.writeObject(user);
            }
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сериализация (загрузка)
     */
    public void upload() {
        try {
            FileInputStream fis = new FileInputStream("users.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);

            int userSize = ois.readInt();

            for (int i = 0; i < userSize; i++) {
                list.add((User) ois.readObject());
            }
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показывает всех текущих пользоватей в системе: Имя + ID
     */
    public void getUsers() {
        for (User e : list
        ) {
            System.out.println("ID: " + e.getUserId() + " Name: " + e.getName());
        }
    }
}
