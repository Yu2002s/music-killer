package xyz.jdynb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import xyz.jdynb.result.Result;

@RestController
@RequestMapping("/test")
public class TestController {
  
  @Data
  @AllArgsConstructor
  public static class ExampleData {

    private String name;

    private Integer age;
    
  }

  @GetMapping("/example")
  public Result<ExampleData> getExample() {
    if (Math.random() * 100 < 50) {
      throw new IllegalStateException("发生错误了");
    }
    return Result.success(new ExampleData("Test", 12));
  }
}
