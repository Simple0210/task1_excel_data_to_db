package com.example.studentsapp.service;

import com.example.studentsapp.entity.Student;
import com.example.studentsapp.payload.ApiResult;
import com.example.studentsapp.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    /*
    STUDENT CONTROLLERDAN BERIB YUBORILGAN MULTIPART FILENI O`QIB ICHIDAGI STUDENTLARNI DATABASEGA SAQLOVCHI METOD
     */
    public ApiResult uploadExcelFileAndCreateStudentsDB(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return new ApiResult("This file is empty", false);
        }
        //MULTIPART FILE NING INPUTSTREAMNI REAL EXCEL FILEGA O`GIRYAPTI
        XSSFWorkbook studentsExcel = new XSSFWorkbook(multipartFile.getInputStream());
        //STUDENTS EXCEL FILEDAN STUDENTLIST DEGAN SHEETNI OLYAPTI
        XSSFSheet studentListSheet = studentsExcel.getSheetAt(0);
        /*
        SHEETNING XAR BIR QATORINI FOR QILIB AYLANIB XAR BITTA CELLDAN STUDENTNING FIELDLARINI AJRATIB OLIB,
        O`SHA FIELDLAR ASOSIDA YANGI STUDENT OBJECTINI YASAB DATABASEDAGI STUDENT TABLEGA SAQLAYAPTI
         */
        String firstname;
        String lastname;
        String middleName;
        String phoneNumber;
        String address;
        String age;
        boolean hasError = false;
        /*
        LIST BIRORTA HAM STUDENTNING MA`LUMOTLARI XATOLIKKA TUSHMASAGINA ULARNI SAQLASH UCHUN YIG`IB BORISH UCHUN ISHLATILADI,
        CHUNKI STUDENTLARNING YARMIGA YETGANDA XATOLIK BO`LSA QOLGAN YARMINI SAQLAB QO`YMASLIK UCHUN
         */
        List<Student> students = new ArrayList<>();

        for (Row row : studentListSheet) {
            if (!studentListSheet.getRow(0).equals(row)) {
                firstname = row.getCell(1).getStringCellValue();
                if (firstname.length() > 255) {
                    return new ApiResult("Firstname matnining uzunligi 255 belgidan oshmasligi kerak", false);
                }
                lastname = row.getCell(2).getStringCellValue();
                if (lastname.length() > 255) {
                    return new ApiResult("Lastname matnining uzunligi 255 belgidan oshmasligi kerak", false);
                }
                middleName = row.getCell(3).getStringCellValue();
                if (middleName.length() > 255) {
                    return new ApiResult("Middlename matnining uzunligi 255 belgidan oshmasligi kerak", false);
                }
                phoneNumber = row.getCell(4).getStringCellValue();
                if (phoneNumber.length() < 7 || phoneNumber.length() > 13) {
                    return new ApiResult("Telefon raqam xato kiritildi", false);
                }
                address = row.getCell(5).getStringCellValue();
                if (address.length() > 255) {
                    return new ApiResult("Address matnining uzunligi 255 belgidan oshmasligi kerak", false);
                }
            /*
            STUDENTNING AGE EXCEL FILEDA NUMBER KO`RINISHIDA YOZILGAN, STUDENT CLASSDA U STRINGDA DATAFORMATTER
            O`SHA CELLDAGI NUMBERNI STRINGGA FORMATLAB BERYAPTI
             */
                DataFormatter formatter = new DataFormatter();
                age = formatter.formatCellValue(row.getCell(6));

                for (int i = 0; i < age.length(); i++) {
                    if (Character.isLetter(age.charAt(i))) {
                        return new ApiResult("Age faqat sonlardan iborat bo`lishi kerak", false);
                    }
                }
                Student student = new Student(firstname, lastname, middleName, phoneNumber, address, Integer.valueOf(age));
                students.add(student);
            }
        }
        studentRepository.saveAll(students);
        return new ApiResult("Ma`lumotlar muvaffaqiyatli saqlandi", true);
    }
}
