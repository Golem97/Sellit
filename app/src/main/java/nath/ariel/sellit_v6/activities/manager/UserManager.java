package nath.ariel.sellit_v6.activities.manager;

import nath.ariel.sellit_v6.activities.repository.UserRepository;

/**
 * Created by Jordan Perez on 16/12/2021
 */
public class UserManager {

    private static volatile UserManager instance;
    private UserRepository userRepository;

    private UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

}