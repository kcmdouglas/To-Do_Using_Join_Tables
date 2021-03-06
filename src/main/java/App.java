
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.ArrayList;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/categories.vtl");

      model.put("categories", Category.all());

      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/category/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params("id")));

      model.put("template", "templates/category.vtl");

      model.put("category", category);

      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/tasks", (request,response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("tasks", Task.all(false));
      model.put("template", "templates/tasks.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/complete", (request,response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      model.put("tasks", Task.all(true));
      model.put("template", "templates/complete.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/task/:id", (request,response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      // int id = Integer.parseInt(request.params("id"));
      // Task task = Task.find(id);
      model.put("task", Task.find(Integer.parseInt(request.params("id"))));
      model.put("template", "templates/task.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/tasks", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String description = request.queryParams("description");
      Task newTask = new Task(description);
      response.redirect("/tasks");
      return null;
    });

    post("/category/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("category");
      Category newCategory = new Category(name);
      newCategory.save();
      response.redirect("/categories");
      return null;
    });

    post("/task/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("task");
      Task newTask = new Task(name);
      newTask.save();
      response.redirect("/categories");
      return null;
    });

    post("/task/new", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("category");
      Category newCategory = new Category(name);
      response.redirect("/categories");
      return null;
    });


    put("/tasks/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Task task = Task.find(Integer.parseInt(request.params("id")));
      String description = request.queryParams("description");
      task.update("description");
      model.put("template", "templates/task.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    // delete("/tasks/:id", (request, response) -> {
    //   HashMap<String, Object> model = new HashMap<String, Object>();
    //   Task task = Task.find(Integer.parseInt(request.params("id")));
    //   task.delete();
    //   model.put("template", "templates/task.vtl");
    //   return new ModelAndView(model, layout);
    // }, new VelocityTemplateEngine());

    post("/task/complete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();

      for (Task task: Task.all(false)){
        if (request.queryParams(Integer.toString(task.getId())) != null) {
          task.complete();
        }
      }
      // Task completedTask = Task.find(Integer.parseInt(request.queryParams("task")));
      // completedTask.complete();
      response.redirect("/tasks");
      return null;
    });

    post("/task/complete/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      for (Task task: Task.all(true)){
        if (request.queryParams(Integer.toString(task.getId())) != null) {
          task.delete();
        }
      }
      // deletedTask.delete();
      response.redirect("/complete");
      return null;
    });
  }
}
