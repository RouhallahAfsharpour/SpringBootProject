package com.example.login_project.controller;
import com.example.login_project.model.Employee;
import com.example.login_project.model.QuestionBank;
import com.example.login_project.service.MainService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class MainController {

    private final MainService mainService;

    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("/")
    public String rootUrl(){
        return "index.html";
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
                                @RequestParam(value = "tel", required = false) String tel) {
        if(mainService.checkEmail(email)){
            return "already_registered.html";
        }else {
            mainService.addNewEmployee(firstName, lastName, gender, email, tel);
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
    @GetMapping("/deleteQuestionFromQuestionnaire")
    public String deleteQuestionQuestionnaire(@RequestParam(value = "nameOfQuestionnaire", required = false) String questionnaire,
                                              @RequestParam(value = "idQuestion", required = false) int questionID, Model model) throws SQLException {
        System.out.println("qn: "+questionnaire+" "+questionID);
        mainService.deleteQuestionFromQuestionnaire(questionnaire,questionID);

        List<QuestionBank> questions = mainService.getAllQuestions(questionnaire);
        model.addAttribute("questionnaireName", questionnaire);
        model.addAttribute("questions", questions);
        model.addAttribute("questionCount", questions.size());
        return "edit_questions.html";
    }


}

