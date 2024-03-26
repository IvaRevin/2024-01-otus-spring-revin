package ru.otus.hw.shell.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {

    private final TestService testService;

    private final ResultService resultService;

    private final StudentService studentService;

    private TestResult testResult;

    private Student student;

    @ShellMethod(value = "Start all process for testing command", key = {"start", "s"})
    public void startTest() {
        this.loginTestingStudent();
        this.testStudent();
        this.results();
    }

    @ShellMethod(value = "Login as student command", key = {"login", "l", "log"})
    public void loginTestingStudent() {
        student = studentService.determineCurrentStudent();
        testResult = null;
    }

    @ShellMethod(value = "Start test command", key = {"test", "t"})
    @ShellMethodAvailability(value = "isStudentExist")
    public void testStudent() {
        testResult = testService.executeTestFor(student);
    }

    @ShellMethod(value = "Show results testing command", key = {"result", "res"})
    @ShellMethodAvailability(value = "isTestResultExist")
    public void results() {
        resultService.showResult(testResult);
    }

    private Availability isTestResultExist() {
        return testResult == null ?
            Availability.unavailable("Take the test to get your result") : Availability.available();
    }

    private Availability isStudentExist() {
        return student == null ?
            Availability.unavailable("Login as a student") : Availability.available();
    }
}
