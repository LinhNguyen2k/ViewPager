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
//             d??ng csvPrinter xem d??? li???u l??u nh?? th??? n??o
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
                    0        // bi???n n??y ????? quy ?????nh l???y c???t n??o 0 -> date , 1->title , 2-> content
                var localdate = ""
                var content = ""
                var check =
                    2     // check xem d??ng ???? ???? ????? hay ch??a ( d???u ngo???c k??p lu??n lu??n l?? s??? ch???n )
                while (true) {              // let's do it
                    var line: String? = bf.readLine()
                    if (line == "")           // tr?????ng h???p xuoosgn d??ng nhi???u l???n
                        content += "\n"
                    if (line!!.contains('"') || check % 2 != 0)  // ?????c bi???t ho???c d???u ngo???c k??p ch??a ????? ( n???i dung nh???y xu???ng d??ng )
                    {
                        for (i in line.indices) {
                            when (dem) {

                                //"123   """, 22/10/2021
                                0 -> {
                                    if (line[i] == '"') {   // t??ng bi???n check khi th???y k?? t??? "
                                        check++
                                    }
                                    if (line[i] != ',' || (line[i] == ',' && check % 2 != 0)) {     // n???u k g???p d???u ph???y ho???c g???p nh??ng k?? t??? " kh??ng ch???n th?? th??m v??o title
                                        content += line[i]
                                    }
                                    if (i == line.length - 1 && check % 2 != 0) {   // h???t d??ng nh??ng k?? t??? " kh??ng ch???n -> l??m ti???p
                                        content += "\n"         //v?? k set l???i dem n??n n?? v???n ti???p t???c t??m content
                                        continue
                                    }
                                    if (check % 2 == 0 && line[i] == ',') {             // k?? t??? " ch???n l?? t???i d???u , chuy???n sang giai ??o???n t??m content
                                        dem = 1
                                        check = 2           // reset check
                                        continue
                                    }
                                }
                                1 -> {
                                    localdate += line[i]
                                    if (check % 2 == 0 && i == line.length - 1) {    // k?? t??? " ch???n v?? h???t d??ng -> t??m xong
                                        check = 2
                                        dem = 0             // quay l???i t??m date
                                        break
                                    }

                                }
                            }
                        }
                        if (check % 2 == 0) {   // check xem content ???? ????? ch??a
                            if (content.contains("\"")) {
                                content = content.substring(1, content.length - 1)
                                content = content.replace("\"\"", "\"")
                            }
                            var notes = Notes(content, localdate, true)
                            localdate = ""
                            content = ""
                            dateList.add(notes)
                        }
                    } else    // tr?????ng h???p kh??ng c?? k?? t??? xu???ng d??ng ho???c d???u , " ho???c l?? chu???i content c???a d??ng tr??n
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