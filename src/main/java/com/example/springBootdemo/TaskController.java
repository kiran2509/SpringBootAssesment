package com.example.springBootdemo;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo")
public class TaskController {


    List<Task> taskList = new ArrayList<>();
    private static int nextTaskId;

    @GetMapping("")
    public List<Task> getTaskList(){
        return taskList;
    }

    @PostMapping("")
    public Task putTask(@RequestBody Task task){
        task.setTaskId(++nextTaskId);
        taskList.add(task);
        return task;
    }

    @GetMapping("/param/{id}")
    public String getTaskById(@PathVariable int id){
        Task task = taskList.stream().filter(t -> t.getTaskId() == id).findFirst().orElse(null);
        if(task != null)
            return task.toString();
        return "Task with given "+ id +" is not present";
    }

    // @PathVariable is extracting value from the URI Path, it's not encoded. Whereas @RequestParam is encode.

    @GetMapping("/param")
    public String getTaskByIdUsingRequestParam(@RequestParam int id){

        Task task = taskList.stream().filter(t -> t.getTaskId() == id).findFirst().orElse(null);
        if(task != null)
            return task.toString();
        return "Task with given "+ id +" is not present";
    }

    @GetMapping("/filter")
    public List<Task> getTasksInOrder(@RequestParam String sortBy){

        System.out.println(sortBy);
        if(sortBy.equalsIgnoreCase("ASC"))
            return taskList.stream().sorted((t1,t2) -> t1.getTaskId() - t2.getTaskId()).collect(Collectors.toList());
        else if(sortBy.equalsIgnoreCase("DESC"))
            return taskList.stream().sorted((t1,t2) -> t2.getTaskId() - t1.getTaskId()).collect(Collectors.toList());

        return null;
    }

    @PutMapping("/param/{id}")
    public String updateStatusById(@PathVariable int id, @RequestBody Task currentTask){

        Task task = taskList.stream().filter(t -> t.getTaskId() == id).findFirst().orElse(null);
        if(task != null){
            String str = task.getTaskStatus();
            task.setTaskStatus(currentTask.getTaskStatus());
            return "Task Status is changed from  "+ str +" to  "+ task.getTaskStatus();
        }
        return "Task with id is not present";
    }

    @DeleteMapping("{id}")
    public String deleteTaskById(@PathVariable int id){

        Task task = taskList.stream().filter(i -> i.getTaskId() == id).findFirst().orElse(null);
        if(task != null) {
            taskList.remove(task);
            return "Task got deleted";
        }
        return "Task with id is not present";
    }
}
