package com.example.login_project.repository;
import com.example.login_project.model.CityWeather;
import com.example.login_project.model.Currency;
import com.example.login_project.model.Employee;
import com.example.login_project.model.QuestionBank;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.w3c.dom.DOMImplementation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MainRepository {

    @Configuration
    public class AppConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    public static List<Employee> allEmployees = new ArrayList<>();
    public static List<Employee> allEmployeesDB = new ArrayList<>();
    public static List<QuestionBank> allQuestions = new ArrayList<>();
    public static List<String> QuestionnaireTableNames = new ArrayList<>();
    public static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);

    //PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    public static String jdbcURL = "jdbc:mysql://localhost:3306/employees";
    public static String jdbcURLQuestionnaires = "jdbc:mysql://localhost:3306/questionnaires";
    public static String username = "root";
    public static String password = "1234";


    public static void addEmployee(String firstName, String lastName, String gender, String email, String tel, String pass){

        Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
        String encodedPassword =passwordEncoder.encode(pass);
        System.out.println(encodedPassword);

        allEmployees.add(new Employee(firstName, lastName, gender, email, tel, encodedPassword));

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
        ) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO workers(first_name, last_name, gender, email, telephone, password)values(?,?,?,?,?,?);");
            ps.setString(1,firstName);
            ps.setString(2,lastName);
            ps.setString(3,gender);
            ps.setString(4,email);
            ps.setString(5,tel);
            ps.setString(6,encodedPassword);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getFirstName(String email){
        String firstName="";
        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT first_name FROM workers WHERE email= '"+email+"'");
        ) {
            while(rs.next()){
                firstName= rs.getString("first_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firstName;
    }

    public static boolean checkPass(String email,String pass){
        Pbkdf2PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
        String encodedPassword ="3768a96ed94ad1deeebf9ffd36560ab080dc5f6e4eba3076aed806002094bcc570ec37eafaec3b07";
        boolean check =false;
        String sql ="SELECT password FROM workers WHERE email= '"+email+"'";

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
        ) {
            while(rs.next()){
                //if(passwordEncoder.matches(pass,rs.getString("password"))){
                if(passwordEncoder.matches(pass,encodedPassword)){
                    System.out.println("correct pass");
                    check=true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }

    //deleteUser
    public static void deleteEmployee(String email){

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
        ) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM workers WHERE email = ?");
            ps.setString(1,email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Employee> getAllEmployees(){
        List<Employee> employees = allEmployees;
        return employees;
    }

    public  static boolean checkEmail(String email){
        boolean check =false;
        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT email FROM workers");
        ) {
            while(rs.next()){
                if(rs.getString("email").equals(email)){
                    check=true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return check;
    }

    public static List<Employee> getAllEmployeesDB() throws SQLException {

        allEmployeesDB.clear();

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT first_name, last_name, gender, email, telephone FROM workers");
        ) {
            while(rs.next()){
                allEmployeesDB.add(new Employee(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("email"),
                        rs.getString("telephone")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Employee> EmployeesFromDB = allEmployeesDB;
        return EmployeesFromDB;
    }

    public static List<String> getAllQuestionnaireTableNames(){
        QuestionnaireTableNames.clear();
        try (Connection connection = DriverManager.getConnection(jdbcURLQuestionnaires,username,password);){
            DatabaseMetaData dbmd = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "questionnaire%", types);
            while (rs.next()) {
                QuestionnaireTableNames.add(rs.getString("TABLE_NAME"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return QuestionnaireTableNames;
    }

    public static void createNewQuestionnaireInDB(String questionnaireName) throws SQLException {
        try(Connection connection = DriverManager.getConnection(jdbcURLQuestionnaires,username,password);
            Statement stmt = connection.createStatement();
        ) {
            String sqlCreate = "CREATE TABLE IF NOT EXISTS " + "questionnaire_"+questionnaireName
                    + " (id INT(11) NOT NULL AUTO_INCREMENT,"
                    + "question VARCHAR(450),"
                    + "option1 VARCHAR(50),"
                    + "option2 VARCHAR(50),"
                    + "option3 VARCHAR(50),"
                    + "option4 VARCHAR(50),"
                    + "answer INTEGER,PRIMARY KEY (id))";

            stmt.execute(sqlCreate);
        }
    }

    public static void addNewQuestion(String questionnaire ,String question, String option1, String option2, String option3, String option4, String answer){
        QuestionBank newQuestion = new QuestionBank(question,option1,option2,option3,option4,answer);

        try(Connection connection = DriverManager.getConnection(jdbcURLQuestionnaires,username,password);
            Statement stmt = connection.createStatement();
        ) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO "+questionnaire+"(question, option1, option2, option3, option4, answer)values(?,?,?,?,?,?);");
            ps.setString(1,question);
            ps.setString(2,option1);
            ps.setString(3,option2);
            ps.setString(4,option3);
            ps.setString(5,option4);
            ps.setString(6,answer);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static List<QuestionBank> getAllQuestions(String questionnaireName) throws SQLException {

        allQuestions.clear();

        try(Connection connection = DriverManager.getConnection(jdbcURLQuestionnaires,username,password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, question, option1, option2, option3, option4, answer FROM "+questionnaireName);
        ) {
            while(rs.next()){
                allQuestions.add(new QuestionBank(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        rs.getString("answer")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<QuestionBank> allQuestionsFromDB = allQuestions;
        return allQuestionsFromDB;
    }

    //Updated
    public static List<QuestionBank> getAllQuestionsUpdated(){
        return allQuestions;
    }

    public static void sendAnswer(int questionID, int ans){
        String email ="abadanastro@gmail.com";
        System.out.println(questionID+"= "+ans);

        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password);
            Statement stmt = connection.createStatement();
        ) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO answers(question_id, submitted_answer)values(?,?);");
            ps.setInt(1,questionID);
            ps.setInt(2,ans);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteQuestion(int questionID){

        List<QuestionBank> allQuestionsUp = getAllQuestionsUpdated();
        /*for(QuestionBank question : allQuestionsUp){
            if(question.getId()==questionID){
                allQuestionsUp.remove(question);
            }
        }*/
        allQuestionsUp.removeIf(obj -> obj.getId() == questionID);
        System.out.println("s"+allQuestionsUp.size());
        allQuestions=allQuestionsUp;
    }


    //delete questionnaire
    public static void deleteQuestionnaire(String questionnaire){
        try(Connection connection = DriverManager.getConnection(jdbcURLQuestionnaires,username,password);
            Statement stmt = connection.createStatement();
        ) {
            String sql="DROP TABLE "+questionnaire;
            stmt.executeUpdate(sql);
            //PreparedStatement ps = connection.prepareStatement("DROP TABLE "+questionnaire);
            //ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //getQuestionForEdit
    public static QuestionBank getQuestionForEdit(String questionnaireName,int questionID){
        QuestionBank questionForEdit= new QuestionBank();

        try(Connection connection = DriverManager.getConnection(jdbcURLQuestionnaires,username,password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, question, option1, option2, option3, option4, answer FROM "+questionnaireName+" WHERE id="+questionID);

        ) {
            while(rs.next()) {

            questionForEdit= new QuestionBank(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        Integer.toString(rs.getInt("answer")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questionForEdit;
    }

    public static void editThisQuestion(String questionnaire, int questionID,String question, String option1, String option2, String option3, String option4, String answer){

        //System.out.println("before update: "+questionnaire+questionID+question+option1+option2+option3+option4+answer);
        try(Connection connection = DriverManager.getConnection(jdbcURLQuestionnaires,username,password);
            Statement stmt = connection.createStatement();
        ) {
            String sql="UPDATE "+questionnaire+" SET question = '"+question+"', option1 = '"+option1+
                    "', option2 = '"+option2+"', option3 = '"+option3+
                    "', option4 = '"+option4+"', answer = '"+answer+
                    "' WHERE id= "+questionID;
            System.out.println(sql);
            stmt.executeUpdate(sql);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteQuestionFromQuestionnaire(String questionnaireName,int questionID){
        try(Connection connection = DriverManager.getConnection(jdbcURLQuestionnaires,username,password);
            Statement stmt = connection.createStatement();
        ) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM "+questionnaireName+" WHERE id = ?");
            ps.setInt(1,questionID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static Double calculateFromUSD(Double amount, String currencyName) throws ExecutionException, InterruptedException {

        //1- Create HttpClient object
        HttpClient client = HttpClient.newHttpClient();
        //2- Build HttpRequest
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(
                        URI
                                .create("https://api.currencyapi.com/v3/latest?apikey=9zFynTI1VtNgjXacCju0ilK9T9uyg7r7uWnsY5wf")
                ).GET()//the HTTP request method
                .build();//building the request object
        // then we can send this request using our client, and we wanted to send asynchronously

        CompletableFuture<Object> response = client.sendAsync(
                        //pass the request object as the first parameter
                        request,//our HttpRequest instance
                        HttpResponse.BodyHandlers.ofString()
                )
                //the second parameter tells the server which response body type(as string) we want to receive
                .thenApply(HttpResponse::body);

        String keys= response.get().toString();
        System.out.println(keys);
        HashMap<String, Map<String, Map<String, Double>>> map = new Gson().fromJson(keys, HashMap.class);
        System.out.println(map.get("data").keySet());


        System.out.println(map.get("data").get(currencyName));
        System.out.println(map.get("data").get(currencyName).get("value"));
        System.out.println(amount*map.get("data").get(currencyName).get("value"));
        return amount*map.get("data").get(currencyName).get("value");
    }

    public static List<Currency> allCurrencies() throws ExecutionException, InterruptedException {
        //1- Create HttpClient object
        HttpClient client = HttpClient.newHttpClient();
        //2- Build HttpRequest
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(
                        URI
                                .create("https://api.currencyapi.com/v3/latest?apikey=9zFynTI1VtNgjXacCju0ilK9T9uyg7r7uWnsY5wf")
                ).GET()//the HTTP request method
                .build();//building the request object
        // then we can send this request using our client, and we wanted to send asynchronously

        CompletableFuture<Object> response = client.sendAsync(
                        //pass the request object as the first parameter
                        request,//our HttpRequest instance
                        HttpResponse.BodyHandlers.ofString()
                )
                //the second parameter tells the server which response body type(as string) we want to receive
                .thenApply(HttpResponse::body);

        String keys= response.get().toString();
        System.out.println(keys);
        HashMap<String, Map<String, Map<String, Double>>> map = new Gson().fromJson(keys, HashMap.class);

        List<Currency> allCurrencies = new ArrayList<>();
        for(String key : map.get("data").keySet()){
            allCurrencies.add(new Currency(key,map.get("data").get(key).get("value")));
        }

        return allCurrencies;
    }

    public static Double convertCurrency(Double amount,String currencyFrom,String currencyTo) throws ExecutionException, InterruptedException {
        List<Currency> allCurrencies =allCurrencies();
        Double exchangeRateFrom =1.0;
        Double exchangeRateTo =1.0;

        for (Currency c : allCurrencies){
            if (c.getCurrencyName().equals(currencyFrom)){
                exchangeRateFrom =c.getExchangeRate();
            }
            if (c.getCurrencyName().equals(currencyTo)){
                exchangeRateTo =c.getExchangeRate();
            }
        }

        return Math.round(((amount/exchangeRateFrom)*exchangeRateTo)*100.0)/100.0;
    }


    public static List<CityWeather> weatherAppGermany() throws ExecutionException, InterruptedException {

        String[] cityNamesGermany = {"Berlin","Hamburg","Munich","Cologne","Frankfurt","Stuttgart","Düsseldorf","Dortmund","Essen",
                "Leipzig","Bremen","Dresden","Hanover","Nuremberg","Duisburg","Bochum","Wuppertal","Bielefeld","Bonn","Münster",
                "Karlsruhe","Mannheim","Augsburg","Wiesbaden","Mönchengladbach","Gelsenkirchen","Braunschweig","Kiel","Chemnitz",
                "Aachen","Halle","Mainz","Erfurt","Oberhausen","Rostock","Freiburg","Magdeburg",
                "Kassel","Hagen","Saarbrücken","Hamm","Potsdam","Mülheim","Ludwigshafen","Oldenburg","Osnabrück","Leverkusen",
                "Heidelberg","Solingen","Darmstadt","Herne","Neuss","Regensburg","Paderborn","Ingolstadt",
                "Würzburg","Fürth","Ulm","Heilbronn","Pforzheim","Wolfsburg","Göttingen","Bottrop","Reutlingen","Koblenz",
                "Recklinghausen","Bremerhaven","Jena","Erlangen","Remscheid","Trier","Salzgitter","Moers",
                "Siegen","Hildesheim","Cottbus"};

        List<CityWeather> cityWeatherList = new ArrayList<>();

        for (String city : cityNamesGermany){

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create("http://api.weatherapi.com/v1/current.json?key=f7d89f3d5a3d4f5999381115221603&q=" + city)
                    ).GET().build();

            client.sendAsync(request,HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .join();

            CompletableFuture<Object> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body);

            String keys= response.get().toString().replace("[","");
            HashMap<String, Map<String, Object>> map = new Gson().fromJson(keys, HashMap.class);
            cityWeatherList.add(new CityWeather(city,new Double(map.get("current").get("temp_c").toString()),new Double(map.get("current").get("humidity").toString()),
                    map.get("current").get("condition").toString().split(",")[0].substring(6)));
        }

        return cityWeatherList;
    }
}
