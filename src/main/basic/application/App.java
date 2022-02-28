package application;

/**
 * Вся работа происходит тут.
 */
public class App {
    private AppFunctionality functionalilty = new AppFunctionality();

    public void run() throws Exception {
        functionalilty.userStorage.upload(); // загрузка базы
        boolean isFirst = true; // для авторизации или регистрации
        while (true) {
            if (isFirst) {
                functionalilty.botDialog.getMessage(0);
                while (true) {
                    String regLog = functionalilty.in.nextLine();
                    functionalilty.registr(regLog);
                    isFirst = false;
                    if (functionalilty.allCool) break;
                }
                functionalilty.currentUser = functionalilty.userStorage.getList().get(functionalilty.userStorage.getUserId());
                functionalilty.botDialog.getMessage(3);
            }


            switch (functionalilty.in.nextLine()) {
                case "/exit":
                    return;
                case "/users":
                    functionalilty.userStorage.getUsers();
                    break;
                case "/myGroups":
                    functionalilty.currentUser.showUserGroups();
                    break;
                case "/createGroup":
                    functionalilty.createGroup();
                    functionalilty.userStorage.save();
                    break;
                case "/invite":
                    functionalilty.inviteGroup();
                    functionalilty.userStorage.save();
                    break;
                case "/invites":
                    functionalilty.userInvites();
                    functionalilty.userStorage.save();
                    break;
                case "/sendMas":
                    functionalilty.mes();
                    functionalilty.userStorage.save();
                    break;
                case "/myMas":
                    functionalilty.myMes();
                    break;
                case "/help":
                    functionalilty.botDialog.getMessage(3);
                    break;
                default:
                    System.out.println("Введите /help если не помните доступных команд");
                    break;
            }
        }
    }
}
