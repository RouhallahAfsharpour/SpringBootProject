package com.example.login_project.controller;
import com.example.login_project.model.CityWeather;
import com.example.login_project.model.Currency;
import com.example.login_project.model.Employee;
import com.example.login_project.model.QuestionBank;
import com.example.login_project.service.MainService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class MainController {

    public static List<CityWeather> weatherAppGermany = new ArrayList<>();

    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/")
    public String rootUrl(){
        return "index.html";
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        String message = "";
        model.addAttribute("message", message);
        return "login.html";
    }

    @PostMapping(value = "/loginRequest")
    public String loginRequest(@RequestParam(value = "email", required = false) String email,
                               @RequestParam(value = "password", required = false) String pass,Model model) throws SQLException {
        if(mainService.validateUser(email,pass)){
            String firstName = mainService.getFirstName(email);
            String message = "Hello "+firstName+"! ";
            model.addAttribute("message", message);
            return "index.html";
        }else {
            String message = "The information is not correct or you are not registered yet!";
            model.addAttribute("message", message);
            return "login.html";
        }

    }

    @GetMapping("/RegisterNewEmployee")
    public String register(){
        return "register_employee.html";
    }

    //EditDeleteAQuestionnaireList
    @GetMapping("/EditDeleteAQuestionnaireList")
    public String editDeleteAQuestionnaire(Model model){
        List<String> questionnaireTableNames = MainService.getAllQuestionnaireTableNames();
        model.addAttribute("questionnaireCount", questionnaireTableNames.size());
        model.addAttribute("questionnaires", questionnaireTableNames);
        return "list_questionnaire_Edit_Delete.html";
    }

    @GetMapping("/CreateNewQuestionnaire")
    public String createNewQuestionnaire(){
        List<String> questionnaireTableNames = MainService.getAllQuestionnaireTableNames();
        return "make_new_questionnaire.html";
    }

    @PostMapping(value = "/makeQuestion")
    public String makeQuestions(@RequestParam(value = "questionnaireName", required = false) String questionnaireName,Model model) throws SQLException {
        model.addAttribute("questionnaire", questionnaireName);
        return "make_questions.html";
    }

    //takePartInQuestionnaire
    @GetMapping("/takePartInQuestionnaire")
    public String takePart(Model model){
        List<String> questionnaireTableNames = MainService.getAllQuestionnaireTableNames();
        model.addAttribute("questionnaireCount", questionnaireTableNames.size());
        model.addAttribute("questionnaires", questionnaireTableNames);
        return "start_questionnaire.html";
    }

    @RequestMapping(value = "/listOfAllEmployees", method = RequestMethod.GET)
    public String getEmployees(Model model) {
        List<Employee> employees = mainService.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("employeeCount", employees.size());
        return "all_employees.html";
    }

    @RequestMapping(value = "/listOfAllEmployeesDB", method = RequestMethod.GET)
    public String getEmployeesDB(Model model) throws SQLException {
        List<Employee> employees = mainService.getAllEmployeesDB();
        model.addAttribute("employees", employees);
        model.addAttribute("employeeCount", employees.size());
        return "all_employees.html";
    }

    @PostMapping(value = "/addNewEmployeeToList")
    public String createEmployee(@RequestParam(value = "firstName", required = false) String firstName,
                                @RequestParam(value = "lastName", required = false) String lastName,
                                @RequestParam(value = "gender", required = false) String gender,
                                @RequestParam(value = "email", required = false) String email,
                                @RequestParam(value = "tel", required = false) String tel,
                                 @RequestParam(value = "tel", required = false) String password) {
        if(mainService.checkEmail(email)){
            return "already_registered.html";
        }else {
            mainService.addNewEmployee(firstName, lastName, gender, email, tel,password);
            return "register_employee.html";
        }
    }

    @GetMapping("/deleteEmployee")
    public String deleteEmployee(@RequestParam String email) {
        mainService.deleteEmployee(email);
        return "redirect:/listOfAllEmployeesDB";
    }

    @PostMapping(value = "/questions")
    public String getQuestions(@RequestParam(value = "questionnaireName", required = false) String questionnaireName,Model model) throws SQLException {
        List<QuestionBank> questions = mainService.getAllQuestions(questionnaireName);
        model.addAttribute("questions", questions);
        model.addAttribute("questionCount", questions.size());
        return "questions.html";
    }

    @RequestMapping(value = "/questionsList", method = RequestMethod.GET)
    public String getQuestionsUpdated(Model model) throws SQLException {
        List<QuestionBank> questions = mainService.getAllQuestionsUpdated();
        if(!questions.isEmpty()){
            model.addAttribute("questions", questions);
            model.addAttribute("questionCount", questions.size());
            return "questions.html";
        } else {
            return "all_questions_answered.html";
        }
    }

    //questionsListSendAnswer
    @PostMapping(value = "/questionsListSendAnswer")
    public String submitAnswer(@RequestParam(value = "id", required = false) int questionID,
                               @RequestParam(value = "ans", required = false) int ans) throws SQLException {
        mainService.sendAnswer(questionID,ans);
        return "redirect:/questionsList";
    }

    @RequestMapping(value = "/AddQuestionToAQuestionnaire", method = RequestMethod.GET)
    public String addQuestionnaireDB(Model model) throws SQLException {
        String message ="Choose a questionnaire to add new questions to it";
        List<String> questionnaireTableNames = MainService.getAllQuestionnaireTableNames();
        model.addAttribute("message", message);
        model.addAttribute("questionnaireCount", questionnaireTableNames.size());
        model.addAttribute("questionnaires", questionnaireTableNames);
        return "list_questionnaire.html";
    }

    @PostMapping(value = "/CreateNewQuestionnaireInDB")
    public String createNewQuestionnaireDB(@RequestParam(value = "questionnaireName", required = false) String questionnaireName,Model model) throws SQLException {
        String message ="";
        List<String> questionnaireTableNames = MainService.getAllQuestionnaireTableNames();
        if (questionnaireTableNames.contains("questionnaire_"+questionnaireName)){
            message = "Sorry! a Table named "+questionnaireName+" already exists in database, please choose another name!";
            model.addAttribute("message", message);
            return "make_new_questionnaire.html";

        }else {
            mainService.createNewQuestionnaireInDB(questionnaireName);
            questionnaireTableNames = MainService.getAllQuestionnaireTableNames();
            message = "Super! a Table named "+questionnaireName+" was created successfully!";
            model.addAttribute("message", message);
            model.addAttribute("questionnaireCount", questionnaireTableNames.size());
            model.addAttribute("questionnaires", questionnaireTableNames);
            return "list_questionnaire.html";
        }
    }

    @PostMapping(value = "/addNewQuestionToQuestionnaire")
    public String addNewQuestionToQuestionnaire(@RequestParam(value = "questionnaireName", required = false) String questionnaire,
                                                @RequestParam(value = "question", required = false) String question,
                                                @RequestParam(value = "option1", required = false) String option1,
                                                @RequestParam(value = "option2", required = false) String option2,
                                                @RequestParam(value = "option3", required = false) String option3,
                                                @RequestParam(value = "option4", required = false) String option4,
                                                @RequestParam(value = "answer", required = false) String answer, Model model) {
        mainService.addNewQuestion(questionnaire,question,option1,option2,option3,option4,answer);
        model.addAttribute("questionnaire", questionnaire);
        return "make_questions.html";
    }

    //deleteQuestionnaire
    @GetMapping("/deleteQuestionnaire")
    public String deleteQuestionnaire(@RequestParam String questionnaireName) {
        System.out.println("qn: "+questionnaireName);
        mainService.deleteQuestionnaire(questionnaireName);
        return "redirect:/EditDeleteAQuestionnaireList";
    }

    //EditQuestionnaire
    @PostMapping(value = "/EditQuestionnaire")
    public String editQuestions(@RequestParam(value = "questionnaireName", required = false) String questionnaireName,Model model) throws SQLException {
        List<QuestionBank> questions = mainService.getAllQuestions(questionnaireName);
        model.addAttribute("questionnaireName", questionnaireName);
        model.addAttribute("questions", questions);
        model.addAttribute("questionCount", questions.size());
        return "edit_questions.html";
    }

    //edit each question
    @PostMapping(value = "/EditAQuestionOfQuestionnaire")
    public String getQuestionForEdit(@RequestParam(value = "id", required = false) int questionID,
                                     @RequestParam(value = "questionnaireName", required = false) String questionnaireName, Model model) throws SQLException {
        QuestionBank questionForEdit = mainService.getQuestionForEdit(questionnaireName,questionID);

        System.out.println("id: "+questionForEdit.getId());
        model.addAttribute("questionnaire",questionnaireName);
        model.addAttribute("idQuestion",questionID);
        model.addAttribute("question",questionForEdit.getQuestion());
        model.addAttribute("option1", questionForEdit.getOption1());
        model.addAttribute("option2", questionForEdit.getOption2());
        model.addAttribute("option3", questionForEdit.getOption3());
        model.addAttribute("option4", questionForEdit.getOption4());
        model.addAttribute("answer",  questionForEdit.getAnswer());

        return "show_question_for_edit.html";
    }


    //EditThisQuestionOfQuestionnaire
    @PostMapping(value = "/EditThisQuestionOfQuestionnaire")
    public String editThisQuestionQuestionnaire(@RequestParam(value = "questionnaireName", required = false) String questionnaire,
                                                @RequestParam(value = "idOfQuestion", required = false) int questionID,
                                                @RequestParam(value = "question", required = false) String question,
                                                @RequestParam(value = "option1", required = false) String option1,
                                                @RequestParam(value = "option2", required = false) String option2,
                                                @RequestParam(value = "option3", required = false) String option3,
                                                @RequestParam(value = "option4", required = false) String option4,
                                                @RequestParam(value = "answer", required = false) String answer, Model model) throws SQLException {

        mainService.editThisQuestion(questionnaire,questionID,question,option1,option2,option3,option4,answer);

        List<QuestionBank> questions = mainService.getAllQuestions(questionnaire);
        model.addAttribute("questionnaireName", questionnaire);
        model.addAttribute("questions", questions);
        model.addAttribute("questionCount", questions.size());
        return "edit_questions.html";
    }

    //deleteQuestionFromQuestionnaire
    @PostMapping(value = "/deleteQuestionFromQuestionnaire")
    public String deleteQuestionQuestionnaire(@RequestParam(value = "nameOfQuestionnaire", required = false) String questionnaire,
                                              @RequestParam(value = "idQuestion", required = false) int questionID, Model model) throws SQLException {
        mainService.deleteQuestionFromQuestionnaire(questionnaire,questionID);

        List<QuestionBank> questions = mainService.getAllQuestions(questionnaire);
        model.addAttribute("questionnaireName", questionnaire);
        model.addAttribute("questions", questions);
        model.addAttribute("questionCount", questions.size());
        return "edit_questions.html";
    }

    @GetMapping("/calculateFromUSD")
    public String calculateFromUSD(Model model){
        Double convertedFromUSD = 0.0;
        model.addAttribute("convertedFromUSD", convertedFromUSD);
        return "currency_from_USD.html";
    }


    @PostMapping(value = "/calculateFromUSD")
    public String createEmployee(@RequestParam(value = "amount", required = false) Double amount,
                                 @RequestParam(value = "currency", required = false) String currency, Model model) throws ExecutionException, InterruptedException {
        Double convertedFromUSD = mainService.calculateFromUSD(amount,currency);
        model.addAttribute("convertedFromUSD", convertedFromUSD);
        model.addAttribute("amountConverted", amount);
        model.addAttribute("currencyConverted", " "+currency);
        model.addAttribute("currencyFrom", " USD equals to: ");
        return "currency_from_USD.html";
    }


    @GetMapping("/currencyConverter")
    public String currencyConverter(Model model) throws ExecutionException, InterruptedException {
        Double currencyConverterAmount = 0.0;
        List<Currency> allCurrencies = mainService.allCurrencies();
        model.addAttribute("allCurrencies", allCurrencies);
        model.addAttribute("currencyConverterAmount", currencyConverterAmount);
        return "currency_converter.html";
    }

    @PostMapping(value = "/convertCurrency")
    public String convertCurrency(@RequestParam(value = "amount", required = false) Double amount,
                                  @RequestParam(value = "currencyFrom", required = false) String currencyFrom,
                                 @RequestParam(value = "currencyTo", required = false) String currencyTo, Model model) throws ExecutionException, InterruptedException {
        Double convertedResult = mainService.convertCurrency(amount,currencyFrom,currencyTo);
        model.addAttribute("convertedResult", convertedResult);
        model.addAttribute("amountConverted", amount);
        model.addAttribute("currencyConverted", " "+currencyTo);
        model.addAttribute("currencyFrom", currencyFrom+" equals to: ");
        List<Currency> allCurrencies = mainService.allCurrencies();
        model.addAttribute("allCurrencies", allCurrencies);
        return "currency_converter.html";
    }

    @RequestMapping(value = "/weatherAppGermany", method = RequestMethod.GET)
    public String weatherAppGermany(Model model) throws ExecutionException, InterruptedException {
        weatherAppGermany = mainService.weatherAppGermany();
        model.addAttribute("weatherGermany", weatherAppGermany);
        model.addAttribute("numberOfCities", weatherAppGermany.size());
        return "weather_Germany.html";
    }

    @RequestMapping(value = "/sortTemperatureGermanyUp", method = RequestMethod.GET)
    public String sortTemperatureGermanyUp(Model model) throws ExecutionException, InterruptedException {
        Collections.sort(weatherAppGermany, Comparator.comparing(CityWeather::getCurrentTemp));
        model.addAttribute("weatherGermany", weatherAppGermany);
        model.addAttribute("numberOfCities", weatherAppGermany.size());
        return "weather_Germany.html";
    }

    @RequestMapping(value = "/sortTemperatureGermanyDown", method = RequestMethod.GET)
    public String sortTemperatureGermanyDown(Model model) throws ExecutionException, InterruptedException {
        Collections.sort(weatherAppGermany, Comparator.comparing(CityWeather::getCurrentTemp).reversed());
        model.addAttribute("weatherGermany", weatherAppGermany);
        model.addAttribute("numberOfCities", weatherAppGermany.size());
        return "weather_Germany.html";
    }

    @RequestMapping(value = "/sortCityNameGermanyUp", method = RequestMethod.GET)
    public String sortCityNameGermanyUp(Model model) throws ExecutionException, InterruptedException {
        Collections.sort(weatherAppGermany, Comparator.comparing(CityWeather::getCityName));
        model.addAttribute("weatherGermany", weatherAppGermany);
        model.addAttribute("numberOfCities", weatherAppGermany.size());
        return "weather_Germany.html";
    }

    @RequestMapping(value = "/sortCityNameGermanyDown", method = RequestMethod.GET)
    public String sortCityNameGermanyDown(Model model) throws ExecutionException, InterruptedException {
        Collections.sort(weatherAppGermany, Comparator.comparing(CityWeather::getCityName).reversed());
        model.addAttribute("weatherGermany", weatherAppGermany);
        model.addAttribute("numberOfCities", weatherAppGermany.size());
        return "weather_Germany.html";
    }

}

