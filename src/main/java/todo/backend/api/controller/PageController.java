package todo.backend.api.controller;

import jakarta.servlet.http.HttpSession;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import todo.backend.api.model.Notification;
import todo.backend.api.model.Task;
import todo.backend.api.model.Users;

import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
//@RequestMapping("/todo")
@RequestMapping

public class PageController {

    private final String API_URL = "https://tasktrackerbackend.up.railway.app/api/tasks";
    private final String NORMAL_API_URL = "https://tasktrackerbackend.up.railway.app";
    private final String NOTIFICATION_API_URL = "https://tasktrackerbackend.up.railway.app/api/notifications";

    private final RestTemplate restTemplate;


    public PageController(RestTemplate restTemplate) {
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
            Notification notification = new Notification("New Account", "Welcome to Task Tracker");

            System.out.println("Here's my title: "+notification.getTitle());
            System.out.println("Here's my description: "+ notification.getDescription());
            System.out.println("Ended to store notification");

            String message = String.valueOf(restTemplate.postForObject(NORMAL_API_URL+"/register", users, String.class));
            System.out.println("Starting if statements");
            if (message.equals("Username Already Exists")) {
                model.addAttribute("showPasswordMessage", "Username already exists");
                return "signup";
            } else if (message.equals("Email Already Exists")) {
                model.addAttribute("showPasswordMessage", "Email already exists");
                return "signup";
            } else if (message.equals("User was added successfully")) {
                restTemplate.postForEntity(NOTIFICATION_API_URL+"/testcreatenotification", notification, Void.class);
                return "redirect:/signin";
            } else {
                System.out.println("This is else statement");
                model.addAttribute("showPasswordMessage", message);
                return "signup";
            }

        } catch (Exception e) {
            System.out.println("Exception error on front end ");
            model.addAttribute("showPasswordMessage", "Failed to create an account");
            return "signup";
        }
    }

    @GetMapping("/forgotPassword")
    public String getforgotpage() {
        return "forgotpassword";
    }

    @PostMapping("forgotPassword")
    public String forgotPassword(@ModelAttribute Users users, Model model) {
        try {
            model.addAttribute("forgotPasswordMessage", "");
//            System.out.println("Starting to store notification");
            Notification notification = new Notification("Forgot password", "Link to reset password was sent");

            System.out.println("Here's my title: "+notification.getTitle());
            System.out.println("Here's my description: "+ notification.getDescription());
            System.out.println("Ended to store notification");
            restTemplate.postForEntity(NOTIFICATION_API_URL+"/testcreatenotification", notification, Void.class);
            String message = String.valueOf(restTemplate.postForObject(NORMAL_API_URL+"/forgotpassword", users, String.class));
            System.out.println("Message:" + message);

            if (message.equals("Link to reset the password was sent.")) {
                model.addAttribute("forgotPasswordMessage", message);
                return "redirect:/signin";
            } else if (message.equals("User with this email was not found")) {
                model.addAttribute("forgotPasswordMessage", message);
                return "forgotpassword";
            } else if (message.equals("Link to reset the password hasn't expired,Check your emails")) {
                model.addAttribute("forgotPasswordMessage", message);
                return "forgotpassword";
            } else if (message.contains("Something went wrong while sending the reset password email")) {
                model.addAttribute("forgotPasswordMessage", message);
                return "forgotpassword";
            } else if (message.contains("Failed to send reset link to forgot password controller")) {
                model.addAttribute("forgotPasswordMessage", message);
                return "forgotpassword";
            }else if (message.contains("Failed to send email to password")) {
                model.addAttribute("forgotPasswordMessage", message);
                return "forgotpassword";
            }
            else {
                model.addAttribute("forgotPasswordMessage", "Email was not found");
                return "forgotpassword";
            }
        } catch (Exception e) {
            model.addAttribute("forgotPasswordMessage", "Exception Error");
            return "forgotpassword";
        }
    }

    @GetMapping("/resetPassword")
    public String getResetPage(@RequestParam("resetPasswordToken") String resetPasswordToken, Model model) {
        try {
            model.addAttribute("resetPasswordToken", resetPasswordToken);
            return "resetpassword";
        } catch (Exception e) {
            return "resetpassword";
        }
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam(value = "resetPasswordToken") String resetPasswordToken, @ModelAttribute Users users, Model model) {
        try {

            System.out.println("Reset token on the path " + resetPasswordToken);
            String message = String.valueOf(restTemplate.postForObject(NORMAL_API_URL+"/resetpassword?resetPasswordToken=" + resetPasswordToken, users, String.class));

            Notification notification = new Notification("User clicked Reset Password","Your password has been successfully reset. You can now log in using your new password.");

            if (message.equals("Password was resetted")) {
                restTemplate.postForEntity(NOTIFICATION_API_URL+"/createnotification", notification, Void.class);
                model.addAttribute("resetPasswordMessage", "");
                return "redirect:/signin";
            }else if(message.equals("New password must be different from your current password.")){
                model.addAttribute("resetPasswordToken",message);
                    return "resetpassword";

            }else if (message.equals("Use legitimate link to reset the password")) {
                model.getAttribute("resetPasswordToken");
                model.addAttribute("resetPasswordToken", resetPasswordToken);
                model.addAttribute("resetPasswordMessage", message);
                return "resetpassword";
            } else if (message.equals("Link to reset the password is expired")) {
                model.getAttribute("resetPasswordToken");
                model.addAttribute("resetPasswordToken", resetPasswordToken);
                model.addAttribute("resetPasswordMessage", message);
                return "resetpassword";
            } else if (message.equals("Token for the user was not found")) {
                model.getAttribute("resetPasswordToken");
                model.addAttribute("resetPasswordToken", resetPasswordToken);
                model.addAttribute("resetPasswordMessage", message);
                return "resetpassword";
            } else {
                model.getAttribute("resetPasswordToken");
                model.addAttribute("resetPasswordToken", resetPasswordToken);
                model.addAttribute("resetPasswordMessage", "Failed to reset the password");
                return "resetpassword";
            }

        } catch (Exception e) {
            model.getAttribute("resetPasswordToken");
            model.addAttribute("resetPasswordToken", resetPasswordToken);
            model.addAttribute("resetPasswordMessage", "Failed to reset the password");
            return "resetpassword";
        }
    }

    @GetMapping("/accountVerification")
    public String getEmailVerification(@RequestParam("token") String token, Model model) {
        try {
            System.out.println("My verification token: " + token);
            String message = restTemplate.getForObject(NORMAL_API_URL+"/verify?token=" + token.trim(), String.class);
            System.out.println("My message:" + message);
            if (message.equals("The account is verified")) {
                model.addAttribute("accountVerificationMessage", message);
                return "accountverification";
            } else if (message.equals("The token is expired, New link for verification was sent to your email.")) {
                model.addAttribute("accountVerificationMessage", message);
                return "accountverification";
            } else if (message.equals("User with that token was not found")) {
                model.addAttribute("accountVerificationMessage", message);
                return "accountverification";
            } else if (message.equals("Account already verified")) {
                model.addAttribute("accountVerificationMessage", message);
                return "accountverification";
            } else {
                model.addAttribute("accountVerificationMessage", "Failed to verify users account.");
                return "accountverification";
            }
        } catch (Exception e) {
            return "Failed to verify users account.";
        }
    }

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
//            System.out.println("Remember Me checkbox: "+user.isRememberMe());

            String url = NORMAL_API_URL+"/login";

            Map<String, String> request = new HashMap<>();

            request.put("username", user.getUsername().trim());
            request.put("password", user.getPassword().trim());
            request.put("rememberMe", String.valueOf(user.isRememberMe()));

//            request.put("email", String.valueOf(user.isRememberMe()));
//            Users user2 = new Users();
//            user2.setUsername(user.getUsername());
//            user2.setPassword(user.getPassword());
//            user2.setRememberMe(user.isRememberMe());
            System.out.println("String RememberMe value:"+String.valueOf(user.isRememberMe()));
            System.out.println("Boolean RememberMe value:"+user.isRememberMe());
            String message = restTemplate.postForObject(url, request, String.class);
            System.out.println("Message: " + message);
            if (message.equals("Verify your account, Click verify button in your email")) {

                model.addAttribute("signinError", message);
                return "signin";
            } else if (message.equals("Incorrect Credentials")) {
                System.out.println("Incorrect Credentials");
                model.addAttribute("signinError", message);
                return "signin";
            } else if (message.equals("You entered unaccepted values")) {
                System.out.println("Incorrect Credentials");
                model.addAttribute("signinError", "Incorrect Credentials");
                return "signin";
            } else if (message.contains("Correct credentials now generating token for login")) {
                System.out.println("Signed In Token: " + message);
                String value = message.substring(51);
                session.setAttribute("token", value);
                return "redirect:/todos";
            } else {
                System.out.println("Incorrect Credentials");
                model.addAttribute("signinError", message);
                return "signin";
            }


        } catch (HttpClientErrorException.Unauthorized e) {

            model.addAttribute("signinError", "Incorrect signin credentials");
            return "signin";
        }
    }


    @GetMapping("/")
    public String openHomePage() {
        return "redirect:/todos";
    }

    @GetMapping("/404")
    public String showErrorPage(){
        return "404";
    }

    @GetMapping("/500")
    public String showInternalErrorPage(){
        return "500";
    }

    @GetMapping("/todos")
    public String getTasks(
            HttpSession session,
            Model model
    ) {

        try {

            if (session.getAttribute("token") != null) {

                String token = session.getAttribute("token").toString();
                if (token.length() > 30) {
                    System.out.println("if statement");
                    System.out.println("My token: "+session.getAttribute("token"));
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + token);

                    HttpEntity<String> entity = new HttpEntity<>(headers);

                    RestTemplate restTemplate = new RestTemplate();

                    ResponseEntity<Task[]> response = restTemplate.exchange(
                            API_URL + "/todos",
                            HttpMethod.GET,
                            entity,
                            Task[].class
                    );
                    System.out.println("After Todos: "+response.getBody());
                    ResponseEntity<Users> profileInfo = restTemplate.exchange(
                            NORMAL_API_URL+"/profileinfo",
                            HttpMethod.GET,
                            entity,
                            Users.class
                    );
                    System.out.println("Username: "+profileInfo.getBody());


                    model.addAttribute("profileInfo", profileInfo.getBody());

                    model.addAttribute("tasks", (response.getBody()));
                    model.addAttribute("currentPage","hometab");
                    model.addAttribute("noOfNotifications", getNoOfNotification(token, session));

                    return "todos";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "redirect:/signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "redirect:/signin";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "todos";
        }
    }

    public List<Long> countNumberOfDays(List<Task> mytasks) {
        LocalDate today = LocalDate.now();

        List<Task> datetasks = mytasks;

        ArrayList<LocalDate> mydate2 = new ArrayList<>();

        for (Task task : datetasks) {

            if (!task.getDueDate().isEmpty() && task.getDueDate() != null) {
                mydate2.add(LocalDate.parse(task.getDueDate()));
            }

        }

        ArrayList<Long> daysLeft = new ArrayList<>();
        for (LocalDate days : mydate2) {
            Long dd = ChronoUnit.DAYS.between(today, days);
            daysLeft.add(dd);
        }

        return daysLeft;
    }


    @GetMapping("/todos/search")
    public String searchTodos(@RequestParam(value = "searchword", required = false) String searchword,
                              HttpSession session,
                              Model model) {

        try {

            if (session.getAttribute("token") != null) {
                String token = (String) session.getAttribute("token");

                if (token.length() > 30) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + token);

                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<Task> entity = new HttpEntity<>(headers);
                    ResponseEntity<Task[]> response = restTemplate.exchange(
                            API_URL + "/todos",
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
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "signin";
            }
        } catch (Exception e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/notification/search")
    public String searchNotification(@RequestParam(value = "searchword", required = false) String searchword,
                              HttpSession session,
                              Model model) {

        try {

            if (session.getAttribute("token") != null) {
                String token = (String) session.getAttribute("token");

                if (token.length() > 30) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + token);

                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<Notification> entity = new HttpEntity<>(headers);
                    ResponseEntity<Notification[]> response = restTemplate.exchange(
                            API_URL + "/notification",
                            HttpMethod.GET,
                            entity,
                            Notification[].class
                    );
                    List<Notification> notifications = Arrays.asList(response.getBody());
                    List<Notification> searchResults = new ArrayList<>();

                    if (searchword.isEmpty()) {
                        searchResults = notifications;
                    } else {
                        for (Notification notification : notifications) {
                            if (notification.getTitle().toLowerCase().contains(searchword.toLowerCase()) || notification.getDescription().toLowerCase().contains(searchword.toLowerCase()) ||
                                   String.valueOf(notification.getCreated()).contains(searchword)) {
                                searchResults.add(notification);
                            }
                        }
                    }
                    model.addAttribute("searchword", searchword);
                    model.addAttribute("notification", searchResults);
                    return "notificationSearchResult";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "signin";
            }
        } catch (Exception e) {
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

            if (session.getAttribute("token") != null) {

                String token = (String) session.getAttribute("token");
                if (token.length() > 30) {
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
                    model.addAttribute("noOfNotifications", getNoOfNotification(token, session));
                    return "viewSingleTask";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "signin";
            }

        } catch (Exception e) {
            return "viewSingleTask";
        }
    }

    @GetMapping("/noti/{id}")
    public String viewSingleNotification(
            @PathVariable Long id,
            Model model,
            HttpSession session
    ) {

        try {

            if (session.getAttribute("token") != null) {

                String token = (String) session.getAttribute("token");
                if (token.length() > 30) {
                    HttpHeaders headers = new HttpHeaders();

                    headers.set("Authorization", "Bearer " + token);

                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    ResponseEntity<Notification> response = restTemplate.exchange(
                            API_URL + "/noti/{id}",
                            HttpMethod.GET,
                            entity,
                            Notification.class,
                            id
                    );

                    Notification notification = response.getBody();
                    model.addAttribute("notification", notification);
                    model.addAttribute("noOfNotifications", getNoOfNotification(token, session));
                    return "notification";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "signin";
            }

        } catch (Exception e) {
            return "notification";
        }
    }


    @GetMapping("/todo")
    public String getPage(HttpSession session, Model model) {
        try {

            if (session.getAttribute("token") != null) {
                String token = (String) session.getAttribute("token");

                if (token.length() > 30) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + token);
                    model.addAttribute("todaysDate",LocalDate.now());
                    model.addAttribute("noOfNotifications", getNoOfNotification(token, session));
                    return "todo";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "signin";
            }
        } catch (Exception e) {
            return "todo";
        }
    }

    @PostMapping("/todo/save/task")
    public String saveTask(@ModelAttribute Task task,
                           HttpSession session,
                           Model model) {

        try {

            if (session.getAttribute("token") != null) {
                String token = session.getAttribute("token").toString();

                if (token.length() > 30) {
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
System.out.println("Task priority: "+ task.getPriority());
                    if(task.getPriority() != null && task.getPriority().equals("HIGH")) {

                        Notification notification = new Notification("New Priority Task Created", "You have successfully created a new priority task. Remember to complete it before its due date.");
                        System.out.println("Task title: "+ notification.getTitle());
                        System.out.println("Task description: "+ notification.getDescription());
                                HttpHeaders headers1 = new HttpHeaders();
                        headers.set("Authorization", "Bearer " + token);

                        headers.setContentType(MediaType.APPLICATION_JSON);
                        HttpEntity<Notification> entity1 = new HttpEntity<>(notification, headers1);
                        restTemplate.exchange(
                                NOTIFICATION_API_URL + "/createnotification",
                                HttpMethod.POST,
                                entity1,
                                Notification.class
                        );
                        //restTemplate.postForEntity(NOTIFICATION_API_URL + "/createnotification", notification, Notification.class);
                    }

                    return "redirect:/todos";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "signin";
            }

        } catch (Exception e) {
            return "redirect:/todo";
        }
    }

    @PutMapping("/todo/{id}")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute Task task,
                             HttpSession session,
                             Model model) {

        try {

            if (session.getAttribute("token") != null) {
                String token = (String) session.getAttribute("token");

                if (token.length() > 30) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + token);

                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<Task> entity = new HttpEntity<>(task, headers);
                    restTemplate.exchange(API_URL + "/todo/{id}",
                            HttpMethod.PUT,
                            entity,
                            Task.class,
                            id);

                    model.addAttribute("noOfNotifications", getNoOfNotification(token, session));

                    return "redirect:/todos";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "signin";
            }
        } catch (Exception e) {
            System.out.println("The application crashed");
            System.out.println(e.getMessage());
            return "redirect:/todos";
        }
    }

    @GetMapping("/completedTasks")
    public String getCompletedTasks(
            HttpSession session,
            Model model) {

        try {

            if (session.getAttribute("token") != null) {
                String token = session.getAttribute("token").toString();

                if (token.length() > 30) {
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
                    model.addAttribute("currentPage","completedtaskstab");
                    model.addAttribute("noOfNotifications", getNoOfNotification(token, session));
                    return "CompletedTasks";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "signin";
            }
        } catch (Exception e) {
            return "CompletedTasks";
        }
    }


    @PatchMapping("/todo/{id}")
    public String updateCompletion(@PathVariable Long id,
                                   @ModelAttribute Task task,
                                   HttpSession session,
                                   Model model) {

        try {

            if (session.getAttribute("token") != null) {
                String token = (String) session.getAttribute("token");

                if (token.length() > 30) {
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
                    model.addAttribute("noOfNotifications", getNoOfNotification(token, session));
                    return "redirect:/todos";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "signin";
            }
        } catch (Exception e) {
            return "redirect:/todos";
        }
    }


    @DeleteMapping("/todo/{id}")
    public String deleteTask(@PathVariable Long id,
                             HttpSession session,
                             Model model) {

        try {

            if (session.getAttribute("token") != null) {
                String token = (String) session.getAttribute("token");

                if (token.length() > 30) {
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
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "signin";
            }

        } catch (Exception e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/changePassword")
    public String getChangePasswordPage(HttpSession session,
                                        Model model) {
        try {
            if (session.getAttribute("token") != null) {
                String token = session.getAttribute("token").toString();
                if (token.length() > 30) {
                    model.addAttribute("noOfNotifications", getNoOfNotification(token, session));
                    return "changepassword";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "signin";
            }
        } catch (Exception e) {
            model.addAttribute("changepasswordError", "Unauthorized");
            return "changepassword";
        }
    }

    @PatchMapping("/changePassword")
    public String changePassword(@ModelAttribute Users users, HttpSession session, Model model) {
        try {

            if (session.getAttribute("token") != null) {
                String token = session.getAttribute("token").toString();

                if (token.length() > 30) {

                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + token);

                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<Users> entity = new HttpEntity<>(users, headers);

                    //RestTemplate restTemplate = new RestTemplate();

                    ResponseEntity<String> response = restTemplate.exchange(
                            NORMAL_API_URL+"/changepassword",
                            HttpMethod.PATCH,
                            entity,
                            String.class
                    );


                    if (response.getBody() != null && response.getBody().equals("Password Changed")) {

                            Notification notification = new Notification("Password Changed Successfully", "Your password has been successfully changed. If you did not make this change, please secure your account immediately.");
                            HttpEntity<Notification> entity1 = new HttpEntity<>(notification, headers);
                            restTemplate.exchange(
                                    NOTIFICATION_API_URL + "/createnotification",
                                    HttpMethod.POST,
                                    entity1,
                                    Notification.class
                            );

                        model.addAttribute("changePasswordError", "Password was successfully changed");
                        return "changepassword";
                    }else if(response.getBody() != null && response.getBody().equals("New password must be different from your current password.")){
                        model.addAttribute("changePasswordError",response.getBody());
                        return "changePassword";
                    } else if (response.getBody() != null && response.getBody().equals("You entered wrong current password")) {
                        model.addAttribute("changePasswordError", response.getBody());
                        return "changepassword";
                    } else {
                        model.addAttribute("changePasswordError", response.getBody());
                        return "changepassword";
                    }
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "redirect:/signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "redirect:/signin";
            }
        } catch (Exception e) {
            model.addAttribute("changePasswordError", e.getMessage());
            return "changepassword";
        }
    }

    @GetMapping("/profile")
    public String openProfile(HttpSession session, Model model) {
        try {
            if (session.getAttribute("token") != null) {
                String token = (String) session.getAttribute("token");

                if (token.length() > 30) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + token);

                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<Users> entity = new HttpEntity<>(headers);
                    ResponseEntity<Users> response = restTemplate.exchange(
                            NORMAL_API_URL+"/profileinfo",
                            HttpMethod.GET,
                            entity,
                            Users.class
                    );


                    model.addAttribute("profileInfo", response.getBody());
                    model.addAttribute("currentPage","profiletab");
                    model.addAttribute("noOfNotifications", getNoOfNotification(token, session));
                    return "profile";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "redirect:/signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "redirect:/signin";
            }
        }
        catch( Exception e){
        return "redirect:/todos";
    }
}

    @GetMapping("/deleteAccount")
    public String openDeleteAccount(HttpSession session, Model model) {
        try {
            if (session.getAttribute("token") != null) {
                String token = (String) session.getAttribute("token");

                if (token.length() > 30) {
                    model.addAttribute("noOfNotifications", getNoOfNotification(token, session));
                    return "deleteAccount";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "redirect:/signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "redirect:/signin";
            }
        }
        catch( Exception e){
            model.addAttribute("signinError", "Unauthorized");
            return "redirect:/todos";
        }
    }

    @DeleteMapping("/deleteAccount")
    public String deleteAccount(HttpSession session, Model model) {
        try {
            model.addAttribute("accountDeletionErrorMessage", "");

            if (session.getAttribute("token") != null) {
                String token = (String) session.getAttribute("token");

                if (token.length() > 30) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + token);

                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Users> entity = new HttpEntity<>(headers);
//                    ResponseEntity<Users> userInfo = restTemplate.exchange(
//                            NORMAL_API_URL+"/profileinfo",
//                            HttpMethod.GET,
//                            entity,
//                            Users.class
//                    );

//                    Users currentUser = userInfo.getBody();
//                    HttpEntity<Users> entity2 = new HttpEntity<>(currentUser,headers);
                    ResponseEntity<String> response2 = restTemplate.exchange(
                            NORMAL_API_URL+"/accountDeletion",
                            HttpMethod.DELETE,
                            entity,
                            String.class
                    );
                    if(response2.getBody().equals("Account deleted successfully")){
                        return "redirect:/signin";
                    }
                    else if(response2.getBody().equals("Something strange happened, User was not found")){
                        model.addAttribute("accountDeletionErrorMessage", response2.getBody());
                        return "deleteAccount";
                    }
                    else {
                        model.addAttribute("accountDeletionErrorMessage", response2.getBody());
                        return "deleteAccount";
                    }
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "redirect:/signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "redirect:/signin";
            }
        }
        catch( Exception e){
            return "redirect:/deleteAccount";
        }
    }

    @PostMapping("/signout")
    public String signout(HttpSession session, Model model){
        try{
            if(session.getAttribute("token") != null){
                String token = session.getAttribute("token").toString();
                if(token.length() > 30){
                    session.invalidate();
                    return "redirect:/signin";
                }else{
                    model.addAttribute("signinError", "Unauthorized");
                    return "redirect:/signin";
                }
            }else{
                model.addAttribute("signinError", "Unauthorized");
                return "redirect:/signin";
            }
        } catch (Exception e) {
            return "redirect:/todos";
        }
    }

    @GetMapping("/calendar")
    public String showCalendar(HttpSession session,Model model){
        try {
            if (session.getAttribute("token") != null) {
                String token = (String) session.getAttribute("token");

                if (token.length() > 30) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization","Bearer "+token);

                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<String> entity = new HttpEntity<>(headers);

                    ResponseEntity<Task[]> response = restTemplate.exchange(
                            API_URL + "/todos",
                            HttpMethod.GET,
                            entity,
                            Task[].class
                    );

                    List<Task> usersTask = Arrays.asList(response.getBody());
                    model.addAttribute("tasks", usersTask);
                    model.addAttribute("currentPage","calendartab");
                    model.addAttribute("noOfNotifications", getNoOfNotification(token, session));

                    return "calendar";
                } else {
                    model.addAttribute("signinError", "Unauthorized");
                    return "redirect:/signin";
                }
            } else {
                model.addAttribute("signinError", "Unauthorized");
                return "redirect:/signin";
            }
        }
        catch( Exception e){
            model.addAttribute("signinError", "Unauthorized");
            return "redirect:/todos";
        }
    }

//    @GetMapping("/passedDueDate")
//    public String getPassedDueDate(
//            HttpSession session,
//            Model model) {
//
//        try {
//
//            if (session.getAttribute("token") != null) {
//                String token = session.getAttribute("token").toString();
//
//                if (token.length() > 30) {
//                    HttpHeaders headers = new HttpHeaders();
//
//                    headers.set("Authorization", "Bearer " + token);
//
//                    HttpEntity<String> entity = new HttpEntity<>(headers);
//
//                    ResponseEntity<Task[]> response = restTemplate.exchange(
//                            API_URL + "/passedDueDate",
//                            HttpMethod.GET,
//                            entity,
//                            Task[].class
//                    );
//
//                    List<Task> tasks2222 = Arrays.asList(response.getBody());
////                    for(Task completedTaskss1 : tasks){
////                        System.out.println(completedTaskss1.getTitle() + "\n");
////                    }
//                    model.addAttribute("tasks", tasks2222);
//                    return "passedDueDate";
//                } else {
//                    model.addAttribute("signinError", "Unauthorized");
//                    return "redirect:/signin";
//                }
//            } else {
//                model.addAttribute("signinError", "Unauthorized");
//                return "redirect:/signin";
//            }
//        } catch (Exception e) {
//            return "redirect:/todos";
//        }
//    }
@GetMapping("/passedDueDate")
public String getPassedDueDate(
        HttpSession session,
        Model model) {

    try {

        String token = (String) session.getAttribute("token");

        if (token == null || token.isEmpty()) {
            return "redirect:/signin";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Task[]> response = restTemplate.exchange(
                API_URL + "/passedDueDate",
                HttpMethod.GET,
                entity,
                Task[].class
        );

        Task[] taskArray = response.getBody();

        List<Task> tasks = taskArray != null
                ? Arrays.asList(taskArray)
                : new ArrayList<>();

        model.addAttribute("tasks", tasks);
        model.addAttribute("noOfNotifications", getNoOfNotification(token, session));
        return "passedDueDate";

    } catch (Exception e) {

        System.out.println(e.getMessage());

        return "redirect:/todos";
    }
}

    @GetMapping("/notifications")
    public String getNotifications(
            HttpSession session,
            Model model) {

        try {

            String token = (String) session.getAttribute("token");
System.out.println("Notification token: "+token);
            if (token == null || token.isEmpty()) {
                return "redirect:/signin";
            }
System.out.println("Setting headers");
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            System.out.println("Fetching data");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            System.out.println("Entity Header: "+ entity.getHeaders());
            System.out.println("Entity Body: "+entity.getBody());
            ResponseEntity<Notification[]> response = restTemplate.exchange(
                    NOTIFICATION_API_URL + "/notification",
                    HttpMethod.GET,
                    entity,
                    Notification[].class
            );
System.out.println("Response Message: "+ response.getBody());
            assert response.getBody() != null;
            List<Notification> notification= Arrays.asList(response.getBody());
            model.addAttribute("notification",notification);
System.out.println("Notification Size if it empty: "+ notification);
List<Notification> emptyArray = new ArrayList<>();
model.addAttribute("emptyArray", emptyArray);
            System.out.println("EmptyArray: "+ notification);

            model.addAttribute("noOfNotifications", getNoOfNotification(token, session));
            model.addAttribute("currentPage","notificationstab");
            return "notifications";

        } catch (Exception e) {

            System.out.println(e.getMessage());
            model.addAttribute("noOfNotifications", 0);
            return "notifications";
        }
    }

    public int getNoOfNotification(String usertoken, HttpSession session) {
        String token = (String) session.getAttribute(usertoken.trim());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + usertoken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        System.out.println("AAbout to get No of notifications");
        System.out.println("Usertoken:"+usertoken);
        ResponseEntity<Integer> response = restTemplate.exchange(
                NOTIFICATION_API_URL + "/noOfNotification",
                HttpMethod.GET,
                entity,
                Integer.class
        );
        int value = response.getBody() == null ?0 :response.getBody();
        System.out.println(" No of notifications" + value);
        return value;
    }

//    public int getNoOfNotification(String usertoken, HttpSession session) {
////        String token = (String) session.getAttribute(usertoken.trim());
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + usertoken);
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<Integer> response = restTemplate.exchange(
//                API_URL + "/noOfNotifications",
//                HttpMethod.GET,
//                entity,
//                Integer.class
//        );
//        int value = response.getBody() == null ?0 :response.getBody();
//        return value;
//    }

//public String sendNotification(String usertoken, HttpSession session) {
//    String token = (String) session.getAttribute(usertoken.trim());
//    HttpHeaders headers = new HttpHeaders();
//    headers.set("Authorization", "Bearer " + usertoken);
//
//    HttpEntity<String> entity = new HttpEntity<>(headers);
//
//    System.out.println("About to send new notifications");
//    System.out.println("Usertoken:"+usertoken);
//    ResponseEntity<String> response = restTemplate.exchange(
//            NOTIFICATION_API_URL + "/save",
//            HttpMethod.POST,
//            entity,
//            String.class
//    );
//    String value = response.getBody() == null ?"0" :response.getBody();
//    System.out.println(" No of notifications" + value);
//    return value;
//}


}

