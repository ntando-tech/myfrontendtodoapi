package todo.backend.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import todo.backend.api.model.Task;
import todo.backend.api.config.RestClientConfig;
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
    @GetMapping("/todos")
    public String getAllTasks(Model model) {

       // RestTemplate restTemplate = new RestTemplate();

        Task[] tasksArray = restTemplate.getForObject(API_URL, Task[].class);
        List<Task> tasks = Arrays.asList(tasksArray);

        model.addAttribute("tasks", tasks);

        return "todos";
    }

    @GetMapping("/todo/{id}")
    public String viewSingleTask(@PathVariable Long id, Model model) {

       // RestTemplate restTemplate = new RestTemplate();

        //Task[] taskArray = restTemplate.getForObject( API_URL+"/" + id, Task[].class);
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
