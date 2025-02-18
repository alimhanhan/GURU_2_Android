package com.example.db_project

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.UpdateAppearance
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var edtName: EditText
    lateinit var edtNumber: EditText
    lateinit var edtNameResult: EditText
    lateinit var edtNumberResult: EditText
    lateinit var btnInit: Button
    lateinit var btnInsert: Button
    lateinit var btnSelect: Button
    lateinit var btnRemove: Button
    lateinit var btnFix: Button

    lateinit var myHelper: myDBHelper
    lateinit var sqlDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtName = findViewById(R.id.editName)
        edtNumber = findViewById(R.id.editNumber)
        edtNameResult = findViewById(R.id.editNameResult)
        edtNumberResult = findViewById(R.id.editNumberResult)
        btnRemove = findViewById(R.id.btnRemove)
        btnFix = findViewById(R.id.btnFix)

        btnInit = findViewById(R.id.btnInit)
        btnInsert = findViewById(R.id.btnInsert)
        btnSelect = findViewById(R.id.btnSelect)

        myHelper = myDBHelper(this)

        btnInit.setOnClickListener {
            sqlDB = myHelper.writableDatabase
            myHelper.onUpgrade(sqlDB, 1, 2)
            sqlDB.close()
        }

        btnInsert.setOnClickListener {
            sqlDB = myHelper.writableDatabase

            sqlDB.execSQL("INSERT INTO groupTBL VALUES ('"+edtName.text.toString()+"', "
            +edtNumber.text.toString() + ");")
            sqlDB.close()
            Toast.makeText(applicationContext, "입력됨", Toast.LENGTH_SHORT).show()
        }

        btnSelect.setOnClickListener {
            sqlDB = myHelper.readableDatabase

            var cursor: Cursor
            cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null)

            var strNames = "그룹이름" + "\r\n" + "--------" + "\r\n"
            var strNumbers = "인원" + "\r\n" + "--------" + "\r\n"

            while (cursor.moveToNext())
            {
                strNames += cursor.getString(0) + "\r\n"
                strNumbers += cursor.getString(1) + "\r\n"
            }

            edtNameResult.setText(strNames)
            edtNumberResult.setText(strNumbers)

            cursor.close()
            sqlDB.close()
        }

        btnRemove.setOnClickListener {
            sqlDB = myHelper.writableDatabase
            sqlDB.execSQL("DELETE FROM groupTBL WHERE gName = '" + edtName + "'")
            sqlDB.close()
            btnRemove.callOnClick()
        }

        btnFix.setOnClickListener {
            sqlDB = myHelper.writableDatabase
            sqlDB.execSQL(" UPDATE groupTBL SET gNumber = 'edtNumber' WHERE gName='edtName'")
            sqlDB.close()
            btnFix.callOnClick()
        }
    }

    inner class myDBHelper(context: Context) : SQLiteOpenHelper(context, "groupDB", null, 1)
    {
        override fun onCreate(db: SQLiteDatabase?)
        {
            db!!.execSQL("CREATE TABLE groupTBL (gName CHAR(20) PRIMARY KEY, gNumber Integer);")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS groupTBL")
            onCreate(db)
        }
    }
}