package Main.Controller;

import Main.model.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;

public class DatabaseController {
    public static Database db = new Database();

    public static List<EmpUser> queryPendingEmpUsers() {
        return db.queryPendingEmpUsers();
    }

    public static boolean setApprove(int id) {
        boolean rs = db.setApprove(id);
        return rs;
    }

    public static EmpUser findEmployee(int id) {
        return db.findEmployee(id);
    }

    public static List<Task> findAssignedTasksByEmployee(int id) {
        return db.findAssignedTasksByEmployee(id);
    }

    public static List<Task> findAssignedTasksByEmployee(int id, int done_num) {
        return db.findAssignedTasksByEmployee(id, done_num);
    }

    public static List<EmpUser> queryEmployees(boolean approved, boolean onTasks, String searchInput) {
        return db.queryEmployees(approved, onTasks, searchInput);
    }

    public static boolean addClient(String name) {
        return db.addClient(name);
    }

    public static boolean deleteClient(String id) {
        return db.deleteClient(Integer.parseInt(id));
    }

    public static List<Client> getAllClients() {
        return db.getAllClients();
    }

    public static boolean addTask(String taskName, int clientId, String deadline, String description) {
        return db.addTask(taskName, clientId, deadline, description);
    }

    public static List<Task> searchTasks(String search, int assign_value) {
        return db.searchTasks(search, assign_value);
    }

    public static boolean removeATask(int id) {
        return db.removeATask(id);
    }

    public static Task findAssignedTaskByID(int id) {
        return db.findAssignedTaskByID(id);
    }

    public static boolean setTaskCompleted(int id) {
        boolean rs = db.setTaskCompleted(id);
        return rs;
    }

    public static Task findUnAssignedTaskByID(int id) {
        return db.findUnAssignedTaskByID(id);
    }

    public static List<TaskFee> findFeeProposalsByTask(int id) {
        return db.findFeeProposalsByTask(id);
    }

    public static boolean assignUsertoTask(int user_id, int task_id, int fee) {
        return db.assignUserToTask(user_id, task_id, fee);
    }

    public void connect() {
        db.connect();
    }

    public void close() {
        db.disconnect();
    }


    public static boolean validate(String username, char[] password) {
        // find user with username, instantiate that user (also get the salt), compare user.hashedpassword with "hashed" password
        User user = db.findUser(username);
        if (user != null) {
            String originHash = user.getHashedPassword();
            String salt = user.getSalt();

            String generatedHash = hashedPassword(password, salt);
            if (originHash.equals(generatedHash)) {
                return true;
            }
            return false;
        }

        return false;
    }

    public static boolean userExists(String username) {
        User user = db.findUser(username);
        return (user == null) ? false : true;
    }

    public static User of(String username) {

        User user = db.findUser(username);

        return user;
    }

    public static void insertEmployee(String user, char[] password, String name, String email, int phone, String skills) {
        String salt = generateSalt();
        String hashedPassword = hashedPassword(password, salt);
        db.insertEmployee(name, email, phone, skills, hashedPassword, salt, user);
    }

    private static String hashedPassword(char[] password, String saltString) {

        byte[] salt = saltString.getBytes();
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] hash = new byte[0];
        try {
            hash = factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        Base64.Encoder enc = Base64.getEncoder();

        return enc.encodeToString(hash);
    }

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        Base64.Encoder enc = Base64.getEncoder();

        return enc.encodeToString(salt);
    }
}
