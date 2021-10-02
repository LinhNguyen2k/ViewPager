package com.example.nguyenanhlinh_viewpager.model

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WriteReadFile(var context: Context) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun writeFile(dateList: ArrayList<Notes>) {
        val filePaths: String = context.filesDir.parentFile.toString() + "/diaryAPP.csv"
        try {
            val file = File(filePaths)
            val fw = FileWriter(file)
            fw.append("\n")
            dateList.forEach {
                if (it.notes.contains("\n") || it.notes.contains(',')) {
                    if (it.notes.contains('"')) {
                        fw.append("\"")
                        var stringP = ""
                        for (i in it.notes.indices) {
                            if (it.notes[i] == '"')
                                stringP += "\"\""
                            else
                                stringP += it.notes[i]
                        }
                        fw.append(stringP)
                        fw.append("\"")
                    } else {
                        fw.append("\"")
                        fw.append(it.notes)
                        fw.append("\"")
                    }
                } else {
                    if (it.notes.contains('"')) {
                        fw.append("\"")
                        var stringP = ""
                        for (i in it.notes.indices) {
                            if (it.notes[i] == '"')
                                stringP += "\"\""
                            else
                                stringP += it.notes[i]
                        }
                        fw.append(stringP)
                        fw.append("\"")
                    } else {
                        fw.append(it.notes)
                    }
                }
                fw.append(",")
                fw.append((it.localdate))
                fw.append("\n")
            }
            fw.flush()
            fw.close()
//             dùng csvPrinter xem dữ liệu lưu như thế nào
//            val writer = Files.newBufferedWriter(Paths.get(file.toURI()))
//            val csvPrinter =
//                CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Date", "Title", "Content"))
//            for (item in list) {
//                csvPrinter.printRecord(sdf.format(item.date), item.title, item.string)
//            }
//            csvPrinter.flush()
//            csvPrinter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun readFile(): ArrayList<Notes> {
        var dateList = ArrayList<Notes>()
        val filePaths: String = context.filesDir.parentFile.toString() + "/diaryAPP.csv"
        val file = File(filePaths)
        if (file.exists())
            try {
                var fr = FileReader(file)
                var bf = BufferedReader(fr)
                bf.readLine()
                var dem =
                    0        // biến này để quy định lấy cột nào 0 -> date , 1->title , 2-> content
                var localdate = ""
                var content = ""
                var check =
                    2     // check xem dòng đó đã đủ hay chưa ( dấu ngoặc kép luôn luôn là số chẵn )
                while (true) {              // let's do it
                    var line: String? = bf.readLine()
                    if (line == "")           // trường hợp xuoosgn dòng nhiều lần
                        content += "\n"
                    if (line!!.contains('"') || check % 2 != 0)  // đặc biệt hoặc dấu ngoặc kép chưa đủ ( nội dung nhảy xuống dòng )
                    {
                        for (i in line.indices) {
                            when (dem) {

                                //"123   """, 22/10/2021
                                0 -> {
                                    if (line[i] == '"') {   // tăng biến check khi thấy kí tự "
                                        check++
                                    }
                                    if (line[i] != ',' || (line[i] == ',' && check % 2 != 0)) {     // nếu k gặp dấu phẩy hoặc gặp nhưng kí tự " không chẵn thì thêm vào title
                                        content += line[i]
                                    }
                                    if (i == line.length - 1 && check % 2 != 0) {   // hết dòng nhưng kí tự " không chẵn -> làm tiếp
                                        content += "\n"         //vì k set lại dem nên nó vẫn tiếp tục tìm content
                                        continue
                                    }
                                    if (check % 2 == 0 && line[i] == ',') {             // kí tự " chẵn là tới dấu , chuyển sang giai đoạn tìm content
                                        dem = 1
                                        check = 2           // reset check
                                        continue
                                    }
                                }
                                1 -> {
                                    localdate += line[i]
                                    if (check % 2 == 0 && i == line.length - 1) {    // kí tự " chẵn và hết dòng -> tìm xong
                                        check = 2
                                        dem = 0             // quay lại tìm date
                                        break
                                    }

                                }
                            }
                        }
                        if (check % 2 == 0) {   // check xem content đã đủ chưa
                            if (content.contains("\"")) {
                                content = content.substring(1, content.length - 1)
                                content = content.replace("\"\"", "\"")
                            }
                            var notes = Notes(content, localdate, true)
                            localdate = ""
                            content = ""
                            dateList.add(notes)
                        }
                    } else    // trường hợp không có kí tự xuống dòng hoặc dấu , " hoặc là chuỗi content của dòng trên
                    {
                        if (check % 2 == 0) {
                            var listString = line.split(',')

                            dateList.add(Notes(listString[0], listString[1], true))
                        }
                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        return dateList
    }
}