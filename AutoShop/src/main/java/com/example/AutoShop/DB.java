package com.example.AutoShop;

import com.mysql.fabric.jdbc.FabricMySQLDriver;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


@Configuration
@Controller
@PropertySource("application.properties")
public class DB {
    static String URL = "jdbc:mysql://localhost:3306/practice";
    static String USERNAME = "root";
    static String PASSWORD = "";

    public DB(){
        try {
            DriverManager.registerDriver(new FabricMySQLDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("UPDATE `user` SET sessia = NULL");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err DB");
        }
    }

    public static void init(){
        try {
            DriverManager.registerDriver(new FabricMySQLDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("UPDATE `user` SET sessia = NULL");
            System.out.println("connect to DB");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("err INIT");
        }
    }

    public static ArrayList<Categiry> getCategory() {
        ArrayList<Categiry> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `category`");
            while (resultSet.next()){
                Categiry categiry = new Categiry();
                categiry.setId(resultSet.getInt(1));
                categiry.setName(resultSet.getString(2));
                categiry.setImg("/img/" + resultSet.getString(3));
                categiry.setHref("/category?cat=" + resultSet.getInt(1));

                list.add(categiry);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err getCategory1");
        }
        return list;
    }

    public static Categiry getCategory(int n) {
        Categiry categiry = null;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `category` WHERE id = " + n);
            if (resultSet.next()){
                categiry = new Categiry();
                categiry.setId(resultSet.getInt(1));
                categiry.setName(resultSet.getString(2));
                categiry.setImg("/img/" + resultSet.getString(3));
                categiry.setHref("/category?cat=" + resultSet.getInt(1));
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err getCategory2");
        }
        return categiry;
    }

    public static ArrayList<Tovar> getTovars(int category) {
        ArrayList<Tovar> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `product` WHERE category = '" + category + "'");
            while (resultSet.next()) {
                Tovar tovar = new Tovar();

                tovar.setId(resultSet.getInt(1));
                tovar.setName(resultSet.getString(3));
                tovar.setBrand(resultSet.getString(4));
                tovar.setDescription(resultSet.getString(5));
                tovar.setPrice(resultSet.getInt(6));
                tovar.setImg("/img/" + resultSet.getString(7));
                tovar.setDisplay(resultSet.getBoolean(8));

                ArrayList<Feedback> ratingList = DB.getFeedback(resultSet.getInt(1));
                if(!ratingList.isEmpty()) {
                    float rating = 0;
                    for (Feedback f : ratingList)
                        rating += f.getRating();
                    tovar.setRating(rating / ratingList.size());
                }

                list.add(tovar);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("err getTovars");
        }
        return list;
    }

    public static ArrayList<Tovar> searchTovar(String str) {
        ArrayList<Tovar> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `product`");
            while (resultSet.next()) {
                if (resultSet.getString(3).toLowerCase().contains(str.toLowerCase()) || resultSet.getString(4).toLowerCase().contains(str.toLowerCase())
                        || resultSet.getString(5).toLowerCase().contains(str.toLowerCase())) {
                    Tovar tovar = new Tovar();
                    tovar.setId(resultSet.getInt(1));
                    tovar.setName(resultSet.getString(3));
                    tovar.setBrand(resultSet.getString(4));
                    tovar.setDescription(resultSet.getString(5));
                    tovar.setPrice(resultSet.getInt(6));
                    tovar.setImg("/img/" + resultSet.getString(7));
                    tovar.setDisplay(resultSet.getBoolean(8));

                    ArrayList<Feedback> ratingList = DB.getFeedback(resultSet.getInt(1));
                    if(!ratingList.isEmpty()) {
                        float rating = 0;
                        for (Feedback f : ratingList)
                            rating += f.getRating();
                        tovar.setRating(rating / ratingList.size());
                    }

                    list.add(tovar);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err searchTovar");
        }
        return list;
    }

    public static Tovar getTovar(int id) {
        Tovar tovar = null;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `product` WHERE id = '" + id + "'");
            if (resultSet.next()){
                tovar = new Tovar();

                tovar.setId(resultSet.getInt(1));
                tovar.setName(resultSet.getString(3));
                tovar.setBrand(resultSet.getString(4));
                tovar.setDescription(resultSet.getString(5));
                tovar.setPrice(resultSet.getInt(6));
                tovar.setImg("/img/" + resultSet.getString(7));
                tovar.setDisplay(resultSet.getBoolean(8));;

                ArrayList<Feedback> ratingList = DB.getFeedback(resultSet.getInt(1));
                if(!ratingList.isEmpty()) {
                    float rating = 0;
                    for (Feedback f : ratingList)
                        rating += f.getRating();
                    tovar.setRating(rating / ratingList.size());
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err getTovar");
        }
        return tovar;
    }

    public static User getUser(String login, String password, String sessia){
        if (login.length() == 0 || password.length() == 0)
            return null;
        User user = null;

        String selectUserQuery = "SELECT * FROM user " +
                "INNER JOIN user_rights ON user.id = user_rights.id_user " +
                "INNER JOIN rights ON rights.id = user_rights.id_rights " +
                "WHERE login = ? AND password = ?";

        String updateSessionQuery = "UPDATE user SET sessia = ? WHERE login = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectUserQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateSessionQuery)) {
            DB.exitUser(sessia);

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                user = new User();
                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setSessia(resultSet.getString(5));
                user.setRule(resultSet.getInt(7));
                while (resultSet.next())
                    user.setRule(resultSet.getInt(7));
            }

            updateStatement.setString(1, sessia);
            updateStatement.setString(2, login);
            updateStatement.setString(3, password);

            updateStatement.executeUpdate();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err getUser1");
        }
        return user;
    }

    public static User getUser(String sessia) {
        User user = null;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `user` INNER JOIN user_rights ON user.id=user_rights.id_user INNER JOIN rights ON rights.id=user_rights.id_rights WHERE sessia = '" + sessia + "'");
            if(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setSessia(resultSet.getString(5));
                user.setRule(resultSet.getInt(7));
                while (resultSet.next())
                    user.setRule(resultSet.getInt(7));
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err getUser2");
        }
        return user;
    }

    public static User regUser(String name, String login, String password, String sessia){
        if (login.length() == 0 || password.length() == 0 || name.length() == 0)
            return null;
        User user = null;
        if(name.equals("") || password.equals(""))
            return user;

        String query = "INSERT INTO user (name, login, password) VALUES (?, ?, ?);";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            DB.exitUser(sessia);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `user` WHERE login = '" + login + "'");
            if(!resultSet.next()) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, login);
                preparedStatement.setString(3, password);

                preparedStatement.executeUpdate();

                statement.executeUpdate("INSERT INTO user_rights (id_user, id_rights) VALUES (LAST_INSERT_ID(), 1);");
                user = new User();
                user.setName(name);
                user.setRule(1);
            }
        } catch (Exception e) {
//            System.out.println(password);
//            e.printStackTrace();
            System.out.println("err regUser");
        }
        return user;
    }

    public static void exitUser(String sessia) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("UPDATE `user` SET `sessia`= NULL WHERE sessia = '" + sessia + "'");
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err exitUser");
        }
    }

    public static void deleteTovar(String id){
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement())
        {
            statement.executeUpdate("UPDATE `product` SET display = NOT display WHERE id = " + id);
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err deleteTovar");
        }
    }

    public static boolean addTovar(String name, String brand,  String description, String price,  String category, String img){
        boolean res = false;
        String query = "INSERT INTO `product` (`category`, `name`, `brand`, `description`, `price`, `img`) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `category` WHERE name = '" + category + "'");
            if (resultSet.next()) {
                preparedStatement.setInt(1, resultSet.getInt(1));
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, brand);

                if (description.isEmpty()) preparedStatement.setNull(4, Types.VARCHAR);
                else preparedStatement.setString(4, description);

                preparedStatement.setInt(5, Integer.parseInt(price));

                if (img.isEmpty()) preparedStatement.setNull(6, Types.VARCHAR);
                else preparedStatement.setString(6, img);

                preparedStatement.executeUpdate();

                res = true;
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err addTovar");
        }
        return res;
    }



    public static boolean canAddFeedback(User user, Tovar tovar){
        boolean res = false;

        String query = "SELECT CASE WHEN (EXISTS (SELECT 1 FROM history WHERE id_user = ? AND id_product = ?) " +
                "AND NOT EXISTS (SELECT 1 FROM feedback WHERE id_user = ? AND id_product = ?) " +
                "AND EXISTS (SELECT 1 FROM user_rights WHERE id_user = ? AND id_rights = 2)) " +
                "THEN 'true' ELSE 'false' END AS can_add_feedback";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, user.getId());
            preparedStatement.setInt(2, tovar.getId());
            preparedStatement.setInt(3, user.getId());
            preparedStatement.setInt(4, tovar.getId());
            preparedStatement.setInt(5, user.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) res = resultSet.getBoolean("can_add_feedback");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err canAddFeedback");
        }
        return res;
    }

    public static boolean addFeedback(User user, Tovar tovar, float rating, String commentary) {
        boolean res = false;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO `feedback`(`id_user`, `id_product`, `rating`, `text`)"
                    + " VALUES ('" + user.getId() + "', '" + tovar.getId() + "', '" + rating + "', '" + commentary + "')");
            res = true;
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err addFeedback");
        }
        return res;
    }

    public static ArrayList<Feedback> getFeedback(int id_tovar) {
        ArrayList<Feedback> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery("SELECT * FROM `feedback` INNER JOIN user ON user.id = id_user WHERE id_product = " + id_tovar);
            while (result.next()) {
                Feedback feedback = new Feedback();

                feedback.setId_tovar(result.getInt(2));
                feedback.setRating(result.getInt(3));
                feedback.setCommentary(result.getString(4));
                feedback.setName(result.getString(6));

                list.add(feedback);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err getFeedback");
        }
        return list;
    }

    public static void buyTovar(User user, Tovar tovar) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO `history`(`id_product`, `id_user`, `data`, `price`) VALUES ('" + tovar.getId() + "', '" + user.getId() + "', '" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())) + "', '" + tovar.getPrice() + "')");
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err buyTovar");
        }
    }

    public static void setUserVipRule(User user){
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `history` WHERE id_user = " + user.getId());
            int rowCount = 0;
            while (resultSet.next())
                rowCount++;
            if (rowCount >= 5)
                statement.executeUpdate("INSERT IGNORE INTO `user_rights` (id_user, id_rights) VALUES (" + user.getId() + ", 2);");
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("err setUserVipRule");
        }
    }

    public static ArrayList<Tovar> getHistory(User user){
        ArrayList<Tovar> list = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `history` INNER JOIN product ON product.id = id_product WHERE `id_user` = " + user.getId() + " ORDER BY data DESC");

            while (resultSet.next()){
                Tovar tovar = new Tovar();
                tovar.setId(resultSet.getInt(2));
                tovar.setData(resultSet.getString(4).substring(0, 16));
                tovar.setPrice(resultSet.getInt(5));
                tovar.setName(resultSet.getString(8));
                tovar.setBrand(resultSet.getString(9));
                tovar.setImg("/img/" + resultSet.getString(12));
                tovar.setDisplay(resultSet.getBoolean(13));

                ArrayList<Feedback> ratingList = DB.getFeedback(resultSet.getInt(2));
                if(!ratingList.isEmpty()) {
                    float rating = 0;
                    for (Feedback f : ratingList)
                        rating += f.getRating();
                    tovar.setRating(rating / ratingList.size());
                }

                list.add(tovar);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("err getHistory");
        }
        return list;
    }
}
