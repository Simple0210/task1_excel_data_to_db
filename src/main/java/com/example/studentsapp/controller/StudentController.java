package com.example.studentsapp.controller;

import com.example.studentsapp.payload.ApiResult;
import com.example.studentsapp.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/uploadStudentExcel")
    public ApiResult uploadExcelFileAndWriteDB(@RequestParam("files") MultipartFile file) throws IOException {

        ApiResult apiResult = studentService.uploadExcelFileAndCreateStudentsDB(file);
        if (!apiResult.isSuccess()){

          return new ApiResult(apiResult.getMessages(), false);
        }
        return new ApiResult(apiResult.getMessages(), true);
    }
}