package todo.backend.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import todo.backend.api.model.Task;
import todo.backend.api.config.RestClientConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
//@RequestMapping("/todo")
@RequestMapping

public class PageController {

    private final String API_URL = "http://localhost:8080/api/tasks";

    private final RestTemplate restTemplate;

    public PageController(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
   }

    @GetMapping("/signin")
    public String getSignin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String GetCreateUser() {
        return "/signup";
    }

   @GetMapping("/todos")
    public String getAllTasks(Model model) {

        Task[] tasksArray = restTemplate.getForObject(API_URL, Task[].class);
        List<Task> tasks = Arrays.asList(tasksArray);

        model.addAttribute("tasks", tasks);

        return "todos";
    }

    @GetMapping("/todos/search")
    public String searchTodos(@RequestParam(value="searchword", required = false) String searchword, Model model) {
        Task[] tasksArray = restTemplate.getForObject(API_URL, Task[].class);
        List<Task> tasks = Arrays.asList(tasksArray);
        List<Task> searchResults = new ArrayList<>();
        if(searchword.isEmpty()){
            searchResults = tasks;
        }
        else {
            for(Task task: tasksArray) {
                if(task.getTitle().toLowerCase().contains(searchword.toLowerCase()) || task.getDescription().toLowerCase().contains(searchword.toLowerCase()) ||
                task.getPriority().toLowerCase().contains(searchword.toLowerCase()) || task.getDueDate().contains(searchword) || String.valueOf(task.getCreated()).contains(searchword)){
                    searchResults.add(task);
                }
            }
        }
        model.addAttribute("searchword", searchword);
        model.addAttribute("tasks", searchResults);
        return "searchResult";

    }


    @GetMapping("/todo/{id}")
    public String viewSingleTask(@PathVariable Long id, Model model) {

        Task task = restTemplate.getForObject( API_URL+"/todo/{id}", Task.class,id);
        model.addAttribute("tasks", task);

        return "viewSingleTask";

    }

    @GetMapping("/todo")
    public String getPage(){
        return "todo";
    }

    @PostMapping("/todo/save/task")
    public String saveTask(@ModelAttribute Task task) {

       // RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForObject(API_URL, task, Task.class);

        return "redirect:/todos";
    }

    @PutMapping("/todo/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task task){
        //RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(API_URL+"/todo/{id}",task,id);
        return "redirect:/todos";
    }

      @GetMapping("/completedTasks")
      public String getCompletedTasks(Model model){
            //RestTemplate restTemplate = new RestTemplate();
         Task[] completedTasks = restTemplate.getForObject(API_URL, Task[].class);
         List<Task> completedTaskss = Arrays.asList(completedTasks);
         model.addAttribute("tasks", completedTaskss);
         return "CompletedTasks";
      }
    @PatchMapping("/todo/{id}")
    public String updateCompletion(@PathVariable Long id,@ModelAttribute Task task, Model model){
        //RestTemplate restTemplate = new RestTemplate();
        restTemplate.patchForObject(API_URL+"/todo/"+id,null,Void.class);
         return "redirect:/todos";
    }


    @DeleteMapping("/todo/{id}")
    public String deleteTask(@PathVariable Long id) {

       //RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(API_URL + "/todo/"+id);


        return "redirect:/todos";
    }

}
