package nath.ariel.sellit_v6.activities.repository;

/**
 * Created by Jordan Perez on 16/12/2021
 */
public final class UserRepository {

    private static volatile UserRepository instance;

    private UserRepository() { }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

}