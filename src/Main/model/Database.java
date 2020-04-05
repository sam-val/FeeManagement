package Main.model;

import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class Database {
    public static final int ORDER_BY_EMPLOYEE_NAMES = 1;
    public static final int UNDONE = 0;
    public static final int DONE = 1;

    public static final String DB_NAME = "payManagement.db";
    public static final int ASSIGNED = 0;
    public static final int UNASSIGNED = 1;
    public static String pathToProject = new File("").getAbsolutePath();
    public static final String CONNECT_STRING = "jdbc:sqlite:" + pathToProject + "/src/DatabaseResource/" + DB_NAME;
    private Connection conn;
    private PreparedStatement statement;
    private Statement st;


    public void createTable() {
        String sql = "create table if not exists accounts(user_id INTEGER PRIMARY KEY, username VARCHAR(15) NOT NULL, hashed_password TEXT NOT NULL, salt TEXT NOT NULL, account_type TINYINT(1) NOT NULL, approved TINYINT(1) NOT NULL);";

        try {
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            st.close();
        } catch (SQLException e) {
            System.out.println("couldn't create table" + e.getMessage());
        }
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(CONNECT_STRING);
            System.out.println("Connection successful");
            // create table:
            createTable();
        } catch (SQLException e) {
            System.out.println("Failed");
            System.out.println(e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
                if (statement != null) {
                    statement.close();
                }
                if (st != null) {
                    st.close();
                }
                System.out.println("Database is closed...");
            }
        } catch (SQLException e) {
            e.getStackTrace();
            System.out.println("Fail to close database or statement");
        }
    }

    public List<EmpUser> queryPendingEmpUsers() {
        String sql = "SELECT * FROM emp_users where approved = 0";

        List<EmpUser> empUsers = new ArrayList<>();


        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                EmpUser empUser = new EmpUser(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6)
                        , rs.getString(7), rs.getInt(8), rs.getString(9));

                empUsers.add(empUser);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }


        return empUsers;
    }


    private List<EmpUser> findApprovedALlEmpUsers() {
        String sql = "SELECT * from emp_users where approved = 1 and type != 0";
        List<EmpUser> users = new ArrayList<>();
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (!rs.isBeforeFirst()) {
                System.out.println("couldn't find employee");
            } else {
                while (rs.next()) {
                    EmpUser empUser = new EmpUser(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6)
                            , rs.getString(7), rs.getInt(8), rs.getString(9));
                    users.add(empUser);
                }
            }
            return users;
        } catch (SQLException e) {
            System.out.println("Error trying to find employee: ");
            e.printStackTrace();
            return  users;
        }
    }

    public EmpUser findEmployee(int id) {
        String sql = "SELECT * from emp_users where id = ? ";
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            EmpUser empUser = null;
            int times = 0;
            if (!rs.isBeforeFirst()) {
                System.out.println("couldn't find employee");
            } else {
                while (rs.next()) {
                    empUser = new EmpUser(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6)
                            , rs.getString(7), rs.getInt(8), rs.getString(9));
                    times += 1;
                    if (times > 1) {
                        throw new SQLException("there is more than one record with that ID!");
                    }
                }
            }
            return empUser;
        } catch (SQLException e) {
            System.out.println("Error trying to find employee: ");
            e.printStackTrace();
            return null;
        }
    }

//    public List<Employee> findEmployees()

    public User findUser(String username) {
        String sql = "SELECT * FROM accounts where username = ?";
        try {
            statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();
            User user = new User();
            if (!results.isBeforeFirst()) {
                System.out.println("no records of user");
                return null;
            } else {
                while (results.next()) {
                    user.setId(results.getInt(1));
                    user.setUsername(results.getString(2));
                    user.setHashedPassword(results.getString(3));
                    user.setSalt(results.getString(4));
                    user.setType(results.getInt(5));
                    user.setApproved(results.getInt(6));
                }

            }
            return user;
        } catch (
                SQLException e) {
            System.out.println("error: " + e.getMessage());
            return null;
        }
    }

    private int insertAccount(String username, String hashedPassword, String saltString) throws SQLException {
        if (findUser(username) == null) {

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String date = dateTimeFormatter.format(now);

            String sql2 = "INSERT INTO accounts(username, hashed_password, salt, account_type, approved, date_created) VALUES(?, ?, ?, 1, 0, ?)";
            // account-type: 0 is admin; 1 is employee;
            statement = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, username);
            statement.setString(2, hashedPassword);
            statement.setString(3, saltString);
            statement.setString(4, date);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected != 1) {
                throw new SQLException("Could not insert new account into DB");
            }
            ResultSet generatedID = statement.getGeneratedKeys();
            while (generatedID.next()) {
                return generatedID.getInt(1);
            }
        }
        System.out.println("User exists");
        throw new SQLException("user exits");
    }


    public void insertEmployee(String employeeName, String email, int phone, String skillSet, String hashedPassword, String salt, String username) {
        try {
            conn.setAutoCommit(false);
            int id = insertAccount(username, hashedPassword, salt);
            String sql = "INSERT INTO employees (user_id, name, email, phone, skill_set) values (?,?,?,?, ?)";
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setString(2, employeeName);
            statement.setString(3, email);
            statement.setInt(4, phone);
            statement.setString(5, skillSet);
            int affectedRow = statement.executeUpdate();
            if (affectedRow != 1) {
                throw new SQLException("inserting employee failed");
            } else {
                conn.commit();
            }

        } catch (Exception e) {
            try {
                System.out.println("Things went wrong at insert Employee: " + e.getMessage());
                System.out.println("Rolling back");
                conn.rollback();
            } catch (SQLException e1) {
                System.out.println("Can't roll back! : ");
                System.out.println(e1.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Could not reset auto-commit to to TRUE: " + e.getMessage());
            }
        }
    }

    public boolean setApprove(int id) {
        String sql = "Update accounts set approved = 1 where user_id = ?";
        try {
            conn.setAutoCommit(false);
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Error: there is more than one employee with that ID!");
            } else {
                conn.commit();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("try rolling back");
            try {
                conn.rollback();
                return false;
            } catch (SQLException ex) {
                System.out.println("cant roll back!");
                ex.printStackTrace();
                return false;
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("cant set back auto commit " + e.getMessage());
            }
        }

    }

    public List<Task> findAssignedTasksByEmployee(int id) {
        System.out.println("id is " + id);
        String sql = "SELECT * FROM tasks_by_user where assigned_user = ?";
        List<Task> tasks = new ArrayList<>();
        try {

            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (!rs.isBeforeFirst()) {
                throw new SQLException("No records");
            }
            while (rs.next()) {
                Task task = new Task();
                System.out.println("id of task" + rs.getInt(1));
                task.setId(rs.getInt(1));
                task.setAssignedDate(rs.getString(8));
                task.setName(rs.getString(2));
                task.setDescription(rs.getString(3));
                task.setClient(rs.getString(4));
                task.setAssignedUser(rs.getInt(5));
                task.setDeadline(rs.getString(6));
                task.setFee(rs.getInt(7));
                task.setDone(rs.getInt(9));
                tasks.add(task);
            }
            return tasks;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return tasks;
        }
    }

    public List<Task> findAssignedTasksByEmployee(int id, int done_num) {
        List<Task> tasks = findAssignedTasksByEmployee(id);
        List<Task> rubbish = new ArrayList<>();
        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                if (done_num == UNDONE) {
                    if (task.getDone() != UNDONE) {
                        System.out.println("run");
                        rubbish.add(task);
                    }
                } else {
                    if (task.getDone() != DONE) {
                        rubbish.add(task);
                    }
                }
            }
            tasks.removeAll(rubbish);

        }
        return tasks;


    }

    public List<EmpUser> queryEmployees(boolean approved, boolean onTasks, String searchInput) {
        String sql;
        List<EmpUser> users = new ArrayList<>();
        if (approved == false) {
            users = searchEmpUserList(queryPendingEmpUsers(), searchInput);
            return users;

        } else {
            try {
                sql = "SELECT * from tasks_by_user where assigned_user != 0 and task_name != '*removed' and done = 0 ";
                st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);

                SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd");

                while (rs.next()) {
                    int user_id = rs.getInt(5);
                    String deadline = rs.getString(6);

                    Date deadlineDate = spf.parse(deadline);
//                    System.out.println(deadlineDate.toString());
                    Date today = spf.parse(spf.format(new Date()));
//                    System.out.println(today.toString());

                    EmpUser user = findEmployee(user_id);

                    // compare 2 Date objects:
                    if (deadlineDate.compareTo(today) >= 0) {
                        users.add(user);
                    }
                }

                if (onTasks == true) {
                    users = searchEmpUserList(users, searchInput);
                } else {
                    List<EmpUser> allUsers = findApprovedALlEmpUsers();

                    List<EmpUser> collected = new ArrayList<>();
                    for (EmpUser user : users) {
                        for (EmpUser empUser : allUsers) {
                            if (user.getId() == empUser.getId()) {
                                collected.add(empUser);
                                break;
                            }
                        }
                    }
                    allUsers.removeAll(collected);
                    users = searchEmpUserList(allUsers, searchInput);

                    }
//                    users = searchEmpUserList(allUsers, searchInput);
                return users;

            } catch (SQLException | ParseException e) {
                System.out.println(e.getMessage());
                return users;
            }
        }

    }

    private List<EmpUser> searchEmpUserList(List<EmpUser> users, String searchInput) {
        if (searchInput.isBlank()) {
            return users;
        }
        searchInput = searchInput.trim().toLowerCase();
        StringBuilder builder = new StringBuilder();
        List<EmpUser> rubbish = new ArrayList<>();
        for (EmpUser user : users) {
            builder.setLength(0);
            builder.append(user.getName());
            builder.append(user.getPhone());
            builder.append(user.getEmail());
            builder.append(user.getSkills());
            builder.append(user.getId());
            System.out.println(builder.toString());
            if (!builder.toString().toLowerCase().contains(searchInput)) {
                rubbish.add(user);
            }
        }
        users.removeAll(rubbish);
        return users;
    }

    public boolean addClient(String name) {

        name.trim();

        String sql = "INSERT INTO clients(name) values (?)";
        try {

            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("error inserting client: " + e.getMessage());
            return false;
        }

    }

    public boolean deleteClient(int id) {
        String sql = "DELETE FROM clients where client_id = ?";

        try {
            conn.setAutoCommit(false);
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            int affectedRow = statement.executeUpdate();
            if (affectedRow != 1) {
                throw new SQLException("more than 1 row affected!");
            }

            return true;

        } catch (SQLException e) {
            try {
                System.out.println(e.getMessage() + "try rolling back");
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println("cant roll back!" + ex.getMessage() );
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("cant set auto commmit back");
                e.printStackTrace();
            }
        }
        return false;
    }

    public List<Client> getAllClients() {
        String sql = "Select * from clients";
        List<Client> clients = new ArrayList<>();
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt(1));
                client.setName(rs.getString(2));
                clients.add(client);
            }
        } catch (SQLException e) {
            System.out.println("error querying clients" + e.getMessage());
        }
        return clients;
    }

    public boolean addTask(String taskName, int clientId, String deadline, String description) {


        String sql = "INSERT INTO tasks(name, description, client, deadline, assigned_to_user) values (?, ? , ? ,?, 0)";
        String sql1 = "SELECT * FROM clients where client_id  = ?";

        try {
            statement = conn.prepareStatement(sql1);
            statement.setInt(1, clientId);
            ResultSet rs =  statement.executeQuery();
            if (!rs.isBeforeFirst()) {
                throw new SQLException("There is no client with that id");
            }
            int times = 0;
            while (rs.next()) {
                if (times > 1) {
                    throw new SQLException("There is more than 1 record with the client ID");
                }
                times += 1;
            }
            statement = conn.prepareStatement(sql);
            statement.setInt(3, clientId);
            statement.setString(1, taskName);
            statement.setString(4, deadline);
            if (description.trim().isBlank() || description.trim().isEmpty()) {
                statement.setString(2, "n/a");
            } else {
                statement.setString(2, description);
            }
            int affectedRow =  statement.executeUpdate();
            if (affectedRow != 1) {
                throw new SQLException("Couldn't insert new task!");
            }
            return true;
        } catch(SQLException e) {
            System.out.println("error inserting task: " + e.getMessage());
            return false;
        }

    }

    public List<Task> findAllOverDueAndUncompletedTasks() {
        // for the dashboard in admin window
        String sql = "Select * from tasks_by_users where assigned_user != 0";

        // grab deadlines from the results & comapre it to current date
        return null;
    }

    public List<Task> searchTasks(String search, int assign_value) {
        String sql;
        System.out.println(search);
        search = search.trim().toLowerCase();
        System.out.println(search);
        List<Task> tasks = new ArrayList<>();


        try {
            if (assign_value == ASSIGNED) {
                sql = "SELECT * FROM tasks_by_user where task_name != '*removed' and ( task_name || client || description ) LIKE ?";
                statement = conn.prepareStatement(sql);
                statement.setString(1, "%" + search + "%");
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    Task task = new Task();
                    task.setId(rs.getInt(1));
                    task.setAssignedDate(rs.getString(8));
                    task.setName(rs.getString(2));
                    task.setDescription(rs.getString(3));
                    task.setClient(rs.getString(4));
                    task.setAssignedUser(rs.getInt(5));
                    task.setDeadline(rs.getString(6));
                    task.setFee(rs.getInt(7));
                    task.setDone(rs.getInt(9));
                        task.setNumOfPendingEmps(0);
                    tasks.add(task);
                }
            } else {

                sql = "SELECT * FROM tasks inner join clients on tasks.client = clients.client_id where tasks.assigned_to_user = 0 and tasks.name != '*removed' and ( tasks.name || clients.name || tasks.description ) LIKE ?";
                statement = conn.prepareStatement(sql);
                statement.setString(1, "%" + search + "%");
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    Task task = new Task();

                    // set number of candidate for the task:
                    String sql2 = "SELECT Count(*) as total from task_fee where task_id = ?";
                    statement = conn.prepareStatement(sql2);
                    statement.setInt(1, rs.getInt(1));
                    ResultSet resultSet = statement.executeQuery();
                    int total = resultSet.getInt("total");
                    task.setNumOfPendingEmps(total);

                    task.setDone(0);
                    task.setFee(0);
                    task.setClient(rs.getString(8));
                    task.setDeadline(rs.getString(6));
                    task.setName(rs.getString(2));
                    task.setId(rs.getInt(1));
                    task.setAssignedUser(rs.getInt(5)); // is 0
                    tasks.add(task);
                }

                ///////
            }

        } catch (SQLException e) {
            System.out.println("Error query tasks: " + e.getMessage());
        }

        return tasks;

    }


    public void findCandidatesByTask(int task_id) {

    }

    public boolean removeATask(int task_id) {
        // actually just change the name to "removed"
        String sql = "UPDATE tasks set name = '*removed', deadline = '*removed', description = '*removed' where task_id = ?";
        boolean result = false;
        try {
            conn.setAutoCommit(false);
            statement = conn.prepareStatement(sql);
            statement.setInt(1, task_id);
            int affectedRow = statement.executeUpdate();
            if (affectedRow != 1) {
                throw new SQLException("there is more than 1 record with that id!");
            }
            result = true;
        } catch (SQLException e) {
            try {
                System.out.println("error " + e.getMessage() + "\nrolling back!");
                result = false;
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public Task findAssignedTaskByID(int id) {

        String sql = "SELECT * from tasks_by_user where id = ? and task_name != '*removed' ";
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            Task task = new Task();
            int times = 0;
            if (!rs.isBeforeFirst()) {
                System.out.println("no record of task with that id");
            } else {
                while (rs.next()) {
                    task.setId(rs.getInt(1));
                    task.setAssignedUser(rs.getInt(5));
                    task.setFee(rs.getInt(7));
                    task.setDone(rs.getInt(9));
                    task.setClient(rs.getString(4));
                    task.setDeadline(rs.getString(8));
                    task.setDescription(rs.getString(3));
                    task.setName(rs.getString(2));
                    times += 1;
                    if (times > 1) {
                        throw new SQLException("there is more than one record with that ID!");
                    }
                }
            }
            return task;
        } catch (SQLException e) {
            System.out.println("Error trying to find employee: ");
            e.printStackTrace();
            return null;
        }
    }

    public boolean setTaskCompleted(int id) {
        String sql = "Update assignment_history set done = 1 where task_id = ?";
        try {
            conn.setAutoCommit(false);
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Error: there is more than one employee with that ID!");
            } else {
                conn.commit();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("try rolling back");
            try {
                conn.rollback();
                return false;
            } catch (SQLException ex) {
                System.out.println("cant roll back!");
                ex.printStackTrace();
                return false;
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("cant set back auto commit " + e.getMessage());
            }
        }
    }

    public Task findUnAssignedTaskByID(int id) {
        String sql = "SELECT * from tasks inner join clients on tasks.client = clients.client_id where tasks.task_id = ? and tasks.name != '*removed' and tasks.assigned_to_user = 0 ";
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            Task task = new Task();
            int times = 0;
            if (!rs.isBeforeFirst()) {
                System.out.println("no record of task with that id");
            } else {
                while (rs.next()) {
                    task.setId(rs.getInt(1));
                    task.setAssignedUser(rs.getInt(5));
                    task.setClient(rs.getString(8));
                    task.setDeadline(rs.getString(6));
                    task.setDescription(rs.getString(3));
                    task.setName(rs.getString(2));
                    times += 1;
                    if (times > 1) {
                        throw new SQLException("there is more than one record with that ID!");
                    }
                }
            }
            return task;
        } catch (SQLException e) {
            System.out.println("Error trying to find employee: ");
            e.printStackTrace();
            return null;
        }
    }

    public List<TaskFee> findFeeProposalsByTask(int id) {
        System.out.println("id is " + id);
        String sql = "SELECT * FROM tasks inner join task_fee on tasks.task_id  = task_Fee.task_id inner join employees on  task_fee.user_id = employees.user_id where tasks.task_id = ? and tasks.name != '*removed'";
        List<TaskFee> taskFees = new ArrayList<>();
        try {

            statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (!rs.isBeforeFirst()) {
                throw new SQLException("No records");
            }
            while (rs.next()) {
                TaskFee taskFee = new TaskFee();
                taskFee.setTaskId(rs.getInt(1));
                taskFee.setPendingEmpId(rs.getInt(8));
                taskFee.setProposedFee(rs.getInt(9));
                taskFee.setComment(rs.getString(10));
                taskFee.setPendingEmpName(rs.getString(12));
                taskFees.add(taskFee);
            }
            return taskFees;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return taskFees;
        }
    }

    public boolean assignUserToTask(int user_id, int task_id, int fee) {
        String sql = "UPDATE tasks SET assigned_to_user = ? where task_id = ? and task_id != '*removed'";
        String sql2 = "Insert into assignment_history (task_id, user_id, pay, assignment_date, done) values (?, ?, ?, ?, 0)";

        try {
            conn.setAutoCommit(false);
            statement = conn.prepareStatement(sql);
            statement.setInt(1, user_id);
            statement.setInt(2, task_id);
            int affected = statement.executeUpdate();
            if (affected != 1) {
                throw new SQLException("more than 1 row updated!");
            }
            statement = conn.prepareStatement(sql2);
            statement.setInt(1, task_id);
            statement.setInt(2, user_id);
            statement.setInt(3, fee);
            SimpleDateFormat spf = new SimpleDateFormat("yyyy/MM/dd");
            String today = spf.format(new Date());
            statement.setString(4, today);

            int num = statement.executeUpdate();
            if (num != 1) {
                throw new SQLException("inserted more than 1 record!");
            } else {
                conn.commit();
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            try {
                System.out.printf("Rolling back...");
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}






