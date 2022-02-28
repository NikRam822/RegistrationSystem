package application;

import base.*;

import java.util.Scanner;

/**
 * Класс для определения функционала класса App
 */
public class AppFunctionality {
    protected User currentUser;
    protected Scanner in = new Scanner(System.in);
    protected UserStorage userStorage = new UserStorage();
    protected BotDialog botDialog = new BotDialog();
    protected boolean allCool = false;

    /**
     * Вывод всех сообщений для текущего пользователя
     */
    protected void myMes() {
        currentUser.getMessages();
    }

    /**
     * Выбор группы.
     * Отправить сообщение всем участником группы ( если администратор )
     * Ввод ключа администрации для группы
     */
    protected void mes() {
        currentUser.showUserGroups();
        System.out.println("Выберете группу.");
        String otv = in.nextLine();

        try {
            Integer.parseInt(otv);
        } catch (NumberFormatException e) {
            System.out.println("Проверьте корректность данных: ID - целое число");
            mes();
        }

        int idGroup = Integer.parseInt(otv);

        if (!currentUser.getUserGroup(idGroup).isAdmin(currentUser)) {
            System.out.println("У вас нет прав для отправки сообщений. Введите /A [ключ администратора] для получения прав. /exit для выхода");
            adminKey(idGroup);
        } else {
            System.out.println("Введите сообщение:");
            String mes = in.nextLine();
            Message message = new Message(currentUser, mes);
            currentUser.getUserGroup(idGroup).setMessage(message);
            System.out.println("Сообщение отправлено.");
            userStorage.save();
        }
        //return;
    }

    /**
     * Вспомогательный для mes
     *
     * @param id - номер группы в списке
     */
    private void adminKey(int id) {

        String[] otv1 = in.nextLine().split(" ");
        if (otv1.length < 2) {
            System.out.println("Неверная операция, будте внимательнее: Введите /A [ключ администратора]");
            mes();
        }

        switch (otv1[0]) {
            case "/A":
                String password = otv1[1];
                if (currentUser.getUserGroup(id).getAdminPassword().equals(password)) {
                    currentUser.getUserGroup(id).setAdmin(currentUser);
                    System.out.println("Опреация выполнена успешно.");
                    userStorage.save();
                    mes();
                } else {
                    System.out.println("Неверный пароль.");
                    mes();
                }
                break;
            case "/exit":
                return;
            default:
                System.out.println("Повторите попытку. Введите /A [ключ администратора] для получения прав. /exit для выхода");
                adminKey(id);
                break;
        }
    }

    /**
     * Просмотр приглашений в группу, возможность отказать или вступить в группу
     */
    protected void userInvites() {
        int j = 0;
        for (Invite e : currentUser.getInvites()
        ) {
            System.out.println("Отправтель: " + e.getSender().getName() + " ID: " + e.getSender().getUserId() + "      Группа: " + e.getInviteGroup().getGroupName() + " Номер группы: " + j + "\n ");
            j++;
        }

        System.out.println("Введите /accept [номера групп через пробел] для принятия приглашения");
        System.out.println("Введите /den [номера групп через пробел] для отказа приглашения");
        System.out.println("/exit для выхода");

        String[] other = in.nextLine().split(" ");

        try {
            for (int i = 1; i < other.length; i++) {
                Integer.parseInt(other[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Проверьте корректность вводимых данных: id ( целые числа через пробел, без использования букв ) ");
            userInvites();
        }

        int[] ind = new int[other.length - 1];
        for (int i = 1; i < other.length; i++) {
            ind[i - 1] = Integer.parseInt(other[i]);
        }

        switch (other[0]) {
            case "/accept":
                for (int e : ind
                ) {
                    currentUser.addGroup(currentUser.getInvites().get(e).getInviteGroup());
                    currentUser.getInvites().get(e).getInviteGroup().addUser(currentUser);
                    currentUser.getInvites().remove(e);
                }
                System.out.print("Вы вступили в группу(ы)");
                break;
            case "/den": {
                for (int e : ind
                ) {
                    currentUser.getInvites().remove(e);
                }
                System.out.print("Вы отклонили приглашение в группу(ы)");
                break;
            }
            case "/exit":
                return;
            default:
                System.out.println("Повторите попытку.");
                userInvites();
                break;
        }
    }

    /**
     * Приглашение в группу пользователя с обработкай данных из всне
     */
    protected void inviteGroup() {
        System.out.println("Список групп:\n");
        currentUser.showUserGroups();
       System.out.println(" ");
        System.out.println("Список пользователей:");
        userStorage.getUsers();
        System.out.println(" ");
        System.out.println("Введите номер группы и ID пользователя-лей");
        String[] inv = in.nextLine().split(" ");

        try {
            for (String e : inv
            ) {
                Integer.parseInt(e);
            }
        } catch (NumberFormatException e) {
            System.out.println("Проверьте корректность вводимых данных: id ( целые числа без использования букв ) ");
            inviteGroup();
        }

        try {
            currentUser.getUserGroup(Integer.parseInt(inv[0]));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Группы с данным номером нет в списке, проверьте корректность вводимых данных.");
            inviteGroup();
        }

        Group groupToInv = currentUser.getUserGroup(Integer.parseInt(inv[0]));


        int[] usersId = new int[inv.length - 1];

        for (int i = 1; i < inv.length; i++) {
            usersId[i - 1] = Integer.parseInt(inv[i]);
        }

        int falseUser = 0;
        try {
            for (int x : usersId
            ) {
                userStorage.getList().get(x);
                falseUser = x;
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Пользователя с ID: " + falseUser + " нет в системе, проверьте данные и повторите попытку.");
            System.out.println();
            inviteGroup();
        }


        groupToInv.invite(userStorage, groupToInv, currentUser, usersId);
    }

    /**
     * Создание группы с обработкай данных из всне
     */
    protected void createGroup() {
        System.out.println("Введите название группы и пароль для администратора: nameGroup adminPassword");
        String[] namePas = in.nextLine().split(" ");
        if (namePas.length != 2) {
            botDialog.getExeption(4);
            createGroup();
        } else {
            userStorage.getList().get(userStorage.getUserId()).createGroup(namePas[0], namePas[1]);
        }
    }

    /**
     * Обработка внешней команды /r для регистрации /l для авторизации /exit для выхода
     *
     * @param str - nextline
     * @throws Exception исключение
     */
    protected void registr(String str)
            throws Exception {
        switch (str) {
            case "/r":
                botDialog.getMessage(2);
                String[] user = in.nextLine().split(" ");
                sleshR(user);
                break;
            case "/l":
                botDialog.getMessage(1);
                String[] userL = in.nextLine().split(" ");
                sleshL(userL);
                break;
            case "/exit":
                return;
            default:
                System.out.println("Неверная операция, повторите попытку. Введите /r для регистрации, /l для авторизации");
                registr(in.nextLine());
        }
    }

    /**
     * Регистрация пользователя
     *
     * @param logPas массив (лоигн, пароль)
     * @throws Exception исключение
     */
    private void sleshR(String[] logPas) throws Exception {
        if (logPas[0].equals("/exit")) {
            return;
        }
        if (logPas.length != 2) {
            System.out.println("Введите логин-пароль заного: registrLogin password");
            sleshR(in.nextLine().split(" "));
        } else if (userStorage.isLoginDuplicate(logPas[0])) {
            System.out.println("Такой логин уже существует");
            System.out.println("Чтобы залогиниться введите /l. Чтобы зарегистрироваться введите новый логин и пароль: registrLogin password");
            String mes = in.nextLine();
            if (mes.equals("/l")) registr(mes);
            else sleshR(mes.split(" "));
        } else {
            System.out.println("Подтвердите пароль: password");
            if (in.nextLine().equals(logPas[1])) {
                userStorage.addUser(logPas[0], logPas[1], userStorage.getListSize());
                userStorage.setUserId(userStorage.getListSize() - 1);
                allCool = true;
                /*User lastUser =*/ userStorage.getList().get(userStorage.getListSize() - 1);
                userStorage.save();
                System.out.println("Регистрация прошла успешно");
            } else {
                System.out.println("Пароли не совпадают, повторите попытку.");
                sleshR(logPas);
            }
        }
    }

    /**
     * Авторизация
     *
     * @param logPas - логин пароль
     * @throws Exception исключение
     */
    private void sleshL(String[] logPas)
            throws Exception {
        if (logPas[0].equals("/exit")) {
            return;
        }
        if (logPas.length != 2) {
            System.out.println("Введите логин-пароль заново: login password");
            sleshL(in.nextLine().split(" "));
        } else if (userStorage.correctLogin(logPas[0], logPas[1])) {
            System.out.println("Вход выполнен успешно");
            allCool = true;
        } else {
            System.out.println("Неверный логин или пароль, повторите попытку: login password");
            sleshL(in.nextLine().split(" "));
        }
    }
}
