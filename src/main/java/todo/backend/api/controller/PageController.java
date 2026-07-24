package todo.backend.api.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import todo.backend.api.model.Task;
import todo.backend.api.model.Users;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
//@RequestMapping("/todo")
@RequestMapping

public class PageController {

    private final String API_URL = "https://tasktrackerbackend-6mlh.onrender.com/api/tasks";

    private final RestTemplate restTemplate;


    public PageController(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
   }

    @GetMapping("/signup")
    public String GetCreateUser() {
        return "signup";
    }


    @PostMapping("/signup")
    public String signup(@ModelAttribute Users users, Model model) {
        try {
            System.out.println("Before sending data to backend");
                String message = String.valueOf(restTemplate.postForObject("https://tasktrackerbackend-6mlh.onrender.com/register", users, String.class));
            System.out.println("Starting if statements");
               if(message.equals("Username Already Exists")) {
                   model.addAttribute("showPasswordMessage", "Username already exists");
               return "signup";
               }
               else if(message.equals("Email Already Exists"))
               {
                   model.addAttribute("showPasswordMessage", "Email already exists");
                   return "signup";
               }
               else if(message.equals("User was added successfully")){

                   return "redirect:/signin";
               }
               else{
                   System.out.println("This is else statement");
                   model.addAttribute("showPasswordMessage", message);
                   return "signup";
               }

        }catch(Exception e){
            System.out.println("Exception error on front end ");
            model.addAttribute("showPasswordMessage","Failed to create an account");
            return "signup";
        }
    }

    @GetMapping("/forgotPassword")
    public String getforgotpage(){
        return "forgotpassword";
    }

    @PostMapping("forgotPassword")
    public String forgotPassword(@ModelAttribute Users users,Model model){
        try{
            model.addAttribute("forgotPasswordMessage","");
            String message = String.valueOf(restTemplate.postForObject("https://tasktrackerbackend-6mlh.onrender.com/forgotpassword", users, String.class));

            if(message.equals("Link to reset the password was sent.")) {
                model.addAttribute("forgotPasswordMessage",message);
                return "redirect:/signin";
            }
            else if(message.equals("User with this email was not found")){
                model.addAttribute("forgotPasswordMessage",message);
                return "forgotpassword";
            }
            else if(message.equals("Link to reset the password hasn't expired,Check your emails")){
                model.addAttribute("forgotPasswordMessage", message);
                return "forgotpassword";
            }
            else if(message.contains("Something went wrong while sending the reset password email")){
                model.addAttribute("forgotPasswordMessage",message);
                return "forgotpassword";
            }
            else{
                model.addAttribute("forgotPasswordMessage","Email was not found");
                return "forgotpassword";
            }
        } catch (Exception e) {
            model.addAttribute("forgotPasswordMessage","Exception Error");
            return "forgotpassword";
        }
    }

    @GetMapping("/resetPassword")
    public String getResetPage(@RequestParam("resetPasswordToken") String resetPasswordToken, Model model){
        try {
            model.addAttribute("resetPasswordToken", resetPasswordToken);
            return "resetpassword";
        }catch(Exception e){
            return "resetpassword";
        }
        }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam(value = "resetPasswordToken") String resetPasswordToken, @ModelAttribute Users users, Model model){
        try{

System.out.println("Reset token on the path "+resetPasswordToken);
            String message = String.valueOf(restTemplate.postForObject("https://tasktrackerbackend-6mlh.onrender.com/resetpassword?resetPasswordToken="+resetPasswordToken, users, String.class));

            if(message.equals("Password was resetted")) {
                model.addAttribute("resetPasswordMessage","");
                return "redirect:/signin";
            }else if(message.equals("Use legitimate link to reset the password")){
                model.getAttribute("resetPasswordToken");
                model.addAttribute("resetPasswordToken", resetPasswordToken);
                model.addAttribute("resetPasswordMessage", message);
                return "resetpassword";
            }
            else if(message.equals("Link to reset the password is expired")){
                model.getAttribute("resetPasswordToken");
                model.addAttribute("resetPasswordToken", resetPasswordToken);
                model.addAttribute("resetPasswordMessage", message);
                return "resetpassword";
            }
            else if(message.equals("Token for the user was not found")){
                model.getAttribute("resetPasswordToken");
                model.addAttribute("resetPasswordToken", resetPasswordToken);
                model.addAttribute("resetPasswordMessage", message);
                return "resetpassword";
            }
            else{
                model.getAttribute("resetPasswordToken");
                model.addAttribute("resetPasswordToken", resetPasswordToken);
                model.addAttribute("resetPasswordMessage", "Failed to reset the password");
                return "resetpassword";
            }

        }catch(Exception e){
            model.getAttribute("resetPasswordToken");
            model.addAttribute("resetPasswordToken", resetPasswordToken);
            model.addAttribute("resetPasswordMessage","Failed to reset the password");
            return "resetpassword";
        }
    }

    @GetMapping("/emailverification")
    public String getEmailVerification(){ return "emailverification";}

    @GetMapping("/signin")
    public String getSignin() {
        return "signin";
    }

    @PostMapping("/signin")
    public String signin(
            @ModelAttribute Users user,
            HttpSession session,
            Model model
    ) {

        try {

            model.addAttribute("signinError", "");
            RestTemplate restTemplate = new RestTemplate();

            String url = "https://tasktrackerbackend-6mlh.onrender.com/login";

            Map<String, String> request = new HashMap<>();

            request.put("username", user.getUsername());
            request.put("password", user.getPassword());

        String message = restTemplate.postForObject(url,request,String.class);
        System.out.println("Message: "+ message);
        if(message.equals("Verify your account, Click verify button in your email")){

            model.addAttribute("signinError",message);
            return "signin";
        }
        else if(message.equals("Incorrect Credentials")){
            System.out.println("Incorrect Credentials");
            model.addAttribute("signinError",message);
            return "signin";
        }else if(message.equals("You entered unaccepted values")){
            System.out.println("Incorrect Credentials");
            model.addAttribute("signinError",message);
            return "signin";
        }
        else if(message.contains("Correct credentials now generating token for login ")){
            System.out.println("Signed In Token: "+message);
            String value = message.substring(51);
            session.setAttribute("token", value);
            return "redirect:/todos";
        }
        else{
            System.out.println("Incorrect Credentials");
            model.addAttribute("signinError",message);
            return "signin";
        }


        }
        catch(HttpClientErrorException.Unauthorized e){

            model.addAttribute("signinError", "Incorrect signin credentials");
            return "signin";
        }
    }


    @GetMapping("/todos")
    public String getTasks(
            HttpSession session,
            Model model
    ) {

        try {
            String token = session.getAttribute("token").toString();

            //if(token.isEmpty() || token == null) {
            if(token.isEmpty() || token.length() < 30){
                model.addAttribute("signinError","Unauthorized");
                return "redirect:/signin";
            }
            else{
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);

                HttpEntity<String> entity = new HttpEntity<>(headers);

                RestTemplate restTemplate = new RestTemplate();

                ResponseEntity<Task[]> response = restTemplate.exchange(
                        API_URL+"/todos",
                        HttpMethod.GET,
                        entity,
                        Task[].class
                );

                model.addAttribute("tasks",(response.getBody()));

                return "todos";
            }
        }
         catch (Exception e) {
            return "todos";
        }
    }

    public List<Long> countNumberOfDays(List<Task> mytasks){
        LocalDate today = LocalDate.now();

        List<Task> datetasks = mytasks;

        ArrayList<LocalDate> mydate2 = new ArrayList<>();

        for (Task task : datetasks) {

            if(!task.getDueDate().isEmpty() && task.getDueDate() != null){
                mydate2.add(LocalDate.parse(task.getDueDate()));
            }

        }

        ArrayList<Long> daysLeft = new ArrayList<>();
        for(LocalDate days : mydate2) {
            Long dd = ChronoUnit.DAYS.between(today, days);
            daysLeft.add(dd);
        }

        return daysLeft;
    }


    @GetMapping("/todos/search")
    public String searchTodos(@RequestParam(value="searchword", required = false) String searchword,
                              HttpSession session,
                              Model model) {

        try {
            String token = (String) session.getAttribute("token");

            if(token.isEmpty() || token == null) {
                return "redirect:/signin";
            }
            else{

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);

                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Task> entity = new HttpEntity<>(headers);
                ResponseEntity<Task[]> response = restTemplate.exchange(
                        API_URL+"/todos",
                        HttpMethod.GET,
                        entity,
                        Task[].class
                );
                List<Task> tasks = Arrays.asList(response.getBody());
                List<Task> searchResults = new ArrayList<>();

                if (searchword.isEmpty()) {
                    searchResults = tasks;
                } else {
                    for (Task task : tasks) {
                        if (task.getTitle().toLowerCase().contains(searchword.toLowerCase()) || task.getDescription().toLowerCase().contains(searchword.toLowerCase()) ||
                                task.getPriority().toLowerCase().contains(searchword.toLowerCase()) || task.getDueDate().contains(searchword) || String.valueOf(task.getCreated()).contains(searchword)) {
                            searchResults.add(task);
                        }
                    }
                }
                model.addAttribute("searchword", searchword);
                model.addAttribute("tasks", searchResults);
                return "searchResult";
            }
        }
        catch(Exception e){
            return "redirect:/todos";
        }
    }


    @GetMapping("/todo/{id}")
        public String viewSingleTask(
            @PathVariable Long id,
            Model model,
            HttpSession session
            ) {

        try {

            String token = (String) session.getAttribute("token");

            if(token.isEmpty() || token == null) {
                return "redirect:/signin";
            }
            else{
                HttpHeaders headers = new HttpHeaders();

                headers.set("Authorization", "Bearer " + token);

                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<Task> response = restTemplate.exchange(
                        API_URL + "/todo/{id}",
                        HttpMethod.GET,
                        entity,
                        Task.class,
                        id
                );
                Task task = response.getBody();

                model.addAttribute("tasks", task);

                return "viewSingleTask";
            }

        }
        catch (Exception e) {
            return "viewSingleTask";
        }
    }


    @GetMapping("/todo")
    public String getPage(HttpSession session){
        try {
            String token = (String) session.getAttribute("token");

            if(token.isEmpty() || token == null) {
                return "redirect:/signin";
            }
            else{
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer "+ token);
                return "todo";
            }
        }
        catch (Exception e) {
            return "todo";
        }
    }

    @PostMapping("/todo/save/task")
    public String saveTask(@ModelAttribute Task task,
                           HttpSession session) {

        try {
            String token = session.getAttribute("token").toString();

            if(token.isEmpty() || token == null) {
                return "redirect:/signin";
            }
            else{

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);

                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Task> entity = new HttpEntity<>(task, headers);
                restTemplate.exchange(
                        API_URL + "/todo/save/task",
                        HttpMethod.POST,
                        entity,
                        Task.class
                );

                return "redirect:/todos";
            }

        }
        catch (Exception e) {
            return "todos";
        }
    }

    @PutMapping("/todo/{id}")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute Task task,
                             HttpSession session){

        try {

            String token = (String) session.getAttribute("token");

            if(token.isEmpty() || token == null) {
                return "redirect:/signin";
            }

            else{

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);

                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Task> entity = new HttpEntity<>(task, headers);
                restTemplate.exchange(API_URL + "/todo/{id}",
                        HttpMethod.PUT,
                        entity,
                        Task.class,
                        id);

                return "redirect:/todos";

            }
        }
        catch (Exception e) {
            return "redirect:/todos";
        }
    }

      @GetMapping("/completedTasks")
      public String getCompletedTasks(
              HttpSession session,
              Model model){

        try {
            String token = session.getAttribute("token").toString();

            if(token.isEmpty() || token == null) {
                return "redirect:/signin";
            }
            else{

                HttpHeaders headers = new HttpHeaders();

                headers.set("Authorization", "Bearer " + token);

                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<Task[]> response = restTemplate.exchange(
                        API_URL + "/completed",
                        HttpMethod.GET,
                        entity,
                        Task[].class
                );

                List<Task> completedTaskss = Arrays.asList(response.getBody());
                model.addAttribute("tasks", completedTaskss);
                return "CompletedTasks";
            }
        }
        catch (Exception e) {
            return "CompletedTasks";
        }
      }


    @PatchMapping("/todo/{id}")
    public String updateCompletion(@PathVariable Long id,
                                   @ModelAttribute Task task,
                                   HttpSession session){

        try {
            String token = (String) session.getAttribute("token");

            if(token.isEmpty() || token == null) {
                return "redirect:/signin";
            }
            else{
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Task> entity = new HttpEntity<>(task, headers);
            restTemplate.exchange(
                    API_URL + "/todo/{id}",
                    HttpMethod.PATCH,
                    entity,
                    Task.class,
                    id);
            return "redirect:/todos";
        }
        }
        catch (Exception e) {
            return "redirect:/todos";
        }
    }


    @DeleteMapping("/todo/{id}")
    public String deleteTask(@PathVariable Long id,
                             HttpSession session) {

        try {
            String token = (String) session.getAttribute("token");

            if(token.isEmpty() || token == null) {
                return "redirect:/signin";
            }
            else{

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);

                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Task> entity = new HttpEntity<>(headers);
                restTemplate.exchange(
                        API_URL + "/todo/{id}",
                        HttpMethod.DELETE,
                        entity,
                        Task.class,
                        id
                );

                return "redirect:/todos";
            }

        }
        catch (Exception e) {
            return "redirect:/todos";
        }
    }



}
